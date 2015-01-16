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

import java.util.Map;

import nivance.jpa.cassandra.prepare.config.KeyspaceAttributes;
import nivance.jpa.cassandra.prepare.option.KeyspaceOption;
import nivance.jpa.cassandra.prepare.option.KeyspaceOptions;
import nivance.jpa.cassandra.prepare.schema.AdminCqlOperations;
import nivance.jpa.cassandra.prepare.schema.DefaultAdminCqlOperations;
import nivance.jpa.cassandra.prepare.support.CassandraExceptionTranslator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.util.StringUtils;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;

/**
 * Convenient factory for configuring a Cassandra Cql Session. It is enough to have one session per application.
 * 
 * @author Alex Shvid
 */

public class SessionFactoryBean implements FactoryBean<Session>, InitializingBean, DisposableBean,
		PersistenceExceptionTranslator {

	private static final Logger log = LoggerFactory.getLogger(SessionFactoryBean.class);

	protected Session session;
	protected Cluster cluster;
	protected String keyspace;
	protected KeyspaceAttributes keyspaceAttributes;
	protected boolean keyspaceCreated = false;
	private AdminCqlOperations adminOps;
	
	protected final PersistenceExceptionTranslator exceptionTranslator = new CassandraExceptionTranslator();

	@Override
	public Session getObject() {
		return session;
	}

	@Override
	public Class<? extends Session> getObjectType() {
		return Session.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
		return exceptionTranslator.translateExceptionIfPossible(ex);
	}

	@Override
	public void afterPropertiesSet() {

		if (cluster == null) {
			throw new IllegalArgumentException("cluster is required");
		}
		Session session = null;
		try {
			session = cluster.connect();
		} catch (RuntimeException ex) {
			RuntimeException resolved = translateExceptionIfPossible(ex);
			throw resolved == null ? ex : resolved;
		}
		if (StringUtils.hasText(keyspace)) {
//			CqlTemplate cqlTemplate = new CqlTemplate(session, keyspace);
			adminOps = new DefaultAdminCqlOperations(session);

			KeyspaceMetadata keyspaceMetadata = adminOps.getKeyspaceMetadata(keyspace);
			boolean keyspaceExists = keyspaceMetadata != null;
			keyspaceCreated = false;

			if (keyspaceExists) {
				log.info("keyspace exists " + keyspaceMetadata.asCQLQuery());
			}
			if (keyspaceAttributes == null) {
				keyspaceAttributes = new KeyspaceAttributes();
			}
			// drop the old schema if needed
			if (keyspaceExists && (keyspaceAttributes.isCreate() || keyspaceAttributes.isCreateDrop())) {
				log.info("Drop keyspace " + keyspace + " on afterPropertiesSet");
				adminOps.dropKeyspace(keyspace).execute();
				keyspaceExists = false;
			}
			// create the new schema if needed
			if (!keyspaceExists
					&& (keyspaceAttributes.isCreate() || keyspaceAttributes.isCreateDrop() || keyspaceAttributes.isUpdate())) {
				log.info("Create keyspace " + keyspace + " on afterPropertiesSet");
				adminOps.createKeyspace(keyspace, createKeyspaceOptions()).execute();
				keyspaceCreated = true;
			}
			// update schema if needed
			if (keyspaceAttributes.isUpdate() && !keyspaceCreated) {
				if (compareKeyspaceAttributes(keyspaceAttributes, keyspaceMetadata) != null) {
					log.info("Update keyspace " + keyspace + " on afterPropertiesSet");
					adminOps.alterKeyspace(keyspace, createKeyspaceOptions()).execute();
				}
			}
			// validate schema if needed
			if (keyspaceAttributes.isValidate()) {
				if (!keyspaceExists) {
					throw new InvalidDataAccessApiUsageException("keyspace '" + keyspace + "' not found in the Cassandra");
				}
				String errorField = compareKeyspaceAttributes(keyspaceAttributes, keyspaceMetadata);
				if (errorField != null) {
					throw new InvalidDataAccessApiUsageException(errorField + " attribute is not much in the keyspace '"
							+ keyspace + "'");
				}
			}
			adminOps.useKeyspace(keyspace).execute();
		}
		// initialize property
		this.session = session;
	}

	@Override
	public void destroy() throws Exception {
		if (StringUtils.hasText(keyspace) && keyspaceAttributes != null && keyspaceAttributes.isCreateDrop()) {
			log.info("Drop keyspace " + keyspace + " on destroy");
			adminOps.useSystemKeyspace().execute();
			adminOps.dropKeyspace(keyspace).execute();

		}
		this.session.close();
	}

	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public void setKeyspaceAttributes(KeyspaceAttributes attributes) {
		this.keyspaceAttributes = attributes;
	}

	private KeyspaceOptions createKeyspaceOptions() {
		KeyspaceOptions keyspaceOptions = new KeyspaceOptions()
				.with(KeyspaceOption.ReplicationOption.CLASS.getName(), keyspaceAttributes.getReplicationStrategy(), false, false) // add at 2014.02.19
				.with(KeyspaceOption.ReplicationOption.REPLICATION_FACTOR.getName(), keyspaceAttributes.getReplicationFactor() , false, false)// add at 2014.02.19
				//.with(KeyspaceOption.REPLICATION, keyspaceReplicationOptions) // delete at 2014.02.19
				.with(KeyspaceOption.DURABLE_WRITES, keyspaceAttributes.isDurableWrites());
		return keyspaceOptions;
	}

	private static String compareKeyspaceAttributes(KeyspaceAttributes keyspaceAttributes,
			KeyspaceMetadata keyspaceMetadata) {
		if (keyspaceAttributes.isDurableWrites() != keyspaceMetadata.isDurableWrites()) {
			return "durableWrites";
		}
		Map<String, String> replication = keyspaceMetadata.getReplication();
		String replicationFactorStr = replication.get("replication_factor");
		if (replicationFactorStr == null) {
			return "replication_factor";
		}
//		try {
//			int replicationFactor = Integer.parseInt(replicationFactorStr);
//			if (keyspaceAttributes.getReplicationFactor() != replicationFactor) {
			if (!keyspaceAttributes.getReplicationFactor().equals(replicationFactorStr)) {
				return "replication_factor";
			}
//		} catch (NumberFormatException e) {
//			return "replication_factor";
//		}

		String attributesStrategy = keyspaceAttributes.getReplicationStrategy();
		if (attributesStrategy.indexOf('.') == -1) {
			attributesStrategy = "org.apache.cassandra.locator." + attributesStrategy;
		}
		String replicationStrategy = replication.get("class");
		if (!attributesStrategy.equals(replicationStrategy)) {
			return "replication_class";
		}
		return null;
	}

}
