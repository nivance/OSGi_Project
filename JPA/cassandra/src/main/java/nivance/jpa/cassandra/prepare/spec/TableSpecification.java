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

import static nivance.jpa.cassandra.prepare.schema.KeyPart.CLUSTERING;
import static nivance.jpa.cassandra.prepare.schema.KeyPart.PARTITION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nivance.jpa.cassandra.prepare.option.TableOption;
import nivance.jpa.cassandra.prepare.schema.KeyPart;
import nivance.jpa.cassandra.prepare.schema.Ordering;

import com.datastax.driver.core.DataType;

/**
 * Builder class to support the construction of table specifications that have columns. This class can also be used as a
 * standalone {@link TableDescriptor}, independent of {@link CreateTableSpecification}.
 * 
 * @author Matthew T. Adams
 * @author Alex Shvid
 */
public class TableSpecification<T extends WithOptionsSpecification<TableOption, T>> extends
		WithOptionsSpecification<TableOption, T> implements TableDescriptor {

	/**
	 * Ordered List of only those columns that comprise the partition key.
	 */
	private List<ColumnSpecification> partitionKeyColumns = new ArrayList<ColumnSpecification>();

	/**
	 * Ordered List of only those columns that comprise the primary key that are not also part of the partition key.
	 */
	private List<ColumnSpecification> clusteringKeyColumns = new ArrayList<ColumnSpecification>();

	/**
	 * List of only those columns that are not partition or primary key columns.
	 */
	private List<ColumnSpecification> nonKeyColumns = new ArrayList<ColumnSpecification>();

	/**
	 * Adds the given non-key column to the table.
	 * 
	 * @param name The column name; must be a valid unquoted or quoted identifier without the surrounding double quotes.
	 * @param type The data type of the column.
	 */
	public T column(String name, DataType type) {
		return column(name, type, null, null);
	}

	/**
	 * Adds the given partition key column to the table.
	 * 
	 * @param name The column name; must be a valid unquoted or quoted identifier without the surrounding double quotes.
	 * @param type The data type of the column.
	 * @return this
	 */
	public T partitionKeyColumn(String name, DataType type) {
		return column(name, type, PARTITION, null);
	}

	/**
	 * Adds the given primary key column to the table with ascending ordering.
	 * 
	 * @param name The column name; must be a valid unquoted or quoted identifier without the surrounding double quotes.
	 * @param type The data type of the column.
	 * @return this
	 */
	public T clusteringKeyColumn(String name, DataType type) {
		return clusteringKeyColumn(name, type, null);
	}

	/**
	 * Adds the given primary key column to the table with the given ordering (<code>null</code> meaning ascending).
	 * 
	 * @param name The column name; must be a valid unquoted or quoted identifier without the surrounding double quotes.
	 * @param type The data type of the column.
	 * @param ordering If the given {@link KeyPart} is {@link KeyPart#CLUSTERING}, then the given ordering is used, else
	 *          ignored.
	 * @return this
	 */
	public T clusteringKeyColumn(String name, DataType type, Ordering ordering) {
		return column(name, type, CLUSTERING, ordering);
	}

	/**
	 * Adds the given info as a new column to the table.
	 * 
	 * @param name The column name; must be a valid unquoted or quoted identifier without the surrounding double quotes.
	 * @param type The data type of the column.
	 * @param keyPart Indicates key type. Null means that the column is not a key column.
	 * @param ordering If the given {@link KeyPart} is {@link KeyPart#CLUSTERING}, then the given ordering is used, else
	 *          ignored.
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	protected T column(String name, DataType type, KeyPart keyPart, Ordering ordering) {

		ColumnSpecification column = new ColumnSpecification().name(name).type(type).keyPart(keyPart)
				.ordering(keyPart == CLUSTERING ? ordering : null);

		if (keyPart == KeyPart.PARTITION) {
			partitionKeyColumns.add(column);
		}

		if (keyPart == KeyPart.CLUSTERING) {
			clusteringKeyColumns.add(column);
		}

		if (keyPart == null) {
			nonKeyColumns.add(column);
		}

		return (T) this;
	}

	/**
	 * Returns an unmodifiable list of all columns. Order is important: partition key columns, clustering key columns, non
	 * key columns
	 */
	public List<ColumnSpecification> getAllColumns() {

		ArrayList<ColumnSpecification> allKeyColumns = new ArrayList<ColumnSpecification>(partitionKeyColumns.size()
				+ clusteringKeyColumns.size() + nonKeyColumns.size());
		allKeyColumns.addAll(partitionKeyColumns);
		allKeyColumns.addAll(clusteringKeyColumns);
		allKeyColumns.addAll(nonKeyColumns);

		return Collections.unmodifiableList(allKeyColumns);
	}

	/**
	 * Returns an unmodifiable list of all partition key columns.
	 */
	public List<ColumnSpecification> getPartitionKeyColumns() {
		return Collections.unmodifiableList(partitionKeyColumns);
	}

	/**
	 * Returns an unmodifiable list of all primary key columns that are not also partition key columns.
	 */
	public List<ColumnSpecification> getClusteringKeyColumns() {
		return Collections.unmodifiableList(clusteringKeyColumns);
	}

	/**
	 * Returns an unmodifiable list of all primary key columns that are not also partition key columns.
	 */
	public List<ColumnSpecification> getPrimaryKeyColumns() {

		ArrayList<ColumnSpecification> primaryKeyColumns = new ArrayList<ColumnSpecification>(partitionKeyColumns.size()
				+ clusteringKeyColumns.size());
		primaryKeyColumns.addAll(partitionKeyColumns);
		primaryKeyColumns.addAll(clusteringKeyColumns);

		return Collections.unmodifiableList(primaryKeyColumns);
	}

	/**
	 * Returns an unmodifiable list of all non-key columns.
	 */
	public List<ColumnSpecification> getNonKeyColumns() {
		return Collections.unmodifiableList(nonKeyColumns);
	}
}
