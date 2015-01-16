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

import java.util.HashMap;
import java.util.Map;

import nivance.jpa.cassandra.prepare.option.TableOption;
import nivance.jpa.cassandra.prepare.option.TableOption.CompressionOption;

/**
 * Convenient default implementation of {@link TableDescriptor} as an extension
 * of {@link TableSpecification} that doesn't require the use of generics.
 * 
 * @author Matthew T. Adams
 */
public class DefaultTableDescriptor extends
		TableSpecification<DefaultTableDescriptor> {

	/**
	 * Factory method to produce a new {@link DefaultTableDescriptor}.
	 * Convenient if imported statically.
	 */
	public static DefaultTableDescriptor table() {
		return new DefaultTableDescriptor();
	}

	public Map<String, Object> getOptions() {
		// add defalut config
		Map<String, Object> compression = new HashMap<>();
		compression.put(CompressionOption.STABLE_COMPRESSION.getName(), " '' ");
		options.put(TableOption.COMPRESSION.getName(), compression);
		return options;
	}
}
