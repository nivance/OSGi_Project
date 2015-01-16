package nivance.jpa.cassandra.prepare.schema;

import java.util.LinkedList;
import java.util.List;

import nivance.jpa.cassandra.prepare.convert.CassandraConverter;

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

public class DefaultFindByExampleOperation<T> extends AbstractFindOperation<T> {

	protected T entity;
	public DefaultFindByExampleOperation(Session session,
			CassandraConverter cassandraConverter, Class<T> entityClass,T entity) {
		super(session, cassandraConverter, entityClass);
		this.entity = entity;
	}

	@Override
	public RegularStatement createStatement() {
		Select select = QueryBuilder.select().all().from(getTableName());
		Select.Where w = select.where();
		List<Clause> list = new LinkedList<Clause>();
		((CassandraConverter) entityReader).write(entity, list);
		for (Clause c : list) {
			w.and(c);
		}
		return select.allowFiltering();
	}

}
