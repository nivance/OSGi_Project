/*
 * Copyright 2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nivance.jpa.cassandra.prepare.schema;

import java.util.LinkedList;
import java.util.List;

import nivance.jpa.cassandra.prepare.convert.CassandraConverter;
import nivance.jpa.cassandra.prepare.mapping.CassandraPersistentEntity;

import org.springframework.util.Assert;

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Delete.Where;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * Implementation of the DeleteOperation.
 * 
 * Supports two types of deletion operations: by Id and by Entity.
 * 
 * @author Alex Shvid
 * 
 */
public class DefaultDeleteOperation<T> extends AbstractUpdateOperation<DeleteOperation> implements DeleteOperation,
		BatchedStatementCreator {

	enum DeleteBy {
		ID, ENTITY, ALL;
	}

	private CassandraConverter converter;
	private final DeleteBy deleteBy;
	private final T entity;
	private final Class<T> entityClass;
	private final Object id;

	private String tableName;
	private Long timestamp;

	@SuppressWarnings("unchecked")
	public DefaultDeleteOperation(Session session, CassandraConverter converter, T entity) {
		super(session);
		Assert.notNull(entity);
		this.converter = converter;
		this.deleteBy = DeleteBy.ENTITY;
		this.entity = entity;
		this.entityClass = (Class<T>) entity.getClass();
		this.id = null;
	}

	public DefaultDeleteOperation(Session session, CassandraConverter converter, Class<T> entityClass, Object id) {
		super(session);
		Assert.notNull(entityClass);
		Assert.notNull(id);
		this.converter = converter;
		this.deleteBy = DeleteBy.ID;
		this.entity = null;
		this.entityClass = entityClass;
		this.id = id;
	}

	public DefaultDeleteOperation(Session session, CassandraConverter converter, Class<T> entityClass) {
		super(session);
		Assert.notNull(entityClass);
		this.converter = converter;
		this.deleteBy = DeleteBy.ALL;
		this.entity = null;
		this.entityClass = entityClass;
		this.id = null;
	}

	@Override
	public DeleteOperation fromTable(String tableName) {
		this.tableName = tableName;
		return this;
	}

	@Override
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public DeleteOperation withTimestamp(long timestampMls) {
		this.timestamp = timestampMls;
		return this;
	}

	private String getTableName() {
		if (tableName != null) {
			return tableName;
		}
		switch (deleteBy) {
			case ID:
			case ALL:
				return converter.getMappingContext().getPersistentEntity(entityClass).getTableName();
			case ENTITY:
				return converter.getMappingContext().getPersistentEntity(entity.getClass()).getTableName();
			}
		throw new IllegalArgumentException("invalid delete type " + deleteBy);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public RegularStatement createStatement() {
		if (deleteBy == DeleteBy.ALL) {
			return QueryBuilder.truncate(getTableName());
		}
		Delete.Selection ds = QueryBuilder.delete();
		Delete query = ds.from(getTableName());
		Where w = query.where();
		List<Clause> clauseList = null;
		switch (deleteBy) {
			case ID:
				CassandraPersistentEntity<?> persistenceEntity = converter.getMappingContext().getPersistentEntity(entityClass);
				clauseList = converter.getPrimaryKey(persistenceEntity, id);
				break;
			case ENTITY:
				clauseList = new LinkedList<Clause>();
				converter.write(entity, clauseList);
				break;
		}
		if (clauseList != null) {
			for (Clause c : clauseList) {
				w.and(c);
			}
		}
		if (timestamp != null) {
			query.using(QueryBuilder.timestamp(timestamp));
		}
		return query;
	}

}
