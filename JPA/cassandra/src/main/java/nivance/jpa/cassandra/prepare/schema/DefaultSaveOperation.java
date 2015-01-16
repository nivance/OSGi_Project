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
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * Implementation for SaveNewOperation
 * 
 * @author Alex Shvid
 * 
 */

public class DefaultSaveOperation<T> extends AbstractSaveOperation<T, SaveOperation> implements SaveOperation {
	
	private CassandraConverter cassandraConverter;

	public DefaultSaveOperation(Session session, CassandraConverter cassandraConverter,
			String tablename, T entity,boolean ifNotExists) {
		super(session, tablename, entity,ifNotExists);
		this.cassandraConverter = cassandraConverter;
	}


	@Override
	public RegularStatement createStatement() {

		Insert query = QueryBuilder.insertInto(getTableName());
		
		if(isIfNotExists()){
			query.ifNotExists();
		}

		cassandraConverter.write(entity, query);
		/*
		 * Add Ttl and Timestamp to Insert query
		 */
		if (getTtl() != null) {
			query.using(QueryBuilder.ttl(getTtl()));
		}
		if (getTimestamp() != null) {
			query.using(QueryBuilder.timestamp(getTimestamp()));
		}

		return query;
	}
	
	@Override
	public SaveOperation toTable(String tableName) {
		setTableName(tableName);
		return this;
	}

	@Override
	public SaveOperation withTimeToLive(int ttlSeconds) {
		setTtl(ttlSeconds);
		return this;
	}

	@Override
	public SaveOperation withTimestamp(long timestampMls) {
		setTimestamp(timestampMls);
		return this;
	}

}
