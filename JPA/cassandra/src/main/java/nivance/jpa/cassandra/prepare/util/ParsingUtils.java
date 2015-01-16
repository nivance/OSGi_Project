/*
 * Copyright 2014 by the original author(s).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nivance.jpa.cassandra.prepare.util;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class ParsingUtils {

	public static void setPropertyValue(BeanDefinitionBuilder builder, Element element, String attrName,
			String propertyName) {

		Assert.notNull(builder, "BeanDefinitionBuilder must not be null");
		Assert.notNull(element, "Element must not be null");
		Assert.hasText(attrName, "Attribute name must not be null");
		Assert.hasText(propertyName, "Property name must not be null");

		String attr = element.getAttribute(attrName);

		if (StringUtils.hasText(attr)) {
			builder.addPropertyValue(propertyName, attr);
		}
	}

}
