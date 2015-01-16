/*
 * Copyright 2013 by the original author(s).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nivance.jpa.cassandra.prepare.core;

import java.io.Serializable;

import nivance.jpa.cassandra.prepare.mapping.CassandraEntityInformation;
import nivance.jpa.cassandra.prepare.mapping.CassandraPersistentEntity;
import nivance.jpa.cassandra.prepare.mapping.CassandraPersistentProperty;

import org.springframework.data.mapping.model.BeanWrapper;
import org.springframework.data.repository.core.support.AbstractEntityInformation;

/**
 * {@link CassandraEntityInformation} implementation using a {@link CassandraPersistentEntity} instance to lookup the
 * necessary information. Can be configured with a custom collection to be returned which will trump the one returned by
 * the {@link CassandraPersistentEntity} if given.
 * 
 * @author Alex Shvid
 * 
 */
public class MappingCassandraEntityInformation<T, ID extends Serializable> extends AbstractEntityInformation<T, ID>
		implements CassandraEntityInformation<T, ID> {

	private final CassandraPersistentEntity<T> entityMetadata;
	private final String customTableName;

	/**
	 * Creates a new {@link MappingCassandraEntityInformation} for the given {@link CassandraPersistentEntity}.
	 * 
	 * @param entity must not be {@literal null}.
	 */
	public MappingCassandraEntityInformation(CassandraPersistentEntity<T> entity) {
		this(entity, null);
	}

	/**
	 * Creates a new {@link MappingCassandraEntityInformation} for the given {@link CassandraPersistentEntity} and custom
	 * table name.
	 * 
	 * @param entity must not be {@literal null}.
	 * @param customTableName
	 */
	public MappingCassandraEntityInformation(CassandraPersistentEntity<T> entity, String customTableName) {
		super(entity.getType());
		this.entityMetadata = entity;
		this.customTableName = customTableName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ID getId(T entity) {

		CassandraPersistentProperty idProperty = entityMetadata.getIdProperty();

		if (idProperty == null) {
			return null;
		}

		try {
			return (ID) BeanWrapper.create(entity, null).getProperty(idProperty);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<ID> getIdType() {
		return (Class<ID>) entityMetadata.getIdProperty().getType();
	}

	@Override
	public String getTableName() {
		return customTableName == null ? entityMetadata.getTableName() : customTableName;
	}

}
