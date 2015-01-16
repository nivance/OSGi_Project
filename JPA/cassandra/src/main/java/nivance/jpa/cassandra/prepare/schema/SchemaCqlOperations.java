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
package nivance.jpa.cassandra.prepare.schema;

import java.util.List;

import nivance.jpa.cassandra.prepare.option.TableOptions;
import nivance.jpa.cassandra.prepare.spec.AlterTableSpecification;
import nivance.jpa.cassandra.prepare.spec.CreateIndexSpecification;
import nivance.jpa.cassandra.prepare.spec.CreateTableSpecification;
import nivance.jpa.cassandra.prepare.spec.DropIndexSpecification;
import nivance.jpa.cassandra.prepare.spec.DropTableSpecification;

import com.datastax.driver.core.TableMetadata;

/**
 * Cassandra Schema Operations interface
 * 
 * TODO:
 * 
 * Add createTable, alterTable, dropIndex methods
 * 
 * @author Alex Shvid
 * 
 */
public interface SchemaCqlOperations {

	/**
	 * Get the given table's metadata.
	 * 
	 * @param tableName The name of the table.
	 */
	TableMetadata getTableMetadata(String tableName);

	/**
	 * Creates table by using CreateTableSpecification
	 * 
	 * @param createTableSpecification
	 * @return
	 */
	UpdateOperation createTable(CreateTableSpecification createTableSpecification);

	/**
	 * Creates table by using parameters of the function
	 * 
	 * @param ifNotExists
	 * @param tableName
	 * @param partitionedColumns
	 * @param clusteringColumns
	 * @param nonKeyColumns
	 * @param tableOptions
	 * @return
	 */
	UpdateOperation createTable(boolean ifNotExists, String tableName, List<CqlColumn> partitionedColumns,
			List<ClusteringCqlColumn> clusteringColumns, List<CqlColumn> nonKeyColumns, TableOptions tableOptions);

	/**
	 * Alters table by using parameters
	 * 
	 * @param tableName Table name to alter
	 * @param addColumns Adding columns to the table or empty Map
	 * @param alterColumns Alter types of the column in the table or empty Map
	 * @param dropColumns Dropping columns in the table or empty Set
	 * @param tableOptions
	 * @return
	 */
	UpdateOperation alterTable(String tableName, List<CqlColumn> addColumns, List<CqlColumn> alterColumns,
			List<String> dropColumns, TableOptions tableOptions);

	/**
	 * Alters table by using AlterTableSpecification
	 * 
	 * @param alterTableSpecification
	 * @return
	 */
	UpdateOperation alterTable(AlterTableSpecification alterTableSpecification);

	/**
	 * Drops table by using tableName
	 * 
	 * @param ifExists
	 * @param tableName
	 * @return
	 */
	UpdateOperation dropTable(boolean ifExists, String tableName);

	/**
	 * Drops table by using AlterTableSpecification
	 * 
	 * @param dropTableSpecification
	 * @return
	 */
	UpdateOperation dropTable(DropTableSpecification dropTableSpecification);

	/**
	 * Creates index by using index name
	 * 
	 * @param createIndexSpecification
	 * @return
	 */
	UpdateOperation createIndex(String indexName);

	/**
	 * Creates index by using default index name for table.column
	 * 
	 * @param createIndexSpecification
	 * @return
	 */
	UpdateOperation createIndex(String tableName, String columnName);

	/**
	 * Creates index by using CreateIndexSpecification
	 * 
	 * @param createIndexSpecification
	 * @return
	 */
	UpdateOperation createIndex(CreateIndexSpecification createIndexSpecification);

	/**
	 * Drops index by using index name
	 * 
	 * @param dropIndexSpecification
	 * @return
	 */
	UpdateOperation dropIndex(String indexName);

	/**
	 * Drops index by using default index name for table.column
	 * 
	 * @param dropIndexSpecification
	 * @return
	 */
	UpdateOperation dropIndex(String tableName, String columnName);

	/**
	 * Drops index by using DropIndexSpecification
	 * 
	 * @param dropIndexSpecification
	 * @return
	 */
	UpdateOperation dropIndex(DropIndexSpecification dropIndexSpecification);

}
