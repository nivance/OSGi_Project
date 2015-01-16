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

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.SimpleStatement;

/**
 * Simple query creator
 * 
 * @author Alex Shvid
 * 
 */

public class SimpleStatementCreator implements StatementCreator {

	private final RegularStatement statement;

	public SimpleStatementCreator(String cql) {
		statement = new SimpleStatement(cql);
	}

	public SimpleStatementCreator(RegularStatement query) {
		statement = query;
	}

	public SimpleStatementCreator withConsistencyLevel(ConsistencyLevel consistency) {
		if (consistency != null) {
			statement.setConsistencyLevel(ConsistencyLevelResolver.resolve(consistency));
		}
		return this;
	}

	public SimpleStatementCreator withRetryPolicy(RetryPolicy retryPolicy) {
		if (retryPolicy != null) {
			statement.setRetryPolicy(RetryPolicyResolver.resolve(retryPolicy));
		}
		return this;
	}

	public SimpleStatementCreator enableTracing() {
		statement.enableTracing();
		return this;
	}

	public SimpleStatementCreator disableTracing() {
		statement.disableTracing();
		return this;
	}

	public SimpleStatementCreator withQueryTracing(Boolean queryTracing) {
		if (queryTracing != null) {
			if (queryTracing.booleanValue()) {
				statement.enableTracing();
			} else {
				statement.disableTracing();
			}
		}
		return this;
	}

	@Override
	public RegularStatement createStatement() {
		return statement;
	}

}
