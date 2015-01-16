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

import org.springframework.dao.QueryTimeoutException;

/**
 * Spring data access exception for a Cassandra write timeout.
 * 
 * @author Matthew T. Adams
 */
public class CassandraWriteTimeoutException extends QueryTimeoutException {

	private static final long serialVersionUID = -4374826375213670718L;

	private String writeType;

	public CassandraWriteTimeoutException(String writeType, String msg, Throwable cause) {
		super(msg, cause);
		this.writeType = writeType;
	}

	public String getWriteType() {
		return writeType;
	}
}
