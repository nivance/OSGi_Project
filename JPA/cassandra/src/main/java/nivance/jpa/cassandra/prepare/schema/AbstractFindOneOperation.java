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

import nivance.jpa.cassandra.prepare.convert.CassandraConverter;

import org.springframework.data.convert.EntityReader;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 * Abstract Find One Operation
 * 
 * @author Alex Shvid
 * 
 * @param <T>
 *            - return Type
 */
public abstract class AbstractFindOneOperation<T> extends AbstractGetOperation<T> {

	protected final EntityReader<? super T, Object> entityReader;
	protected final Class<T> entityClass;

	public AbstractFindOneOperation(Session session,
			CassandraConverter cassandraConverter, Class<T> entityClass) {
		super(session);
		this.entityReader = cassandraConverter;
		this.entityClass = entityClass;
	}

	@Override
	public String getTableName() {
		String tableName = super.getTableName();
		if (tableName != null) {
			return tableName;
		}
		return ((CassandraConverter) entityReader).getMappingContext()
				.getPersistentEntity(entityClass).getTableName();
	}

	@Override
	public T transform(ResultSet resultSet) {

		Row row = resultSet.one();
		if (row != null) {
			return entityReader.read(entityClass, row);
		}
		return null;

	}

}
