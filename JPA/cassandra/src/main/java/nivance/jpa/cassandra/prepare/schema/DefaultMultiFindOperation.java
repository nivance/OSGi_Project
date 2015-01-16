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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import nivance.jpa.cassandra.prepare.convert.CassandraConverter;
import nivance.jpa.cassandra.prepare.mapping.CassandraPersistentEntity;

import org.springframework.data.convert.EntityReader;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.google.common.base.Function;
import com.google.common.collect.Iterators;

/**
 * Default Find by Ids operation
 * 
 * @author Alex Shvid
 * 
 */

public class DefaultMultiFindOperation<T> extends AbstractMultiGetOperation<List<T>> {

	private final EntityReader<? super T, Object> entityReader;
	private final Class<T> entityClass;
	private final List<? extends T> entities;

	public DefaultMultiFindOperation(Session session,
			CassandraConverter cassandraConverter, Class<T> entityClass,List<? extends T> entities) {
		super(session);
		this.entityReader = cassandraConverter;
		this.entityClass = entityClass;
		this.entities = entities;
	}

	@Override
	public Iterator<Statement> getQueryIterator() {
		final CassandraPersistentEntity<?> cpe = ((CassandraConverter) entityReader).getMappingContext().getPersistentEntity(entityClass);
		final String tableName = getTableName() != null ? getTableName() : cpe.getTableName();

		return Iterators.transform(entities.iterator(), new Function<T, Statement>() {

			@Override
			public Statement apply(final T entity) {
				Select select = QueryBuilder.select().all().from(tableName);
				Select.Where w = select.where();
//				List<Clause> list = ((CassandraConverter) entityReader).getPrimaryKey(cpe, id);
				List<Clause> list = new LinkedList<Clause>();
				((CassandraConverter) entityReader).write(entity, list);
				for (Clause c : list) {
					w.and(c);
				}
				return select;
			}

		});

	}

	@Override
	public List<T> transform(List<ResultSet> resultSets) {

		List<T> result = new ArrayList<T>(resultSets.size());

		for (ResultSet resultSet : resultSets) {

			Row row = resultSet.one();
			if (row != null) {
				T obj = entityReader.read(entityClass, row);
				result.add(obj);
			}
		}

		return result;
	}

}
