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

import nivance.jpa.cassandra.prepare.convert.CassandraConverter;

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

/**
 * Count operation implementation
 * 
 * @author Alex Shvid
 * 
 * @param <T> - entity by the type T
 */

public class DefaultCountOperation<T> extends AbstractGetOperation<Long> {

	private final Class<T> entityClass;
	private CassandraConverter converter;

	public DefaultCountOperation(Session session, CassandraConverter converter, Class<T> entityClass) {
		super(session);
		this.entityClass = entityClass;
		this.converter = converter;
	}

	@Override
	public Long transform(ResultSet resultSet) {
		Iterator<Row> i = resultSet.iterator();
		if (i.hasNext()) {
			Row row = i.next();
			long count = row.getLong(0);
			return count;
		} else {
			return 0L;
		}
	}

	@Override
	public RegularStatement createStatement() {
		String tableName = getTableName();
		if (tableName == null) {
			tableName = converter.getMappingContext()
					.getPersistentEntity(entityClass).getTableName();
		}
		Select select = QueryBuilder.select().countAll().from(tableName);
		return select;
	}

}
