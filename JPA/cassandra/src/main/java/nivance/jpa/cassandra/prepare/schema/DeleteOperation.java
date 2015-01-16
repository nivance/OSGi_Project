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

/**
 * Base interface for delete operations
 * 
 * @author Alex Shvid
 * 
 */
public interface DeleteOperation extends QueryOperation<ResultSet, DeleteOperation> {

	/**
	 * Specifies table differ from entitie's table to delete
	 * 
	 * @param tableName table is using for deleting entity
	 * @return this
	 */
	DeleteOperation fromTable(String tableName);

	/**
	 * Specifies Timestamp (cell's timestamp in the Cassandra) in milliseconds for the deleting entity
	 * 
	 * @param timestamp Timestamp in milliseconds
	 * @return this
	 */
	DeleteOperation withTimestamp(long timestampMls);

}
