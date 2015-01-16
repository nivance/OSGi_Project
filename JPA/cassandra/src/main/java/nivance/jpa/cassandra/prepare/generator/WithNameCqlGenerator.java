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
package nivance.jpa.cassandra.prepare.generator;

import nivance.jpa.cassandra.prepare.spec.WithNameSpecification;

import org.springframework.util.Assert;

public abstract class WithNameCqlGenerator<T extends WithNameSpecification<T>> {

	public abstract StringBuilder toCql(StringBuilder cql);

	private WithNameSpecification<T> specification;

	public WithNameCqlGenerator(WithNameSpecification<T> specification) {
		setSpecification(specification);
	}

	protected void setSpecification(WithNameSpecification<T> specification) {
		Assert.notNull(specification);
		this.specification = specification;
	}

	@SuppressWarnings("unchecked")
	public T getSpecification() {
		return (T) specification;
	}

	/**
	 * Convenient synonymous method of {@link #getSpecification()}.
	 */
	protected T spec() {
		return getSpecification();
	}

	public String toCql() {
		return toCql(null).toString();
	}
}
