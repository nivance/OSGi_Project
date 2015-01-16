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
package nivance.jpa.cassandra.prepare.generator;

import static nivance.jpa.cassandra.prepare.schema.KeyPart.CLUSTERING;
import static nivance.jpa.cassandra.prepare.schema.KeyPart.PARTITION;
import static nivance.jpa.cassandra.prepare.util.CqlStringUtils.noNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nivance.jpa.cassandra.prepare.spec.ColumnSpecification;
import nivance.jpa.cassandra.prepare.spec.CreateTableSpecification;

/**
 * CQL generator for generating a <code>CREATE TABLE</code> statement.
 * 
 * @author Alex Shvid
 * @author Matthew T. Adams
 */
public class CreateTableCqlGenerator extends TableCqlGenerator<CreateTableSpecification> {

	public CreateTableCqlGenerator(CreateTableSpecification specification) {
		super(specification);
	}

	public StringBuilder toCql(StringBuilder cql) {
		cql = preambleCql(cql);
		return columnsAndOptionsCql(cql).append(";");
	}

	protected StringBuilder preambleCql(StringBuilder cql) {
		return noNull(cql).append("CREATE TABLE ").append(spec().getIfNotExists() ? "IF NOT EXISTS " : "")
				.append(spec().getNameAsIdentifier());
	}

	protected StringBuilder columnsAndOptionsCql(StringBuilder cql) {

		cql = noNull(cql);

		// begin columns
		cql.append(" (");

		List<ColumnSpecification> partitionKey = new ArrayList<ColumnSpecification>();
		List<ColumnSpecification> clusteringKey = new ArrayList<ColumnSpecification>();
		for (ColumnSpecification col : spec().getAllColumns()) {
			col.toCql(cql).append(", ");

			if (col.getKeyPart() == PARTITION) {
				partitionKey.add(col);
			} else if (col.getKeyPart() == CLUSTERING) {
				clusteringKey.add(col);
			}
		}

		// begin primary key clause
		cql.append("PRIMARY KEY (");

		if (partitionKey.size() > 1) {
			// begin partition key clause
			cql.append("(");
		}
		
		appendColumnNames(cql, partitionKey);

		if (partitionKey.size() > 1) {
			cql.append(")");
			// end partition key clause
		}

		if (!clusteringKey.isEmpty()) {
			cql.append(", ");
		}

		appendColumnNames(cql, clusteringKey);

		cql.append(")");
		// end primary key clause

		cql.append(")");
		// end columns

		return optionsCql(cql, createOrderingClause(clusteringKey));

	}

	private static StringBuilder createOrderingClause(List<ColumnSpecification> columns) {
		StringBuilder ordering = null;
		boolean first = true;
		for (ColumnSpecification col : columns) {

			if (col.getOrdering() != null) { // then ordering specified
				if (ordering == null) { // then initialize ordering clause
					ordering = new StringBuilder().append("CLUSTERING ORDER BY (");
				}
				if (first) {
					first = false;
				} else {
					ordering.append(", ");
				}
				ordering.append(col.getName()).append(" ").append(col.getOrdering().cql());
			}
		}
		if (ordering != null) { // then end ordering option
			ordering.append(")");
		}
		return ordering;
	}

	private static void appendColumnNames(StringBuilder str, List<ColumnSpecification> columns) {

		Collections.sort(columns, new Comparator<ColumnSpecification>() {

			@Override
			public int compare(ColumnSpecification o1, ColumnSpecification o2) {
				return 0;
			}
		});
		boolean first = true;
		for (ColumnSpecification col : columns) {
			if (first) {
				first = false;
			} else {
				str.append(", ");
			}
			str.append(col.getName());

		}

	}

}
