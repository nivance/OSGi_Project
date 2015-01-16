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
package nivance.jpa.cassandra.prepare.core;

import java.util.Collection;
import java.util.List;

import nivance.jpa.cassandra.prepare.config.TableAttributes;
import nivance.jpa.cassandra.prepare.convert.CassandraConverter;
import nivance.jpa.cassandra.prepare.mapping.Table;
import nivance.jpa.cassandra.prepare.schema.DefaultSchemaCqlOperations;
import nivance.jpa.cassandra.prepare.schema.SchemaCqlOperations;
import nivance.jpa.cassandra.prepare.schema.UpdateOperation;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.google.common.base.Optional;

/**
 * Convenient factory for configuring a Cassandra Session.
 * 
 * @author Alex Shvid
 */

public class CassandraSessionFactoryBean extends SessionFactoryBean implements FactoryBean<Session>, InitializingBean,
		DisposableBean, BeanClassLoaderAware, PersistenceExceptionTranslator {

	private ClassLoader beanClassLoader;
	private CassandraConverter converter;
//	private MappingContext<? extends CassandraPersistentEntity<?>, CassandraPersistentProperty> mappingContext;//add 2014.02.10
	private Collection<TableAttributes> tables;
	private SchemaOperations schemaOperations;//add 2014.02.10
	private SchemaCqlOperations schemaCqlOperations;//add 2014.02.10

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}

	@Override
	public void afterPropertiesSet() {
		if (converter == null) {
			throw new IllegalArgumentException("converter is required");
		}
		super.afterPropertiesSet();
		
//		mappingContext = converter.getMappingContext();
		schemaOperations = new DefaultSchemaOperations(session, keyspace, converter);
		schemaCqlOperations = new DefaultSchemaCqlOperations(session, keyspace);
		
		if (StringUtils.hasText(keyspace)) {
			if (!CollectionUtils.isEmpty(tables)) {
				for (TableAttributes tableAttributes : tables) {
					String entityClassName = tableAttributes.getEntityClass();
					Class<?> entityClass = loadClass(entityClassName);
					String useTableName = tableAttributes.getTableName() != null ? tableAttributes.getTableName()
							: this.getTableName(entityClass);
					if (keyspaceCreated) {
						createNewTable(useTableName, entityClass);
					} else if (keyspaceAttributes.isUpdate()) {
						TableMetadata table = schemaCqlOperations.getTableMetadata(useTableName);
						if (table == null) {
							createNewTable(useTableName, entityClass);
						} else {
							Optional<UpdateOperation> alter = schemaOperations.alterTable(useTableName, entityClass,
									true);
							if (alter.isPresent()) {
								alter.get().execute();
							}
							schemaOperations.alterIndexes(useTableName, entityClass).execute();
						}
					} else if (keyspaceAttributes.isValidate()) {
						TableMetadata table = schemaCqlOperations.getTableMetadata(useTableName);
						if (table == null) {
							throw new InvalidDataAccessApiUsageException("not found table " + useTableName + " for entity "
									+ entityClassName);
						}

						String query = schemaOperations.validateTable(useTableName, entityClass);

						if (query != null) {
							throw new InvalidDataAccessApiUsageException("invalid table " + useTableName + " for entity "
									+ entityClassName + ". modify it by " + query);
						}

						List<String> queryList = schemaOperations.validateIndexes(useTableName, entityClass);

						if (!queryList.isEmpty()) {
							throw new InvalidDataAccessApiUsageException("invalid indexes in table " + useTableName + " for entity "
									+ entityClassName + ". modify it by " + queryList);
						}
					}
				}
			}
		}
	}

	private Class<?> loadClass(String className) {
		try {
			return ClassUtils.forName(className, this.beanClassLoader);
		} catch (Exception e) {
			throw new IllegalArgumentException("class not found " + className, e);
		}
	}
	
	public String getTableName(Class<?> entityClass) {
		if (entityClass == null) {
			throw new InvalidDataAccessApiUsageException(
					"No class parameter provided, entity table name can't be determined!");
		}
		Table table = entityClass.getAnnotation(Table.class);
		if(table==null){
			throw new InvalidDataAccessApiUsageException("Tablename is null:: "
					+ entityClass.getName());
		}
		
//		CassandraPersistentEntity<?> entity = mappingContext.getPersistentEntity(entityClass);
//		if (entity == null) {
//			throw new InvalidDataAccessApiUsageException("No Persitent Entity information found for the class "
//					+ entityClass.getName());
//		}
		return table.name();
	}

	private void createNewTable(String useTableName, Class<?> entityClass) {
		schemaOperations.createTable(useTableName, entityClass).execute();
		schemaOperations.createIndexes(useTableName, entityClass).execute();
	}

	public void setConverter(CassandraConverter converter) {
		this.converter = converter;
	}

	public void setTables(Collection<TableAttributes> tables) {
		this.tables = tables;
	}

}
