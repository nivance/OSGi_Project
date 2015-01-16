package nivance.jpa.cassandra.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import nivance.jpa.cassandra.prepare.convert.CassandraConverter;
import nivance.jpa.cassandra.prepare.core.MappingCassandraEntityInformation;
import nivance.jpa.cassandra.prepare.mapping.CassandraEntityInformation;
import nivance.jpa.cassandra.prepare.mapping.CassandraPersistentEntity;
import nivance.jpa.cassandra.prepare.schema.AbstractFindOperation;
import nivance.jpa.cassandra.prepare.schema.BatchOperation;
import nivance.jpa.cassandra.prepare.schema.BatchedStatementCreator;
import nivance.jpa.cassandra.prepare.schema.ConsistencyLevel;
import nivance.jpa.cassandra.prepare.schema.CountByExampleOperation;
import nivance.jpa.cassandra.prepare.schema.DefaultBatchOperation;
import nivance.jpa.cassandra.prepare.schema.DefaultCountOperation;
import nivance.jpa.cassandra.prepare.schema.DefaultDeleteOperation;
import nivance.jpa.cassandra.prepare.schema.DefaultExistsOperation;
import nivance.jpa.cassandra.prepare.schema.DefaultFindByExampleOperation;
import nivance.jpa.cassandra.prepare.schema.DefaultFindOneOperation;
import nivance.jpa.cassandra.prepare.schema.DefaultMultiFindOperation;
import nivance.jpa.cassandra.prepare.schema.DefaultSaveOperation;
import nivance.jpa.cassandra.prepare.schema.DefaultUpdateOperation;
import nivance.jpa.cassandra.prepare.schema.RetryPolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.google.common.base.Function;
import com.google.common.collect.Iterators;

public abstract class AbstractCassDaoImpl<T, ID extends Serializable> {
	private static Logger logger = LoggerFactory
			.getLogger(AbstractCassDaoImpl.class);

	protected Session session;
	protected CassandraConverter converter;
	protected ConsistencyLevel consistencyLevel;
	protected RetryPolicy retryPolicy;
	protected CassandraEntityInformation<T, ID> entityInformation;
	protected CassandraPersistentEntity<?> persistentEntity;

	@SuppressWarnings("unchecked")
	public AbstractCassDaoImpl(Session session, CassandraConverter converter,
			Class<T> domainClass, ConsistencyLevel consistencyLevel,
			RetryPolicy retryPolicy) {
		this.session = session;
		this.converter = converter;
		persistentEntity = converter.getMappingContext().getPersistentEntity(
				domainClass);
		entityInformation = new MappingCassandraEntityInformation<T, ID>(
				(CassandraPersistentEntity<T>) persistentEntity);
		initQueryOptions(consistencyLevel, retryPolicy);
	}

	private void initQueryOptions(ConsistencyLevel consistencyLevel,
			RetryPolicy retryPolicy) {
		if (consistencyLevel != null) {
			this.consistencyLevel = consistencyLevel;
		}
		if (retryPolicy != null) {
			this.retryPolicy = retryPolicy;
		}
	}

