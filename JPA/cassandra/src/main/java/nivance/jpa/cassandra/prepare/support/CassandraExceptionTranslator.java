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
package nivance.jpa.cassandra.prepare.support;

import nivance.jpa.cassandra.prepare.support.exception.CassandraAuthenticationException;
import nivance.jpa.cassandra.prepare.support.exception.CassandraConnectionFailureException;
import nivance.jpa.cassandra.prepare.support.exception.CassandraInsufficientReplicasAvailableException;
import nivance.jpa.cassandra.prepare.support.exception.CassandraInternalException;
import nivance.jpa.cassandra.prepare.support.exception.CassandraKeyspaceExistsException;
import nivance.jpa.cassandra.prepare.support.exception.CassandraTableExistsException;
import nivance.jpa.cassandra.prepare.support.exception.CassandraTruncateException;
import nivance.jpa.cassandra.prepare.support.exception.CassandraUnauthorizedException;
import nivance.jpa.cassandra.prepare.support.exception.CassandraUncategorizedException;
import nivance.jpa.cassandra.prepare.support.exception.CassandraWriteTimeoutException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;

import com.datastax.driver.core.WriteType;
import com.datastax.driver.core.exceptions.AlreadyExistsException;
import com.datastax.driver.core.exceptions.AuthenticationException;
import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.exceptions.DriverInternalError;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.TruncateException;
import com.datastax.driver.core.exceptions.UnauthorizedException;
import com.datastax.driver.core.exceptions.UnavailableException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;

/**
 * Simple {@link PersistenceExceptionTranslator} for Cassandra. Convert the given runtime exception to an appropriate
 * exception from the {@code org.springframework.dao} hierarchy. Return {@literal null} if no translation is
 * appropriate: any other exception may have resulted from user code, and should not be translated.
 * 
 * @author Alex Shvid
 * @author Matthew T. Adams
 */

public class CassandraExceptionTranslator implements PersistenceExceptionTranslator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.dao.support.PersistenceExceptionTranslator#
	 * translateExceptionIfPossible(java.lang.RuntimeException)
	 */
	public DataAccessException translateExceptionIfPossible(RuntimeException x) {

		if (x instanceof DataAccessException) {
			return (DataAccessException) x;
		}

		if (!(x instanceof DriverException)) {
			return null;
		}

		// Remember: subclasses must come before superclasses, otherwise the
		// superclass would match before the subclass!

		if (x instanceof AuthenticationException) {
			return new CassandraAuthenticationException(((AuthenticationException) x).getHost(), x.getMessage(), x);
		}
		if (x instanceof DriverInternalError) {
			return new CassandraInternalException(x.getMessage(), x);
		}
		if (x instanceof NoHostAvailableException) {
			return new CassandraConnectionFailureException(((NoHostAvailableException) x).getErrors(), x.getMessage(), x);
		}
		if (x instanceof WriteTimeoutException) {
			WriteType writeType = ((WriteTimeoutException) x).getWriteType();
			return new CassandraWriteTimeoutException(writeType == null ? null : writeType.name(), x.getMessage(), x);
		}
		if (x instanceof TruncateException) {
			return new CassandraTruncateException(x.getMessage(), x);
		}
		if (x instanceof UnavailableException) {
			UnavailableException ux = (UnavailableException) x;
			return new CassandraInsufficientReplicasAvailableException(ux.getRequiredReplicas(), ux.getAliveReplicas(),
					x.getMessage(), x);
		}
		if (x instanceof AlreadyExistsException) {
			AlreadyExistsException aex = (AlreadyExistsException) x;

			return aex.wasTableCreation() ? new CassandraTableExistsException(aex.getTable(), x.getMessage(), x)
					: new CassandraKeyspaceExistsException(aex.getKeyspace(), x.getMessage(), x);
		}
		if (x instanceof UnauthorizedException) {
			return new CassandraUnauthorizedException(x.getMessage(), x);
		}
		// unknown or unhandled exception
		return new CassandraUncategorizedException(x.getMessage(), x);
	}
}
