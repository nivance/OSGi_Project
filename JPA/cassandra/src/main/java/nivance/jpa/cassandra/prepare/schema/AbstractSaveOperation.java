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

import org.springframework.util.Assert;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

/**
 * Abstract save operation implementation
 * 
 * @author Alex Shvid
 * 
 * @param <T> - entity Type
 * @param <O> - operation Type
 */

public abstract class AbstractSaveOperation<T, O extends QueryOperation<ResultSet, O>> extends
	AbstractUpdateOperation<O> implements BatchedStatementCreator{

	protected final Session session;
	protected final T entity;
	private String tableName;
	private Integer ttl;
	private Long timestamp;
	private boolean ifNotExists;

	protected AbstractSaveOperation(Session session, String tableName, T entity,boolean ifNotExists) {
		super(session);
		Assert.notNull(session);
		Assert.notNull(entity);
		this.session = session;
		this.entity = entity;
		this.tableName = tableName;
		this.ifNotExists = ifNotExists;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTtl(Integer ttl) {
		this.ttl = ttl;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getTtl() {
		return ttl;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public boolean isIfNotExists() {
		return ifNotExists;
	}

	public void setIfNotExists(boolean ifNotExists) {
		this.ifNotExists = ifNotExists;
	}

	
}
