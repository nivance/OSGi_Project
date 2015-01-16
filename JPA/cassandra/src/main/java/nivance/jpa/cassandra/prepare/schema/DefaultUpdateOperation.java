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

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Update;

/**
 * Implementation for SaveOperation
 * 
 * @author Alex Shvid
 * 
 */

public class DefaultUpdateOperation<T> extends
		AbstractUpdateOperation<UpdateOperation> implements UpdateOperation {

	private String tablename;
	private CassandraConverter cassandraConverter;
	private T entity;

	public DefaultUpdateOperation(Session session,
			CassandraConverter cassandraConverter, String tablename, T entity) {
		super(session);
		this.tablename = tablename;
		this.cassandraConverter = cassandraConverter;
		this.entity = entity;
	}

	@Override
	public RegularStatement createStatement() {
		Update query = QueryBuilder.update(tablename);
		cassandraConverter.write(entity, query);
		return query;
	}

	@Override
	public void setTableName(String tableName) {
		// TODO Auto-generated method stub
		this.tablename = tableName;
	}

}
