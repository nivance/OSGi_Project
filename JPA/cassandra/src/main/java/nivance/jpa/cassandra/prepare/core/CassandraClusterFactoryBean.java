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
package nivance.jpa.cassandra.prepare.core;

import nivance.jpa.cassandra.prepare.config.CompressionType;
import nivance.jpa.cassandra.prepare.config.PoolingOptions;
import nivance.jpa.cassandra.prepare.config.SocketOptions;
import nivance.jpa.cassandra.prepare.support.CassandraExceptionTranslator;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.util.StringUtils;

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.ProtocolOptions.Compression;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.datastax.driver.core.policies.RetryPolicy;

/**
 * Convenient factory for configuring a Cassandra Cluster.
 * 
 * @author Alex Shvid
 */

public class CassandraClusterFactoryBean implements FactoryBean<Cluster>, InitializingBean, DisposableBean,
		PersistenceExceptionTranslator {

	public static final String DEFAULT_CONTACT_POINTS = "localhost";
	public static final int DEFAULT_PORT = 9042;
	public static final boolean DEFAULT_METRICS_ENABLED = true;

	private Cluster cluster;

	private String contactPoints = DEFAULT_CONTACT_POINTS;
	private int port = DEFAULT_PORT;
	private CompressionType compressionType;

	private PoolingOptions localPoolingOptions;
	private PoolingOptions remotePoolingOptions;
	private SocketOptions socketOptions;

	private AuthProvider authProvider;
	private LoadBalancingPolicy loadBalancingPolicy;
	private ReconnectionPolicy reconnectionPolicy;
	private RetryPolicy retryPolicy;

	private boolean metricsEnabled = DEFAULT_METRICS_ENABLED;

	private final PersistenceExceptionTranslator exceptionTranslator = new CassandraExceptionTranslator();

	@Override
	public Cluster getObject() throws Exception {
		return cluster;
	}

	@Override
	public Class<? extends Cluster> getObjectType() {
		return Cluster.class;
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

		Cluster.Builder builder = Cluster.builder();

		builder.addContactPoints(StringUtils.commaDelimitedListToStringArray(contactPoints)).withPort(port);

		if (compressionType != null) {
			builder.withCompression(convertCompressionType(compressionType));
		}

		if(localPoolingOptions != null || remotePoolingOptions!=null){
			builder.withPoolingOptions(getPoolingOptions());
		}
		
		if (socketOptions != null) {
			builder.withSocketOptions(configSocketOptions(socketOptions));
		}

		if (authProvider != null) {
			builder.withAuthProvider(authProvider);
		}

		if (loadBalancingPolicy != null) {
			builder.withLoadBalancingPolicy(loadBalancingPolicy);
		}

		if (reconnectionPolicy != null) {
			builder.withReconnectionPolicy(reconnectionPolicy);
		}

		if (retryPolicy != null) {
			builder.withRetryPolicy(retryPolicy);
		}

		if (!metricsEnabled) {
			builder.withoutMetrics();
		}

		Cluster cluster = null;
		try {
			cluster = builder.build();
		} catch (RuntimeException ex) {
			RuntimeException resolved = translateExceptionIfPossible(ex);
			throw resolved == null ? ex : resolved;
		}

		// initialize property
		this.cluster = cluster;
		cluster.connect();
	}
	
	private com.datastax.driver.core.PoolingOptions getPoolingOptions(){
		com.datastax.driver.core.PoolingOptions poolingOptions = new com.datastax.driver.core.PoolingOptions();
		if (localPoolingOptions != null) {
			configPoolingOptions(poolingOptions,HostDistance.LOCAL, localPoolingOptions);
		}

		if (remotePoolingOptions != null) {
			configPoolingOptions(poolingOptions,HostDistance.REMOTE, remotePoolingOptions);
		}
		return poolingOptions;
	}

	@Override
	public void destroy() throws Exception {
		this.cluster.close();
	}

	public void setContactPoints(String contactPoints) {
		this.contactPoints = contactPoints;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setCompressionType(CompressionType compressionType) {
		this.compressionType = compressionType;
	}

	public void setLocalPoolingOptions(PoolingOptions localPoolingOptions) {
		this.localPoolingOptions = localPoolingOptions;
	}

	public void setRemotePoolingOptions(PoolingOptions remotePoolingOptions) {
		this.remotePoolingOptions = remotePoolingOptions;
	}

	public void setSocketOptions(SocketOptions socketOptions) {
		this.socketOptions = socketOptions;
	}

	public void setAuthProvider(AuthProvider authProvider) {
		this.authProvider = authProvider;
	}

	public void setLoadBalancingPolicy(LoadBalancingPolicy loadBalancingPolicy) {
		this.loadBalancingPolicy = loadBalancingPolicy;
	}

	public void setReconnectionPolicy(ReconnectionPolicy reconnectionPolicy) {
		this.reconnectionPolicy = reconnectionPolicy;
	}

	public void setRetryPolicy(RetryPolicy retryPolicy) {
		this.retryPolicy = retryPolicy;
	}

	public void setMetricsEnabled(boolean metricsEnabled) {
		this.metricsEnabled = metricsEnabled;
	}

	private static Compression convertCompressionType(CompressionType type) {
		switch (type) {
		case NONE:
			return Compression.NONE;
		case SNAPPY:
			return Compression.SNAPPY;
		}
		throw new IllegalArgumentException("unknown compression type " + type);
	}

	private static void configPoolingOptions(com.datastax.driver.core.PoolingOptions poolingOptions,HostDistance hostDistance,
			PoolingOptions config) {

		if (config.getMinSimultaneousRequests() != null) {
			poolingOptions
					.setMinSimultaneousRequestsPerConnectionThreshold(hostDistance, config.getMinSimultaneousRequests());
		}
		if (config.getMaxSimultaneousRequests() != null) {
			poolingOptions
					.setMaxSimultaneousRequestsPerConnectionThreshold(hostDistance, config.getMaxSimultaneousRequests());
		}
		if (config.getCoreConnections() != null) {
			poolingOptions.setCoreConnectionsPerHost(hostDistance, config.getCoreConnections());
		}
		if (config.getMaxConnections() != null) {
			poolingOptions.setMaxConnectionsPerHost(hostDistance, config.getMaxConnections());
		}
	}

	private static com.datastax.driver.core.SocketOptions configSocketOptions(SocketOptions config) {
		com.datastax.driver.core.SocketOptions socketOptions = new com.datastax.driver.core.SocketOptions();

		if (config.getConnectTimeoutMls() != null) {
			socketOptions.setConnectTimeoutMillis(config.getConnectTimeoutMls());
		}
		if (config.getKeepAlive() != null) {
			socketOptions.setKeepAlive(config.getKeepAlive());
		}
		if (config.getReuseAddress() != null) {
			socketOptions.setReuseAddress(config.getReuseAddress());
		}
		if (config.getSoLinger() != null) {
			socketOptions.setSoLinger(config.getSoLinger());
		}
		if (config.getTcpNoDelay() != null) {
			socketOptions.setTcpNoDelay(config.getTcpNoDelay());
		}
		if (config.getReceiveBufferSize() != null) {
			socketOptions.setReceiveBufferSize(config.getReceiveBufferSize());
		}
		if (config.getSendBufferSize() != null) {
			socketOptions.setSendBufferSize(config.getSendBufferSize());
		}

		return socketOptions;
	}
}
