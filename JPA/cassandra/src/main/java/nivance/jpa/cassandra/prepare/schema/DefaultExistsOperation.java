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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import nivance.jpa.cassandra.prepare.convert.CassandraConverter;
import nivance.jpa.cassandra.prepare.mapping.CassandraPersistentEntity;

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

/**
 * Exists operation implementation
 * 
 * @author Alex Shvid
 * 
 * @param <T> - entity by the type T
 */

public class DefaultExistsOperation<T> extends AbstractGetOperation<Boolean> {

	private final T entity;
	private final Class<T> entityClass;
	private final Object id;
	private final CassandraConverter converter;

	@SuppressWarnings("unchecked")
	public DefaultExistsOperation(Session session, CassandraConverter converter, T entity) {
		super(session);
		this.converter = converter;
		this.entity = entity;
		this.entityClass = (Class<T>) entity.getClass();
		this.id = null;
	}

	public DefaultExistsOperation(Session session, CassandraConverter converter, Class<T> entityClass, Object id) {
		super(session);
		this.converter = converter;
		this.entity = null;
		this.entityClass = entityClass;
		this.id = id;
	}

	@Override
	public Boolean transform(ResultSet resultSet) {
		Iterator<Row> i = resultSet.iterator();
		if (i.hasNext()) {
			Row row = i.next();
			long count = row.getLong(0);
			return count > 0;
		} else {
			return false;
		}
	}

	@Override
	public RegularStatement createStatement() {
		String tableName = getTableName();
		CassandraPersistentEntity<?> persistentEntity = converter.getMappingContext().getPersistentEntity(entityClass);
		if (tableName == null) {
			tableName = persistentEntity.getTableName();
		}
		Select select = QueryBuilder.select().countAll().from(tableName);
		Select.Where w = select.where();
		
		List<Clause> clauseList = null;
		if (entity != null) {
			clauseList = new LinkedList<Clause>();
			converter.write(entity, clauseList);
		} else {
			clauseList = converter.getPrimaryKey(persistentEntity, id);
		}
		for (Clause c : clauseList) {
			w.and(c);
		}
		return select;
	}
}
