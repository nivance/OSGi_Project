/*
 * Copyright 2014 the original author or authors.
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
package nivance.jpa.cassandra.prepare.config.xml;

import nivance.jpa.cassandra.prepare.config.ConfigConstants;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Namespace handler for Cassandra.
 * 
 * @author Alex Shvid
 */

public class CassandraNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser(ConfigConstants.CASSANDRA_CLUSTER_ELEMENT, new CassandraClusterParser());
		registerBeanDefinitionParser(ConfigConstants.CASSANDRA_SESSION_ELEMENT, new CassandraSessionParser());
		registerBeanDefinitionParser(ConfigConstants.CASSANDRA_MAPPING_CONVERTER_ELEMENT,
				new CassandraMappingConverterParser());
	}

}