	/*
	 * (non-Javadoc)
	 */
	public <S extends T> Object insert(S entity) {
		logger.info("save function, value:" + entity);
		DefaultSaveOperation<T> so = new DefaultSaveOperation<T>(session,
				converter, entityInformation.getTableName(), entity, false);
		so.withConsistencyLevel(consistencyLevel).withRetryPolicy(retryPolicy);
		so.execute();
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public <S extends T> Object insert(S entity, boolean ifNotExists) {
		logger.info("save function, value:" + entity);
		DefaultSaveOperation<T> so = new DefaultSaveOperation<T>(session,
				converter, entityInformation.getTableName(), entity, ifNotExists);
		so.withConsistencyLevel(consistencyLevel).withRetryPolicy(retryPolicy);
		if(ifNotExists){
			return so.execute().one().getBool(0);
		}
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public <S extends T> Object insert(List<S> entities) {
		Assert.notNull(entities, "The given Iterable of entities not be null!");
		Assert.notEmpty(entities,
				"The given Iterable of entities not be empty!");
		Iterator<BatchedStatementCreator> creators = Iterators.transform(
				entities.iterator(),
				new Function<T, BatchedStatementCreator>() {
					@Override
					public BatchedStatementCreator apply(T entity) {
						Assert.notNull(entity);
						return new DefaultSaveOperation<T>(session, converter,
								entityInformation.getTableName(), entity, false);
					}
				});
		BatchOperation bo = new DefaultBatchOperation(session, converter,
				creators);
		bo.withConsistencyLevel(consistencyLevel).withRetryPolicy(retryPolicy);
		bo.execute();
		return entities.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public <S extends T> Object update(S entity) {
		DefaultUpdateOperation<T> so = new DefaultUpdateOperation<T>(session,
				converter, entityInformation.getTableName(), entity);
		so.withConsistencyLevel(consistencyLevel).withRetryPolicy(retryPolicy);
		so.execute();
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public <S extends T> Object update(List<S> entities) {
		Assert.notNull(entities, "The given Iterable of entities not be null!");
		Assert.notEmpty(entities,
				"The given Iterable of entities not be empty!");
		Iterator<BatchedStatementCreator> creators = Iterators.transform(
				entities.iterator(),
				new Function<T, BatchedStatementCreator>() {
					@Override
					public BatchedStatementCreator apply(T entity) {
						Assert.notNull(entity);
						return new DefaultUpdateOperation<T>(session,
								converter, entityInformation.getTableName(),
								entity);
					}
				});
		BatchOperation option = new DefaultBatchOperation(session, converter,
				creators);
		option.withConsistencyLevel(consistencyLevel);
		option.withRetryPolicy(retryPolicy);
		option.execute();
		return entities.size();
	}

	public <S extends T> T findOne(final S entity) {
		Assert.notNull(entity, "entity not be null!");
		DefaultFindOneOperation<T> option = new DefaultFindOneOperation<T>(
				session, converter, entityInformation.getJavaType(), entity);
		option.withConsistencyLevel(consistencyLevel);
		option.withRetryPolicy(retryPolicy);
		return option.execute();
	}

	public List<T> findByExample(final T entity) {
		Assert.notNull(entity, "entity not be null!");
		DefaultFindByExampleOperation<T> operation = new DefaultFindByExampleOperation<>(
				session, converter, entityInformation.getJavaType(), entity);
		operation.withConsistencyLevel(consistencyLevel);
		operation.withRetryPolicy(retryPolicy);
		return operation.execute();
	}

	public <S extends T> boolean exists(S s) {
		logger.info("exists function value:" + s);
		DefaultExistsOperation<T> operatoin = new DefaultExistsOperation<T>(
				session, converter, s);
		operatoin.withConsistencyLevel(consistencyLevel);
		operatoin.withRetryPolicy(retryPolicy);
		return operatoin.execute();
	}

	public List<T> findAll() {
		logger.info("findAll function");
		return new AbstractFindOperation<T>(session, converter,
				entityInformation.getJavaType()) {
			@Override
			public RegularStatement createStatement() {
				Select select = QueryBuilder.select().all()
						.from(getTableName());
				return select;
			}
		}.withConsistencyLevel(consistencyLevel).withRetryPolicy(retryPolicy)
				.execute();
	}

	@SuppressWarnings("unused")
	@Deprecated
	private List<T> findAll(final String query) {
		return new AbstractFindOperation<T>(session, converter,
				entityInformation.getJavaType()) {
			@Override
			public RegularStatement createStatement() {
				return new SimpleStatement(query);
			}

		}.withConsistencyLevel(consistencyLevel).withRetryPolicy(retryPolicy)
				.execute();
	}

	public <S extends T> List<T> findAll(final List<S> entities) {
		logger.info("findAll function, values:" + entities);
		return new DefaultMultiFindOperation<T>(session, converter,
				entityInformation.getJavaType(), entities)
				.withConsistencyLevel(consistencyLevel)
				.withRetryPolicy(retryPolicy).execute();
	}

	public long count() {
		logger.info("count function");
		return new DefaultCountOperation<T>(session, converter,
				entityInformation.getJavaType())
				.withConsistencyLevel(consistencyLevel)
				.withRetryPolicy(retryPolicy).execute();
	}

	public <S extends T> long count(S s) {
		logger.info("count function");
		return new CountByExampleOperation<T>(session, converter,
				entityInformation.getJavaType(), s)
				.withConsistencyLevel(consistencyLevel)
				.withRetryPolicy(retryPolicy).execute();
	}

	@Deprecated
	public Object delete(ID id) {
		Assert.notNull(id, "The given id must not be null!");
		ResultSet rs = new DefaultDeleteOperation<T>(session, converter,
				entityInformation.getJavaType(), id)
				.withConsistencyLevel(consistencyLevel)
				.withRetryPolicy(retryPolicy).execute();
		logger.debug("delete id:" + id + ", resultset:" + rs);
		return 1;
	}

	public Object delete(T entity) {
		logger.info("delete function, value:" + entity);
		Assert.notNull(entity, "The given entity must not be null!");
		ResultSet rs = new DefaultDeleteOperation<T>(session, converter, entity)
				.withConsistencyLevel(consistencyLevel)
				.withRetryPolicy(retryPolicy).execute();
		logger.debug("delete entity:" + entity + ", resultset:" + rs);
		return 1;
	}

	public Object delete(List<? extends T> entities) {
		logger.info("delete function, value:" + entities);
		Assert.notNull(entities, "The given Iterable of entities not be null!");
		Assert.notEmpty(entities,
				"The given Iterable of entities not be empty!");
		Iterator<BatchedStatementCreator> creators = Iterators.transform(
				entities.iterator(),
				new Function<T, BatchedStatementCreator>() {
					@Override
					public BatchedStatementCreator apply(T entity) {
						Assert.notNull(entity);
						return new DefaultDeleteOperation<T>(session,
								converter, entity);
					}
				});
		BatchOperation bo = new DefaultBatchOperation(session, converter,
				creators);
		bo.withConsistencyLevel(consistencyLevel).withRetryPolicy(retryPolicy);
		bo.execute();
		return entities.size();
	}

	public Object deleteAll() {
		ResultSet rs = new DefaultDeleteOperation<T>(session, converter,
				entityInformation.getJavaType())
				.withConsistencyLevel(consistencyLevel)
				.withRetryPolicy(retryPolicy).execute();
		logger.debug("deleteAll's resultset:" + rs);
		return 1;
	}

}
