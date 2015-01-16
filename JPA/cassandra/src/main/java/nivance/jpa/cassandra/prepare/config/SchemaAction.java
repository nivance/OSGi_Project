/*
 * Copyright 2013-2014 the original author or authors.
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
package nivance.jpa.cassandra.prepare.config;

/**
 * SchemaAction
 * 
 * Possible values: *validate: validate the keyspace schema, makes no changes. *update: updates the keyspace schema.
 * *create: creates the keyspace and tables, destroying previous data. *create-drop: drops the keyspace at the end of
 * the session.
 * 
 * @author Alex Shvid
 * 
 */
public enum SchemaAction {

	VALIDATE, UPDATE, CREATE, CREATE_DROP;

}
