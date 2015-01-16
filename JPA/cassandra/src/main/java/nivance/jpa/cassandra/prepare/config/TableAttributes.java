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
 * Table attributes are used for manipulation around table at the startup (create/update/validate).
 * 
 * @author Alex Shvid
 */
public class TableAttributes {

	private String tableName;
	private String entityClass;

	public String getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(String name) {
		this.entityClass = name;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String name) {
		this.tableName = name;
	}

	@Override
	public String toString() {
		return "TableAttributes [entityClass=" + entityClass + ", tableName=" + tableName + "]";
	}

}
