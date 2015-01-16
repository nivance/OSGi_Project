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
package nivance.jpa.cassandra.prepare.schema;

import java.util.List;
import java.util.Map;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;

/**
 * Operations for interacting with Cassandra at the lowest level. This interface provides Exception Translation.
 * 
 * @author Alex Shvid
 * @author David Webb
 * @author Matthew Adams
 */
public interface CqlOperations {

	/**
	 * Creates query by using QueryCreator
	 * 
	 * @param qc - QueryCreator interface
	 * @return
	 */
	Statement createQuery(StatementCreator qc);

	/**
	 * Executes the supplied CQL Query and returns nothing.
	 * 
	 * @param cql The CQL String
	 */
	ResultSet update(String cql);

	/**
	 * Executes the supplied CQL Query and returns nothing.
	 * 
	 * @param cql The CQL String
	 */
	UpdateOperation getUpdateOperation(String cql);


	/**
	 * Executes the supplied PreparedStatement with custom binder.
	 * 
	 * @param bs BoundStatement
	 */
	ResultSet update(BoundStatement bs);

	/**
	 * Executes the supplied PreparedStatement with custom binder.
	 * 
	 * @param bs BoundStatement
	 */
	UpdateOperation getUpdateOperation(BoundStatement bs);

	/**
	 * Executes the supplied CQL Query and returns nothing.
	 * 
	 * @param qc The QueryCreator
	 */
	ResultSet update(StatementCreator qc);

	/**
	 * Executes the supplied CQL Query and returns nothing.
	 * 
	 * @param qc The QueryCreator
	 */
	UpdateOperation getUpdateOperation(StatementCreator qc);

	/**
	 * Executes the supplied CQL Query batch and returns nothing.
	 * 
	 * @param sqls The CQL queries
	 */
	ResultSet batchUpdate(String[] cqls);

	/**
	 * Executes the supplied CQL Query batch and returns nothing.
	 * 
	 * @param sqls The CQL queries
	 */
	UpdateOperation getBatchUpdateOperation(String[] cqls);

	/**
	 * Executes the supplied CQL Query batch and returns nothing.
	 * 
	 * @param statements The Statements
	 */
	ResultSet batchUpdate(Iterable<Statement> statements);

	/**
	 * Executes the supplied CQL Query batch and returns nothing.
	 * 
	 * @param statements The Statements
	 */
	UpdateOperation getBatchUpdateOperation(Iterable<Statement> statements);

	/**
	 * Executes the provided CQL Query, and extracts the results with the ResultSetCallback.
	 * 
	 * @param cql The CQL Query String
	 * 
	 * @return ResultSet
	 */
	ResultSet select(String cql);

	/**
	 * Executes the provided CQL Query, and extracts the results with the ResultSetCallback.
	 * 
	 * @param bs BoundStatement
	 * 
	 * @return ResultSet
	 */
	ResultSet select(BoundStatement bs);

	/**
	 * Executes the provided CQL Query, and extracts the results with the ResultSetCallback.
	 * 
	 * @param qc The QueryCreator
	 * 
	 * @return ResultSet
	 */
	ResultSet select(StatementCreator qc);

	/**
	 * Process a ResultSet, trying to convert the first columns of the first Row to Class<T>. This is used internal to the
	 * Template for core operations, but is made available through Operations in the event you have a ResultSet to
	 * process. The ResultsSet could come from a ResultSetFuture after an asynchronous query.
	 * 
	 * @param resultSet
	 * @param elementType
	 * @param singleResult Expected single result
	 * @return
	 */
	<T> T processOneFirstColumn(ResultSet resultSet, Class<T> elementType, boolean singleResult);

	/**
	 * Process a ResultSet with <b>ONE</b> Row and convert to a Map. This is used internal to the Template for core
	 * operations, but is made available through Operations in the event you have a ResultSet to process. The ResultsSet
	 * could come from a ResultSetFuture after an asynchronous query.
	 * 
	 * @param resultSet
	 * @param singleResult Expected single result
	 * @return
	 */
	Map<String, Object> processOneAsMap(ResultSet resultSet, boolean singleResult);

