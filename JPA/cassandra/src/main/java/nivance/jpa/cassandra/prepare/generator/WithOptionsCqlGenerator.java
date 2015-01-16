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

import static nivance.jpa.cassandra.prepare.util.CqlStringUtils.noNull;
import static nivance.jpa.cassandra.prepare.util.CqlStringUtils.singleQuote;

import java.util.Map;

import nivance.jpa.cassandra.prepare.option.Option;
import nivance.jpa.cassandra.prepare.spec.WithOptionsSpecification;

/**
 * Abstract class for entities that have options.
 * 
 * @author Alex Shvid
 * @author Matthew T. Adams
 * @param T The subtype of this class for which this is a CQL generator.
 */
public abstract class WithOptionsCqlGenerator<O extends Option, T extends WithOptionsSpecification<O, T>> extends
		WithNameCqlGenerator<T> {

	public WithOptionsCqlGenerator(WithOptionsSpecification<O, T> specification) {
		super(specification);
	}

	protected T spec() {
		return (T) getSpecification();
	}

	@SuppressWarnings("unchecked")
	protected StringBuilder optionsCql(StringBuilder cql, StringBuilder ordering) {
		cql = noNull(cql);

		Map<String, Object> options = spec().getOptions();
		if (options.isEmpty() && ordering == null) {
			return cql;
		}

		cql.append(" WITH ");
		boolean first = true;

		if (ordering != null) {
			cql.append(ordering);
			first = false;
		}

		for (String key : options.keySet()) {
			if (first) {
				first = false;
			} else {
				cql.append(" AND ");
			}

			cql.append(key);

			Object value = options.get(key);
			if (value == null) {
				continue;
			}
			cql.append(" = ");

			if (value instanceof Map) {
				optionValueMap((Map<String, Object>) value, cql);
			} else {
				cql.append(value.toString());
			}
		}
		return cql;
	}

	protected StringBuilder optionValueMap(Map<String, Object> valueMap, StringBuilder cql) {
		cql = noNull(cql);

		if (valueMap == null || valueMap.isEmpty()) {
			return cql;
		}
		// else option value is a non-empty map

		// append { 'name' : 'value', ... }
		cql.append("{ ");
		boolean mapFirst = true;
		for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
			if (mapFirst) {
				mapFirst = false;
			} else {
				cql.append(", ");
			}

			String option = entry.getKey();
			cql.append(singleQuote(option)); // entries in map keys are always quoted
			cql.append(" : ");
			Object entryValue = entry.getValue();
			entryValue = entryValue == null ? "" : entryValue.toString();
			cql.append(entryValue);
		}
		cql.append(" }");

		return cql;
	}
}
