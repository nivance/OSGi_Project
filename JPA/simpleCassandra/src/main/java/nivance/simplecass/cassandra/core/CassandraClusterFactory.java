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
package nivance.simplecass.cassandra.core;

import javax.annotation.PreDestroy;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nivance.simplecass.cassandra.exception.KeyspaceCreateException;
import nivance.simplecass.cassandra.mapping.KeyspaceStatement;

import org.springframework.util.StringUtils;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ProtocolOptions.Compression;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.Policies;
import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.datastax.driver.core.policies.RetryPolicy;

@Slf4j
public class CassandraClusterFactory {
	public static final String DEFAULT_CONTACT_POINTS = "localhost";
	public static final int DEFAULT_PORT = 9042;
	public static final boolean DEFAULT_METRICS_ENABLED = true;

	public Cluster cluster;
	
	private @Setter String contactPoints = DEFAULT_CONTACT_POINTS;
	private @Setter int port = DEFAULT_PORT;

	boolean metricsEnabled;

	public void initCluster() {
		Cluster.Builder builder = Cluster.builder();

		builder.addContactPoints(StringUtils.commaDelimitedListToStringArray(contactPoints)).withPort(port);

		builder.withCompression(Compression.NONE);

		{
			PoolingOptions options = new PoolingOptions();
			options.setCoreConnectionsPerHost(HostDistance.LOCAL, 100);
			options.setMaxConnectionsPerHost(HostDistance.LOCAL, 200);
			options.setMaxSimultaneousRequestsPerConnectionThreshold(HostDistance.LOCAL, 200);
			options.setMinSimultaneousRequestsPerConnectionThreshold(HostDistance.LOCAL, 50);

			options.setCoreConnectionsPerHost(HostDistance.REMOTE, 50);
			options.setMaxConnectionsPerHost(HostDistance.REMOTE, 100);
			options.setMaxSimultaneousRequestsPerConnectionThreshold(HostDistance.LOCAL, 200);
			options.setMinSimultaneousRequestsPerConnectionThreshold(HostDistance.LOCAL, 50);
			builder.withPoolingOptions(options);

		}
		{
			SocketOptions options = new SocketOptions();
			options.setConnectTimeoutMillis(20 * 1000);
			options.setKeepAlive(true);
			options.setTcpNoDelay(true);
			options.setReadTimeoutMillis(20 * 1000);
			options.setReceiveBufferSize(8192);
			options.setSendBufferSize(8192);
			options.setReuseAddress(true);
			builder.withSocketOptions(options);
		}

		LoadBalancingPolicy loadBalancingPolicy = Policies.defaultLoadBalancingPolicy();
		ReconnectionPolicy reconnectionPolicy = new ConstantReconnectionPolicy(1000);
		RetryPolicy retryPolicy = Policies.defaultRetryPolicy();

		builder.withLoadBalancingPolicy(loadBalancingPolicy);
		builder.withReconnectionPolicy(reconnectionPolicy);
		builder.withRetryPolicy(retryPolicy);
		if (!metricsEnabled) {
			builder.withoutMetrics();
		}

		Cluster cluster = null;
		try {
			cluster = builder.build();
		} catch (RuntimeException ex) {
			throw ex;
		}

		// initialize property
		this.cluster = cluster;
	}

	public Session openKeyspace(String keyspace,String strategy, String replica) {
		Session session = cluster.connect();
		KeyspaceMetadata km = cluster.getMetadata().getKeyspace(keyspace);
		if (km == null) {
			log.info("create keyspace=" + keyspace + ",replica=" + replica);
			ResultSet rs = session.execute(KeyspaceStatement.getCreateKeySpace(keyspace,strategy, replica));
			if (rs == null || rs.one() != null && rs.one().getBool(0) == false) {
				throw new KeyspaceCreateException("error in creating keyspace, no return");
			}
		}
		session.execute("USE " + keyspace + ";");
		return session;
	}
	
	@PreDestroy
	public void destroy() {
		cluster.close();
	}
	

}
