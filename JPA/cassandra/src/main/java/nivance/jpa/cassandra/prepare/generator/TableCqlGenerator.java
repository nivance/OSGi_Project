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

import nivance.jpa.cassandra.prepare.option.TableOption;
import nivance.jpa.cassandra.prepare.spec.TableSpecification;

/**
 * Base class that contains behavior common to CQL generation for table operations.
 * 
 * @author Matthew T. Adams
 * @param T The subtype of this class for which this is a CQL generator.
 */
public abstract class TableCqlGenerator<T extends TableSpecification<T>> extends
		WithOptionsCqlGenerator<TableOption, T> {

	public TableCqlGenerator(TableSpecification<T> specification) {
		super(specification);
	}

	protected T spec() {
		return (T) getSpecification();
	}

}
