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

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;

/**
 * Abstract Get Operation
 * 
 * @author Alex Shvid
 * 
 * @param <T> - return Type
 */
public abstract class AbstractGetOperation<T> extends AbstractQueryOperation<T, GetOperation<T>> implements
		GetOperation<T>, StatementCreator {

	private String tableName;

	public abstract T transform(ResultSet resultSet);

	public AbstractGetOperation(Session session) {
		super(session);
	}

	@Override
	public GetOperation<T> formTable(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public String getTableName() {
		return tableName;
	}

	@Override
	public T execute() {
		Statement query = doCreateStatement(this);
		ResultSet resultSet = doExecute(query);
		return transform(resultSet);
	}

	protected T processWithFallback(ResultSet resultSet) {
		try {
			return transform(resultSet);
		} catch (RuntimeException e) {
			throw e;
		}
	}

}
