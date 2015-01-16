/*
 * Copyright 2014 the original author or authors.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import nivance.jpa.cassandra.prepare.convert.CassandraConverter;
import nivance.jpa.cassandra.prepare.generator.AlterTableCqlGenerator;
import nivance.jpa.cassandra.prepare.generator.CreateIndexCqlGenerator;
import nivance.jpa.cassandra.prepare.generator.CreateTableCqlGenerator;
import nivance.jpa.cassandra.prepare.generator.DropIndexCqlGenerator;
import nivance.jpa.cassandra.prepare.generator.DropTableCqlGenerator;
import nivance.jpa.cassandra.prepare.mapping.CassandraPersistentEntity;
import nivance.jpa.cassandra.prepare.schema.DefaultIngestOperation;
import nivance.jpa.cassandra.prepare.schema.DefaultSchemaCqlOperations;
import nivance.jpa.cassandra.prepare.schema.DefaultSchemaUpdateOperation;
import nivance.jpa.cassandra.prepare.schema.IngestOperation;
import nivance.jpa.cassandra.prepare.schema.UpdateOperation;
import nivance.jpa.cassandra.prepare.spec.AlterTableSpecification;
import nivance.jpa.cassandra.prepare.spec.CreateIndexSpecification;
import nivance.jpa.cassandra.prepare.spec.CreateTableSpecification;
import nivance.jpa.cassandra.prepare.spec.DefaultTableDescriptor;
import nivance.jpa.cassandra.prepare.spec.DropIndexSpecification;
import nivance.jpa.cassandra.prepare.spec.DropTableSpecification;
import nivance.jpa.cassandra.prepare.spec.WithNameSpecification;

import org.springframework.data.mapping.model.MappingException;
import org.springframework.util.Assert;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterators;

/**
 * SchemaOperations implementation
 * 
 * @author Alex Shvid
 * 
 */
public class DefaultSchemaOperations implements SchemaOperations {

	private Session session;
	private String keyspace;
	private CassandraConverter converter;

	protected DefaultSchemaOperations(Session session, String keyspace, CassandraConverter converter) {
		Assert.notNull(session);
		this.session = session;
		this.keyspace = keyspace;
		this.converter = converter;
	}

	@Override
	public UpdateOperation createTable(String tableName, Class<?> entityClass) {
		Assert.notNull(entityClass);
		final CassandraPersistentEntity<?> entity = this.getPersistentEntity(entityClass);
		CreateTableSpecification spec = converter.getCreateTableSpecification(entity);
		spec.name(tableName);
		DefaultTableDescriptor defaultTableDescriptor = new DefaultTableDescriptor();
		spec.with(defaultTableDescriptor.getOptions());//add compression
		CreateTableCqlGenerator generator = new CreateTableCqlGenerator(spec);
		String cql = generator.toCql();
		return new DefaultSchemaUpdateOperation(session, cql);

	}

	@Override
	public Optional<UpdateOperation> alterTable(String tableName, Class<?> entityClass,
			boolean dropRemovedAttributeColumns) {

		Assert.notNull(entityClass);

		String cql = alterTableCql(tableName, entityClass, dropRemovedAttributeColumns);
		if (cql != null) {
			return Optional.<UpdateOperation> of(new DefaultSchemaUpdateOperation(session, cql));
		} else {
			return Optional.absent();
		}
	}

	@Override
	public String validateTable(String tableName, Class<?> entityClass) {

		Assert.notNull(entityClass);

		return alterTableCql(tableName, entityClass, true);

	}

