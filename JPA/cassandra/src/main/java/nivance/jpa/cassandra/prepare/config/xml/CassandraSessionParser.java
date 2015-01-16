/*
 * Copyright 2013-2014 the original author or authors.
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

import java.util.List;

import nivance.jpa.cassandra.prepare.config.ConfigConstants;
import nivance.jpa.cassandra.prepare.config.KeyspaceAttributes;
import nivance.jpa.cassandra.prepare.config.TableAttributes;
import nivance.jpa.cassandra.prepare.core.CassandraSessionFactoryBean;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.data.config.ParsingUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Parser for Cassandra Schema definitions.
 * 
 * @author Alex Shvid
 */

public class CassandraSessionParser extends AbstractSimpleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return CassandraSessionFactoryBean.class;
	}

	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
			throws BeanDefinitionStoreException {

		String id = super.resolveId(element, definition, parserContext);
		return StringUtils.hasText(id) ? id : ConfigConstants.CASSANDRA_SESSION;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {

		String keyspace = element.getAttribute("keyspace");
		if (StringUtils.hasText(keyspace)) {
			builder.addPropertyValue("keyspace", keyspace);
		}

		String clusterRef = element.getAttribute("cassandra-cluster-ref");
		if (!StringUtils.hasText(clusterRef)) {
			clusterRef = ConfigConstants.CASSANDRA_CLUSTER;
		}
		builder.addPropertyReference("cluster", clusterRef);

		String converterRef = element.getAttribute("cassandra-converter-ref");
		if (!StringUtils.hasText(converterRef)) {
			converterRef = ConfigConstants.CASSANDRA_CONVERTER;
		}
		builder.addPropertyReference("converter", converterRef);

		postProcess(builder, element);
	}

	@Override
	protected void postProcess(BeanDefinitionBuilder builder, Element element) {
		List<Element> subElements = DomUtils.getChildElements(element);

		// parse nested elements
		for (Element subElement : subElements) {
			String name = subElement.getLocalName();

			if ("keyspace-attributes".equals(name)) {
				builder.addPropertyValue("keyspaceAttributes", parseKeyspaceAttributes(subElement));
				builder.addPropertyValue("tables", parseTablesAttributes(subElement));
			}
		}
	}

	private BeanDefinition parseKeyspaceAttributes(Element element) {
		BeanDefinitionBuilder defBuilder = BeanDefinitionBuilder.genericBeanDefinition(KeyspaceAttributes.class);
		ParsingUtils.setPropertyValue(defBuilder, element, "action", "actionStr");
		ParsingUtils.setPropertyValue(defBuilder, element, "replication-strategy", "replicationStrategy");
		ParsingUtils.setPropertyValue(defBuilder, element, "replication-factor", "replicationFactor");
		ParsingUtils.setPropertyValue(defBuilder, element, "durable-writes", "durableWrites");
		return defBuilder.getBeanDefinition();
	}

	private ManagedList<Object> parseTablesAttributes(Element element) {

		List<Element> subElements = DomUtils.getChildElements(element);
		ManagedList<Object> tables = new ManagedList<Object>(subElements.size());

		// parse nested elements
		for (Element subElement : subElements) {
			String name = subElement.getLocalName();

			if ("table".equals(name)) {
				tables.add(parseTable(subElement));
			}
		}

		return tables;
	}

	private BeanDefinition parseTable(Element element) {
		BeanDefinitionBuilder defBuilder = BeanDefinitionBuilder.genericBeanDefinition(TableAttributes.class);
		ParsingUtils.setPropertyValue(defBuilder, element, "entity-class", "entityClass");
		ParsingUtils.setPropertyValue(defBuilder, element, "name", "tableName");
		return defBuilder.getBeanDefinition();
	}

}