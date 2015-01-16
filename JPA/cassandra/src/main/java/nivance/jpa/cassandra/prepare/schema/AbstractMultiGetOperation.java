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
import java.util.List;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;

/**
 * Abstract Multi Get Operation
 * 
 * @author Alex Shvid
 * 
 * @param <T> - return Type
 */

public abstract class AbstractMultiGetOperation<T> extends AbstractQueryOperation<T, GetOperation<T>> implements
		GetOperation<T> {

	private String tableName;

	public abstract Iterator<Statement> getQueryIterator();

	public abstract T transform(List<ResultSet> resultSets);

	public AbstractMultiGetOperation(Session session) {
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
		Iterator<Statement> queryIterator = getQueryIterator();
		List<ResultSet> resultSets = doExecute(queryIterator);
		return transform(resultSets);
	}


	protected T processWithFallback(List<ResultSet> resultSets) {
		try {
			return transform(resultSets);
		} catch (RuntimeException e) {
			throw e;
		}
	}

}
