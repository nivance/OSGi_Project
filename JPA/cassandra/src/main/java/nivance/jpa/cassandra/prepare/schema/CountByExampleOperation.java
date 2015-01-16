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
package nivance.jpa.cassandra.prepare.schema;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import nivance.jpa.cassandra.prepare.convert.CassandraConverter;

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

/**
 * Copyright © 2014畅享互联.
 * 
 * @Title: DefaultFindByExampleOperation.java
 * @Prject: DBPJKP
 * @date: 2014年4月11日 下午8:34:30
 */
public class CountByExampleOperation<T> extends AbstractGetOperation<Long> {

	private final Class<T> entityClass;
	private CassandraConverter converter;
	protected T entity;
	public CountByExampleOperation(Session session, CassandraConverter converter, Class<T> entityClass,T entity) {
		super(session);
		this.entityClass = entityClass;
		this.converter = converter;
		this.entity = entity;
	}

	@Override
	public Long transform(ResultSet resultSet) {
		Iterator<Row> i = resultSet.iterator();
		if (i.hasNext()) {
			Row row = i.next();
			long count = row.getLong(0);
			return count;
		} else {
			return 0L;
		}
	}

	@Override
	public RegularStatement createStatement() {
		String tableName = getTableName();
		if (tableName == null) {
			tableName = converter.getMappingContext()
					.getPersistentEntity(entityClass).getTableName();
		}
		Select select = QueryBuilder.select().countAll().from(tableName);
		if(entity!=null){
			Select.Where w = select.where();
			List<Clause> list = new LinkedList<Clause>();
			converter.write(entity, list);
			for (Clause c : list) {
				w.and(c);
			}
		}
		return select.allowFiltering();
	}

}
