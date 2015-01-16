/*
 * Copyright 2013 the original author or authors.
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
package nivance.jpa.cassandra.prepare.spec;

import static nivance.jpa.cassandra.prepare.util.CqlStringUtils.checkIdentifier;
import static nivance.jpa.cassandra.prepare.util.CqlStringUtils.identifize;

/**
 * Builder class to construct a <code>CREATE INDEX</code> specification.
 * 
 * @author Alex Shvid
 */
public class CreateIndexSpecification extends WithNameSpecification<CreateIndexSpecification> {

	/**
	 * The name of the table.
	 */
	private String table;

	/**
	 * The name of the column.
	 */
	private String column;

	/**
	 * Sets the table name.
	 * 
	 * @return this
	 */
	public CreateIndexSpecification on(String table) {
		checkIdentifier(table);
		this.table = table;
		return this;
	}

	public String getTable() {
		return table;
	}

	public String getTableAsIdentifier() {
		return identifize(table);
	}

	/**
	 * Sets the column name.
	 * 
	 * @return this
	 */
	public CreateIndexSpecification column(String column) {
		checkIdentifier(column);
		this.column = column;
		return this;
	}

	public String getColumn() {
		return column;
	}

	public String getColumnAsIdentifier() {
		return identifize(column);
	}

}
