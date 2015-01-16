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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import nivance.jpa.cassandra.prepare.schema.KeyPart;
import nivance.jpa.cassandra.prepare.schema.Ordering;

import org.springframework.data.mapping.model.SimpleTypeHolder;

import com.datastax.driver.core.DataType;

/**
 * {@link CassandraPersistentProperty} caching access to {@link #isIdProperty()} and {@link #getColumnName()}.
 * 
 * @author Alex Shvid
 */
public class CachingCassandraPersistentProperty extends BasicCassandraPersistentProperty {

	private Boolean isIdProperty;
	private Boolean hasEmbeddableType;
	private String columnName;
	private Caching<Ordering> ordering = new Caching<Ordering>();
	private DataType dataType;
	private Boolean isIndexed;
	private Caching<String> indexName = new Caching<String>();
	private Caching<KeyPart> keyPart = new Caching<KeyPart>();
	private Caching<Integer> ordinal = new Caching<Integer>();

	/**
	 * Creates a new {@link CachingCassandraPersistentProperty}.
	 * 
	 * @param field
	 * @param propertyDescriptor
	 * @param owner
	 * @param simpleTypeHolder
	 */
	public CachingCassandraPersistentProperty(Field field, PropertyDescriptor propertyDescriptor,
			CassandraPersistentEntity<?> owner, SimpleTypeHolder simpleTypeHolder) {
		super(field, propertyDescriptor, owner, simpleTypeHolder);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.cassandra.core.mapping.BasicCassandraPersistentProperty#isIdProperty()
	 */
	@Override
	public boolean isIdProperty() {

		if (this.isIdProperty == null) {
			this.isIdProperty = super.isIdProperty();
		}

		return this.isIdProperty;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.cassandra.core.mapping.BasicCassandraPersistentProperty#isCompositePrimaryKey()
	 */
	@Override
	public boolean hasEmbeddableType() {

		if (this.hasEmbeddableType == null) {
			this.hasEmbeddableType = super.hasEmbeddableType();
		}

		return this.hasEmbeddableType;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.cassandra.core.mapping.BasicCassandraPersistentProperty#getFieldName()
	 */
	@Override
	public String getColumnName() {

		if (this.columnName == null) {
			this.columnName = super.getColumnName();
		}

		return this.columnName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.cassandra.core.mapping.BasicCassandraPersistentProperty#getOrdering()
	 */
	@Override
	public Ordering getOrdering() {

		if (this.ordering.isNotCached()) {
			this.ordering.set(super.getOrdering());
		}

		return this.ordering.get();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.cassandra.core.mapping.BasicCassandraPersistentProperty#getDataType()
	 */
	@Override
	public DataType getDataType() {

		if (this.dataType == null) {
			this.dataType = super.getDataType();
		}

		return this.dataType;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.cassandra.core.mapping.BasicCassandraPersistentProperty#isIndexed()
	 */
	@Override
	public boolean isIndexed() {

		if (this.isIndexed == null) {
			this.isIndexed = super.isIndexed();
		}

		return this.isIndexed;
	}

	/**
	 * Returns index name for the column.
	 * 
	 * @return
	 */
	public String getIndexName() {

		if (this.indexName.isNotCached()) {
			this.indexName.set(super.getIndexName());
		}

		return this.indexName.get();

	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.cassandra.core.mapping.BasicCassandraPersistentProperty#getKeyPart()
	 */
	@Override
	public KeyPart getKeyPart() {

		if (this.keyPart.isNotCached()) {
			this.keyPart.set(super.getKeyPart());
		}

		return this.keyPart.get();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.cassandra.core.mapping.BasicCassandraPersistentProperty#getOrdinal()
	 */
	@Override
	public Integer getOrdinal() {

		if (this.ordinal.isNotCached()) {
			this.ordinal.set(super.getOrdinal());
		}

		return this.ordinal.get();
	}

	static class Caching<T> {
		private T value;
		private boolean cached = false;

		boolean isNotCached() {
			return !cached;
		}

		T get() {
			return value;
		}

		void set(T v) {
			value = v;
			cached = true;
		}
	}
}