	/**
	 * Service method to generate cql query for the given table
	 * 
	 * @param tableName
	 * @param entityClass
	 * @param dropRemovedAttributeColumns
	 * @return Cql query string or null if no changes
	 */
	protected String alterTableCql(String tableName, Class<?> entityClass, boolean dropRemovedAttributeColumns) {
		final CassandraPersistentEntity<?> entity = this.getPersistentEntity(entityClass);
		TableMetadata tableMetadata = new DefaultSchemaCqlOperations(session, keyspace).getTableMetadata(tableName);
		AlterTableSpecification spec = converter.getAlterTableSpecification(entity, tableMetadata,
				dropRemovedAttributeColumns);
		if (!spec.hasChanges()) {
			return null;
		}

		AlterTableCqlGenerator generator = new AlterTableCqlGenerator(spec);

		String cql = generator.toCql();

		return cql;

	}

	@Override
	public UpdateOperation dropTable(String tableName) {
		DropTableSpecification spec = new DropTableSpecification().name(tableName);
		String cql = new DropTableCqlGenerator(spec).toCql();
		return new DefaultSchemaUpdateOperation(session, cql);

	}

	@Override
	public IngestOperation createIndexes(String tableName, Class<?> entityClass) {
		Assert.notNull(entityClass);
		CassandraPersistentEntity<?> entity = this.getPersistentEntity(entityClass);
		List<CreateIndexSpecification> specList = converter.getCreateIndexSpecifications(entity);
		Iterator<Statement> queryIterator = Iterators.transform(specList.iterator(),
				new Function<CreateIndexSpecification, Statement>() {
					@Override
					public Statement apply(CreateIndexSpecification spec) {
						String cql = new CreateIndexCqlGenerator(spec).toCql();
						return new SimpleStatement(cql);
					}
				});
		return new DefaultIngestOperation(session, queryIterator);

	}

	@Override
	public IngestOperation alterIndexes(String tableName, Class<?> entityClass) {

		Assert.notNull(entityClass);

		List<String> cqlList = alterIndexesCql(tableName, entityClass);

		Iterator<Statement> queryIterator = Iterators.transform(cqlList.iterator(), new Function<String, Statement>() {

			@Override
			public Statement apply(String cql) {
				return new SimpleStatement(cql);
			}

		});
		return new DefaultIngestOperation(session, queryIterator);

	}

	@Override
	public List<String> validateIndexes(String tableName, Class<?> entityClass) {
		Assert.notNull(entityClass);
		return alterIndexesCql(tableName, entityClass);
	}

	/**
	 * Service method to generate cql queries to alter indexes in table
	 * 
	 * @param tableName
	 * @param entityClass
	 * @return List of cql queries
	 */

	protected List<String> alterIndexesCql(String tableName, Class<?> entityClass) {
		CassandraPersistentEntity<?> entity = this.getPersistentEntity(entityClass);
		TableMetadata tableMetadata = new DefaultSchemaCqlOperations(session, keyspace).getTableMetadata(tableName);
		List<WithNameSpecification<?>> specList = converter.getIndexChangeSpecifications(entity,
				tableMetadata);
		if (specList.isEmpty()) {
			return Collections.emptyList();
		}
		List<String> result = new ArrayList<String>(specList.size());

		for (WithNameSpecification<?> spec : specList) {

			if (spec instanceof CreateIndexSpecification) {
				result.add(new CreateIndexCqlGenerator((CreateIndexSpecification) spec).toCql());
			} else if (spec instanceof DropIndexSpecification) {
				result.add(new DropIndexCqlGenerator((DropIndexSpecification) spec).toCql());
			} else {
				throw new MappingException("unexpected index operation " + spec + " for " + entityClass);
			}
		}
		return result;
	}
	
	protected CassandraPersistentEntity<?> getPersistentEntity(Class<?> entityClass) {
		if (entityClass == null) {
			throw new IllegalArgumentException("No class parameter provided, entity table name can't be determined!");
		}
		//TODO
		CassandraPersistentEntity<?> entity = converter.getMappingContext().getPersistentEntity(entityClass);
		if (entity == null) {
			throw new MappingException("persistent entity not found for a given class " + entityClass);
		}
		return entity;
	}

}
