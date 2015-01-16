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

package nivance.jpa.cassandra.prepare.support.exception;

import java.net.InetAddress;

import org.springframework.dao.PermissionDeniedDataAccessException;

/**
 * Spring data access exception for a Cassandra authentication failure.
 * 
 * @author Matthew T. Adams
 */
public class CassandraAuthenticationException extends PermissionDeniedDataAccessException {

	private static final long serialVersionUID = 8556304586797273927L;

	private InetAddress host;

	public CassandraAuthenticationException(InetAddress host, String msg, Throwable cause) {
		super(msg, cause);
		this.host = host;
	}

	public InetAddress getHost() {
		return host;
	}
}
