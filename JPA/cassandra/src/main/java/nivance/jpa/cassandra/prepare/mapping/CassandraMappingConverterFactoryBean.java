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
package nivance.jpa.cassandra.prepare.mapping;

import nivance.jpa.cassandra.prepare.convert.CassandraConverter;
import nivance.jpa.cassandra.prepare.convert.MappingCassandraConverter;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Convenient factory for configuring a CassandraConverter.
 * 
 * @author Alex Shvid
 */

public class CassandraMappingConverterFactoryBean implements FactoryBean<CassandraConverter>, InitializingBean {

	private CassandraConverter cassandraConverter;

	@Override
	public CassandraConverter getObject() {
		return cassandraConverter;
	}

	@Override
	public Class<? extends CassandraConverter> getObjectType() {
		return CassandraConverter.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() {

		MappingCassandraConverter converter = new MappingCassandraConverter(new CassandraMappingContext());
		converter.afterPropertiesSet();

		// initialize property
		this.cassandraConverter = converter;

	}

}
