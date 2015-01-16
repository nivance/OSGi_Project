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
 * Base interface to save entity (actually update).
 * 
 * @author Alex Shvid
 * 
 */
public interface SaveOperation extends QueryOperation<ResultSet, SaveOperation> {

	/**
	 * Specifies table differ from entitie's table to save
	 * 
	 * @param tableName table name is using to save entity
	 * @return this
	 */
	SaveOperation toTable(String tableName);

	/**
	 * Specifies fields that actually have to be saved. By default will save all fields in the entity.
	 * 
	 * @param fields Array of selected fields
	 * @return this
	 */
	//SaveOperation selectedFields(String... fields);

	/**
	 * Specifies fields that actually have to be saved. By default will save all fields in the entity. The only fields
	 * that have special tag will be saved in this update. Tags are integers and usually constants that defined by @Tag
	 * annotation
	 * 
	 * @param tags Array of tagged fields
	 * @return this
	 */
	//SaveOperation taggedFields(int... tags);

	/**
	 * Specifies TTL (time to live) in seconds for the saved entity in the Cassandra
	 * 
	 * @param ttlSeconds Time to live in seconds
	 * @return this
	 */
	SaveOperation withTimeToLive(int ttlSeconds);

	/**
	 * Specifies Timestamp (cell's timestamp in the Cassandra) in milliseconds for the saved entity in the Cassandra
	 * 
	 * @param timestamp Timestamp in milliseconds
	 * @return this
	 */
	SaveOperation withTimestamp(long timestampMls);

}
