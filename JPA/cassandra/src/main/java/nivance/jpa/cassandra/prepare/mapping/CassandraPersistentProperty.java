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
package nivance.jpa.cassandra.prepare.mapping;

import nivance.jpa.cassandra.prepare.schema.KeyPart;
import nivance.jpa.cassandra.prepare.schema.Ordering;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mapping.PersistentProperty;

import com.datastax.driver.core.DataType;

/**
 * Cassandra specific {@link org.springframework.data.mapping.PersistentProperty} extension.
 * 
 * @author Alex Shvid
 */
public interface CassandraPersistentProperty extends PersistentProperty<CassandraPersistentProperty> {

	/**
	 * Returns the true if the field has embedded type.
	 * 
	 * @return
	 */
	boolean hasEmbeddableType();

	/**
	 * Returns the name of the field a property is persisted to.
	 * 
	 * @return
	 */
	String getColumnName();

	/**
	 * Returns ordering for the column. Valid only for clustered columns.
	 * 
	 * @return
	 */
	Ordering getOrdering();

	/**
	 * Returns the data type.
	 * 
	 * @return
	 */
	DataType getDataType();

	/**
	 * Returns true if the property has index on this column.
	 * 
	 * @return
	 */
	boolean isIndexed();

	/**
	 * Returns index name for the column.
	 * 
	 * @return
	 */
	String getIndexName();

	/**
	 * Returns keyPart if the property has KeyColumn annotation.
	 * 
	 * @return
	 */
	KeyPart getKeyPart();

	/**
	 * Returns key ordinal, number of the property in the composite primary key.
	 * 
	 * @return
	 */
	Integer getOrdinal();

	/**
	 * Simple {@link Converter} implementation to transform a {@link CassandraPersistentProperty} into its column name.
	 * 
	 * @author Alex Shvid
	 */
	public enum PropertyToColumnNameConverter implements Converter<CassandraPersistentProperty, String> {

		INSTANCE;

		/*
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
		 */
		public String convert(CassandraPersistentProperty source) {
			return source.getColumnName();
		}
	}

}
