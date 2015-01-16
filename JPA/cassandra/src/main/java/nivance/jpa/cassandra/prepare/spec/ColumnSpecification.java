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
import static nivance.jpa.cassandra.prepare.schema.Ordering.ASCENDING;
import static nivance.jpa.cassandra.prepare.util.CqlStringUtils.checkIdentifier;
import static nivance.jpa.cassandra.prepare.util.CqlStringUtils.identifize;
import static nivance.jpa.cassandra.prepare.util.CqlStringUtils.noNull;
import nivance.jpa.cassandra.prepare.schema.KeyPart;
import nivance.jpa.cassandra.prepare.schema.Ordering;

import com.datastax.driver.core.DataType;

/**
 * Builder class to help construct CQL statements that involve column manipulation. Not threadsafe.
 * <p/>
 * Use {@link #name(String)} and {@link #type(String)} to set the name and type of the column, respectively. To specify
 * a <code>PRIMARY KEY</code> column, use {@link #clusteringKeyPart()} or {@link #clusteringKeyPart(Ordering)}. To
 * specify that the <code>PRIMARY KEY</code> column is or is part of the partition key, use {@link #partitionKeyPart()}
 * instead of {@link #clusteringKeyPart()} or {@link #clusteringKeyPart(Ordering)}.
 * 
 * @author Matthew T. Adams
 * @author Alex Shvid
 */
public class ColumnSpecification {

	/**
	 * Default ordering of primary key fields; value is {@link Ordering#ASCENDING}.
	 */
	public static final Ordering DEFAULT_ORDERING = ASCENDING;

	private String name;
	private DataType type; // TODO: determining if we should be coupling this to Datastax Java Driver type?
	private KeyPart keyPart;
	private Ordering ordering;

	/**
	 * Sets the column's name.
	 * 
	 * @return this
	 */
	public ColumnSpecification name(String name) {
		checkIdentifier(name);
		this.name = name;
		return this;
	}

	/**
	 * Sets the column's type.
	 * 
	 * @return this
	 */
	public ColumnSpecification type(DataType type) {
		this.type = type;
		return this;
	}

	/**
	 * Identifies this column as a primary key column. Sets the column's {@link #keyPart} and {@link #ordering} for
	 * {@link KeyPart#CLUSTERING} key part.
	 * 
	 * @return this
	 */
	public ColumnSpecification primaryKeyPart(KeyPart keyPart, Ordering ordering) {
		return primaryKeyPart(keyPart, ordering);
	}

	/**
	 * Identifies this column as a primary key column. Sets the column's {@link #keyPart} and {@link #ordering} for
	 * {@link KeyPart#CLUSTERING} key part.
	 * 
	 * @return this
	 */
	public ColumnSpecification primaryKeyPart(KeyPart keyPart, Ordering ordering, boolean enable) {
		if (keyPart == KeyPart.PARTITION) {
			return partitionKeyPart(enable);
		} else if (keyPart == KeyPart.CLUSTERING) {
			return clusteringKeyPart(ordering, enable);
		} else {
			throw new IllegalArgumentException("null or unknown KeyPart " + keyPart);
		}
	}

	/**
	 * Identifies this column as a primary key column that is also part of a partition key. Sets the column's
	 * {@link #keyPart} to {@link KeyPart#PARTITION} and its {@link #ordering} to <code>null</code>.
	 * 
	 * @return this
	 */
	public ColumnSpecification partitionKeyPart() {
		return partitionKeyPart(true);
	}

	/**
	 * Toggles the identification of this column as a primary key column that also is or is part of a partition key. Sets
	 * {@link #ordering} to <code>null</code> and, if the given boolean is <code>true</code>, then sets the column's
	 * {@link #keyPart} to {@link KeyPart#PARTITION}, else sets it to <code>null</code>.
	 * 
	 * @return this
	 */
	public ColumnSpecification partitionKeyPart(boolean enable) {
		this.keyPart = enable ? PARTITION : null;
		this.ordering = null;
		return this;
	}

	/**
	 * Identifies this column as a primary key column with default ordering. Sets the column's {@link #keyPart} to
	 * {@link KeyPart#CLUSTERING} and its {@link #ordering} to {@link #DEFAULT_ORDERING}.
	 * 
	 * @return this
	 */
	public ColumnSpecification clusteringKeyPart() {
		return clusteringKeyPart(DEFAULT_ORDERING);
	}

	/**
	 * Identifies this column as a primary key column with the given ordering. Sets the column's {@link #keyPart} to
	 * {@link KeyPart#CLUSTERING} and its {@link #ordering} to the given {@link Ordering}.
	 * 
	 * @return this
	 */
	public ColumnSpecification clusteringKeyPart(Ordering order) {
		return clusteringKeyPart(order, true);
	}

	/**
	 * Toggles the identification of this column as a primary key column. If the given boolean is <code>true</code>, then
	 * sets the column's {@link #keyPart} to {@link KeyPart#PARTITION} and {@link #ordering} to the given {@link Ordering}
	 * , else sets both {@link #keyPart} and {@link #ordering} to <code>null</code>.
	 * 
	 * @return this
	 */
	public ColumnSpecification clusteringKeyPart(Ordering ordering, boolean enable) {
		this.keyPart = enable ? CLUSTERING : null;
		this.ordering = enable ? ordering : null;
		return this;
	}

	/**
	 * Sets the column's {@link #keyPart}.
	 * 
	 * @return this
	 */
	/* package */ColumnSpecification keyPart(KeyPart keyPart) {
		this.keyPart = keyPart;
		return this;
	}

	/**
	 * Sets the column's {@link #ordering}.
	 * 
	 * @return this
	 */
	/* package */ColumnSpecification ordering(Ordering ordering) {
		this.ordering = ordering;
		return this;
	}

	public String getName() {
		return name;
	}

	public String getNameAsIdentifier() {
		return identifize(name);
	}

	public DataType getType() {
		return type;
	}

	public KeyPart getKeyPart() {
		return keyPart;
	}

	public Ordering getOrdering() {
		return ordering;
	}

	public String toCql() {
		return toCql(null).toString();
	}

	public StringBuilder toCql(StringBuilder cql) {
		return (cql = noNull(cql)).append(name).append(" ").append(type);
	}

	@Override
	public String toString() {
		return toCql(null).append(" /* keyPart=").append(keyPart).append(", ordering=").append(ordering).append(" */ ")
				.toString();
	}
}