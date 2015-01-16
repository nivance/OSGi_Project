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

import nivance.jpa.cassandra.prepare.config.CompressionType;
import nivance.jpa.cassandra.prepare.config.ConfigConstants;
import nivance.jpa.cassandra.prepare.config.PoolingOptions;
import nivance.jpa.cassandra.prepare.config.SocketOptions;
import nivance.jpa.cassandra.prepare.core.CassandraClusterFactoryBean;
import nivance.jpa.cassandra.prepare.util.ParsingUtils;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Parser for Cluster definitions.
 * 
 * @author Alex Shvid
 */

public class CassandraClusterParser extends AbstractSimpleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return CassandraClusterFactoryBean.class;
	}

	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
			throws BeanDefinitionStoreException {

		String id = super.resolveId(element, definition, parserContext);
		return StringUtils.hasText(id) ? id : ConfigConstants.CASSANDRA_CLUSTER;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {

		String contactPoints = element.getAttribute("contactPoints");
		if (StringUtils.hasText(contactPoints)) {
			builder.addPropertyValue("contactPoints", contactPoints);
		}

		String port = element.getAttribute("port");
		if (StringUtils.hasText(port)) {
			builder.addPropertyValue("port", port);
		}

		String compression = element.getAttribute("compression");
		if (StringUtils.hasText(compression)) {
			builder.addPropertyValue("compressionType", CompressionType.valueOf(compression));
		}

		postProcess(builder, element);
	}

	@Override
	protected void postProcess(BeanDefinitionBuilder builder, Element element) {
		List<Element> subElements = DomUtils.getChildElements(element);

		// parse nested elements
		for (Element subElement : subElements) {
			String name = subElement.getLocalName();

			if ("local-pooling-options".equals(name)) {
				builder.addPropertyValue("localPoolingOptions", parsePoolingOptions(subElement));
			} else if ("remote-pooling-options".equals(name)) {
				builder.addPropertyValue("remotePoolingOptions", parsePoolingOptions(subElement));
			} else if ("socket-options".equals(name)) {
				builder.addPropertyValue("socketOptions", parseSocketOptions(subElement));
			}
		}

	}

	private BeanDefinition parsePoolingOptions(Element element) {
		BeanDefinitionBuilder defBuilder = BeanDefinitionBuilder.genericBeanDefinition(PoolingOptions.class);
		ParsingUtils.setPropertyValue(defBuilder, element, "min-simultaneous-requests", "minSimultaneousRequests");
		ParsingUtils.setPropertyValue(defBuilder, element, "max-simultaneous-requests", "maxSimultaneousRequests");
		ParsingUtils.setPropertyValue(defBuilder, element, "core-connections", "coreConnections");
		ParsingUtils.setPropertyValue(defBuilder, element, "max-connections", "maxConnections");
		return defBuilder.getBeanDefinition();
	}

	private BeanDefinition parseSocketOptions(Element element) {
		BeanDefinitionBuilder defBuilder = BeanDefinitionBuilder.genericBeanDefinition(SocketOptions.class);
		ParsingUtils.setPropertyValue(defBuilder, element, "connect-timeout-mls", "connectTimeoutMls");
		ParsingUtils.setPropertyValue(defBuilder, element, "keep-alive", "keepAlive");
		ParsingUtils.setPropertyValue(defBuilder, element, "reuse-address", "reuseAddress");
		ParsingUtils.setPropertyValue(defBuilder, element, "so-linger", "soLinger");
		ParsingUtils.setPropertyValue(defBuilder, element, "tcp-no-delay", "tcpNoDelay");
		ParsingUtils.setPropertyValue(defBuilder, element, "receive-buffer-size", "receiveBufferSize");
		ParsingUtils.setPropertyValue(defBuilder, element, "send-buffer-size", "sendBufferSize");
		return defBuilder.getBeanDefinition();
	}

}
