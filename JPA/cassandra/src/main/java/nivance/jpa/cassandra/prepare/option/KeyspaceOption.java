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
package nivance.jpa.cassandra.prepare.option;

import java.util.Map;


/**
 * Enumeration that represents all known keyspace options. value.
 * 
 * Implements {@link Option} via delegation, since {@link Enum}s can't extend anything.
 * 
 * @author Alex Shvid
 * @see ReplicationOption
 */
public enum KeyspaceOption implements Option {

	/**
	 * <code>comment</code>
	 */
	REPLICATION("replication", Map.class, true, false, false),//delete 2014.02.19
//	CLASS("class", String.class, true, false, false),//add 2014.02.19
//	
//	REPLICATION_FACTOR("replication_factor", String.class, true, false, false),//add 2014.02.19

	DURABLE_WRITES("durable_writes", Boolean.class, false, false, false);

	private Option delegate;

	private KeyspaceOption(String name, Class<?> type, boolean requiresValue, boolean escapesValue, boolean quotesValue) {
		this.delegate = new DefaultOption(name, type, requiresValue, escapesValue, quotesValue);
	}

	public Class<?> getType() {
		return delegate.getType();
	}

	public boolean takesValue() {
		return delegate.takesValue();
	}

	public String getName() {
		return delegate.getName();
	}

	public boolean escapesValue() {
		return delegate.escapesValue();
	}

	public boolean quotesValue() {
		return delegate.quotesValue();
	}

	public boolean requiresValue() {
		return delegate.requiresValue();
	}

	public void checkValue(Object value) {
		delegate.checkValue(value);
	}

	public boolean isCoerceable(Object value) {
		return delegate.isCoerceable(value);
	}

	public String toString() {
		return delegate.toString();
	}

	public String toString(Object value) {
		return delegate.toString(value);
	}

	/**
	 * Known replication options.
	 * 
	 * @author Alex Shvid
	 */
	public enum ReplicationOption implements Option {
		/**
		 * <code>class</code>
		 */
		CLASS("class", String.class, true, false, true),

		/**
		 * <code>replication_factor</code>
		 */
		REPLICATION_FACTOR("replication_factor", String.class, true, false, false);

		private Option delegate;

		private ReplicationOption(String name, Class<?> type, boolean requiresValue, boolean escapesValue,
				boolean quotesValue) {
			this.delegate = new DefaultOption(name, type, requiresValue, escapesValue, quotesValue);
		}

		public Class<?> getType() {
			return delegate.getType();
		}

		public boolean takesValue() {
			return delegate.takesValue();
		}

		public String getName() {
			return delegate.getName();
		}

		public boolean escapesValue() {
			return delegate.escapesValue();
		}

		public boolean quotesValue() {
			return delegate.quotesValue();
		}

		public boolean requiresValue() {
			return delegate.requiresValue();
		}

		public void checkValue(Object value) {
			delegate.checkValue(value);
		}

		public boolean isCoerceable(Object value) {
			return delegate.isCoerceable(value);
		}

		public String toString() {
			return delegate.toString();
		}

		public String toString(Object value) {
			return delegate.toString(value);
		}

	}
}
