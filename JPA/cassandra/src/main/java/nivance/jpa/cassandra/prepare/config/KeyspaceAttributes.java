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
package nivance.jpa.cassandra.prepare.config;


/**
 * Keyspace attributes are used to create/validate or drop keyspace on startup.
 * 
 * @author Alex Shvid
 */
public class KeyspaceAttributes {

	public static final String DEFAULT_REPLICATION_STRATEGY = "SimpleStrategy";//NetworkTopologyStrategy
	public static final String DEFAULT_REPLICATION_FACTOR = "1";//dc1:1,dc2:1
	public static final boolean DEFAULT_DURABLE_WRITES = true;

	private SchemaAction action = SchemaAction.UPDATE;
	private String replicationStrategy = DEFAULT_REPLICATION_STRATEGY;
	private String replicationFactor = DEFAULT_REPLICATION_FACTOR;
	private boolean durableWrites = DEFAULT_DURABLE_WRITES;

	public SchemaAction getAction() {
		return action;
	}

	public void setAction(SchemaAction action) {
		this.action = action;
	}

	public void setActionStr(String action) {
		this.action = SchemaAction.valueOf(action.toUpperCase());
	}

	public boolean isValidate() {
		return action == SchemaAction.VALIDATE;
	}

	public boolean isUpdate() {
		return action == SchemaAction.UPDATE;
	}

	public boolean isCreate() {
		return action == SchemaAction.CREATE;
	}

	public boolean isCreateDrop() {
		return action == SchemaAction.CREATE_DROP;
	}

	public String getReplicationStrategy() {
		return replicationStrategy;
	}

	public void setReplicationStrategy(String replicationStrategy) {
		this.replicationStrategy = replicationStrategy;
	}

	public String getReplicationFactor() {
		return replicationFactor;
	}

	public void setReplicationFactor(String replicationFactor) {
		this.replicationFactor = replicationFactor;
	}

	public boolean isDurableWrites() {
		return durableWrites;
	}

	public void setDurableWrites(boolean durableWrites) {
		this.durableWrites = durableWrites;
	}

}