	/**
	 * Process a ResultSet and convert the first column of the results to a List. This is used internal to the Template
	 * for core operations, but is made available through Operations in the event you have a ResultSet to process. The
	 * ResultsSet could come from a ResultSetFuture after an asynchronous query.
	 * 
	 * @param resultSet
	 * @param elementType
	 * @return
	 */
	<T> List<T> processFirstColumn(ResultSet resultSet, Class<T> elementType);

	/**
	 * Process a ResultSet and convert it to a List of Maps with column/value. This is used internal to the Template for
	 * core operations, but is made available through Operations in the event you have a ResultSet to process. The
	 * ResultsSet could come from a ResultSetFuture after an asynchronous query.
	 * 
	 * @param resultSet
	 * @return
	 */
	List<Map<String, Object>> processAsMap(ResultSet resultSet);

	/**
	 * Binds prepared statement
	 * 
	 * @param ps The PreparedStatement
	 * @return
	 */
	BoundStatement bind(PreparedStatement ps);

	/**
	 * Get the current Session used for operations in the implementing class.
	 * 
	 * @return The DataStax Driver Session Object
	 */
	Session getSession();

	/**
	 * This is an operation designed for high performance writes. The cql is used to create a PreparedStatement once, then
	 * all row values are bound to the single PreparedStatement and executed against the Session.
	 * 
	 * <p>
	 * This is used internally by the other ingest() methods, but can be used if you want to write your own RowIterator.
	 * The Object[] length returned by the next() implementation must match the number of bind variables in the CQL.
	 * </p>
	 * 
	 * @param asynchronously Do asynchronously or not
	 * @param ps The PreparedStatement
	 * @param rows Implementation to provide the Object[] to be bound to the CQL.
	 */
	List<ResultSet> ingest(PreparedStatement ps, Iterable<Object[]> rows);

	/**
	 * This is an operation designed for high performance writes. The cql is used to create a PreparedStatement once, then
	 * all row values are bound to the single PreparedStatement and executed against the Session.
	 * 
	 * <p>
	 * This is used internally by the other ingest() methods, but can be used if you want to write your own RowIterator.
	 * The Object[] length returned by the next() implementation must match the number of bind variables in the CQL.
	 * </p>
	 * 
	 * @param asynchronously Do asynchronously or not
	 * @param ps The PreparedStatement
	 * @param rows Implementation to provide the Object[] to be bound to the CQL.
	 */
	IngestOperation getIngestOperation(PreparedStatement ps, Iterable<Object[]> rows);

	/**
	 * This is an operation designed for high performance writes. The cql is used to create a PreparedStatement once, then
	 * all row values are bound to the single PreparedStatement and executed against the Session.
	 * 
	 * <p>
	 * The Object[] length of the nested array must match the number of bind variables in the CQL.
	 * </p>
	 * 
	 * @param asynchronously Do asynchronously or not
	 * @param ps The PreparedStatement
	 * @param rows Object array of Object array of values to bind to the CQL.
	 */
	List<ResultSet> ingest(PreparedStatement ps, Object[][] rows);

	/**
	 * This is an operation designed for high performance writes. The cql is used to create a PreparedStatement once, then
	 * all row values are bound to the single PreparedStatement and executed against the Session.
	 * 
	 * <p>
	 * The Object[] length of the nested array must match the number of bind variables in the CQL.
	 * </p>
	 * 
	 * @param asynchronously Do asynchronously or not
	 * @param ps The PreparedStatement
	 * @param rows Object array of Object array of values to bind to the CQL.
	 */
	IngestOperation getIngestOperation(PreparedStatement ps, Object[][] rows);

	/**
	 * Calculates number of rows in table
	 * 
	 * @param tableName
	 * @return
	 */
	Long countAll(String tableName);

	/**
	 * Calculates number of rows in table
	 * 
	 * @param tableName
	 * @return
	 */
//	ProcessOperation<Long> getCountAllOperation(String tableName);

	/**
	 * Delete all rows in the table
	 * 
	 * @param tableName
	 */
	ResultSet truncate(String tableName);

	/**
	 * Delete all rows in the table
	 * 
	 * @param tableName
	 */
	UpdateOperation getTruncateOperation(String tableName);

	/**
	 * Support admin operations
	 * 
	 * @return CassandraAdminOperations
	 */

	AdminCqlOperations adminOps();

	/**
	 * Support schema operations
	 * 
	 * @return CassandraSchemaOperations
	 */

	SchemaCqlOperations schemaOps();

}
