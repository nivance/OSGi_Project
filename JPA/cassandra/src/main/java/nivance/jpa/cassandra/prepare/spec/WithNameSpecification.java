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
package nivance.jpa.cassandra.prepare.spec;

import static nivance.jpa.cassandra.prepare.util.CqlStringUtils.checkIdentifier;
import static nivance.jpa.cassandra.prepare.util.CqlStringUtils.identifize;

/**
 * Abstract builder class to support the construction of named entities specifications.
 * 
 * @author Matthew T. Adams
 * @author Alex Shvid
 * @param <T> The subtype of the {@link WithNameSpecification}
 */
public abstract class WithNameSpecification<T extends WithNameSpecification<T>> {

	/**
	 * The name of the entity.
	 */
	private String name;

	/**
	 * Sets the entity name.
	 * 
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T name(String name) {
		checkIdentifier(name);
		this.name = name;
		return (T) this;
	}

	/**
	 * Sets the optional entity name.
	 * 
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T optionalName(String name) {
		if (name != null) {
			checkIdentifier(name);
		}
		this.name = name;
		return (T) this;
	}

	public String getName() {
		return name;
	}

	public String getNameAsIdentifier() {
		return name != null ? identifize(name) : "";
	}
}
