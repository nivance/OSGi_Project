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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import nivance.jpa.cassandra.prepare.option.Option;
import nivance.jpa.cassandra.prepare.option.OptionsCreator;
import nivance.jpa.cassandra.prepare.option.TableOption;
import nivance.jpa.cassandra.prepare.util.CqlStringUtils;

/**
 * Abstract options specification based on Map<String, Object>.
 * 
 * @author Alex Shvid
 * @author Matthew T. Adams
 * 
 */
public class WithOptionsSpecification<O extends Option, T extends WithOptionsSpecification<O, T>> extends
		WithNameSpecification<T> {

	protected Map<String, Object> options = new LinkedHashMap<String, Object>();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private OptionsCreator<O, ?> creator = new OptionsCreator(options);

	public T name(String name) {
		return (T) super.name(name);
	}

	/**
	 * Expected right quotation for all options
	 * 
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T with(Map<String, Object> optionsByName) {
		options.putAll(optionsByName);
		return (T) this;
	}

	/**
	 * Convenience method that calls <code>with(option, null)</code>.
	 * 
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T with(O option) {
		creator.with(option);
		return (T) this;
	}

	/**
	 * Sets the given option. This is a convenience method that calls {@link #with(String, Object, boolean, boolean)}
	 * appropriately from the given {@link TableOption} and value for that option.
	 * 
	 * @param option The option to set.
	 * @param value The value of the option. Must be type-compatible with the {@link TableOption}.
	 * @return this
	 * @see #with(String, Object, boolean, boolean)
	 */
	@SuppressWarnings("unchecked")
	public T with(O option, Object value) {
		creator.with(option, value);
		return (T) this;
	}

	/**
	 * Adds the given option by name to this table's options.
	 * <p/>
	 * Options that have <code>null</code> values are considered single string options where the name of the option is the
	 * string to be used. Otherwise, the result of {@link Object#toString()} is considered to be the value of the option
	 * with the given name. The value, after conversion to string, may have embedded single quotes escaped according to
	 * parameter <code>escape</code> and may be single-quoted according to parameter <code>quote</code>.
	 * 
	 * @param name The name of the option
	 * @param value The value of the option. If <code>null</code>, the value is ignored and the option is considered to be
	 *          composed of only the name, otherwise the value's {@link Object#toString()} value is used.
	 * @param escape Whether to escape the value via {@link CqlStringUtils#escapeSingle(Object)}. Ignored if given value
	 *          is an instance of a {@link Map}.
	 * @param quote Whether to quote the value via {@link CqlStringUtils#singleQuote(Object)}. Ignored if given value is
	 *          an instance of a {@link Map}.
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T with(String name, Object value, boolean escape, boolean quote) {
		creator.with(name, value, escape, quote);
		return (T) this;
	}

	public Map<String, Object> getOptions() {
		return Collections.unmodifiableMap(options);
	}

}
