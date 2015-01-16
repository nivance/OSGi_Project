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
import com.datastax.driver.core.Session;


public class FreeUpdateOperation<T> extends
		AbstractUpdateOperation<UpdateOperation> implements UpdateOperation {

	private RegularStatement statement;

	public FreeUpdateOperation(Session session,RegularStatement statement) {
		super(session);
		this.statement = statement;
	}

	@Override
	public RegularStatement createStatement() {
		return statement;
	}

	@Override
	public void setTableName(String tableName) {
//		this.tablename = tableName;
	}

}
