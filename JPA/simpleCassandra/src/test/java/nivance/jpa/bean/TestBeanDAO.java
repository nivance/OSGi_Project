package nivance.jpa.bean;

import nivance.simplecass.cassandra.annotation.TableType;
import nivance.simplecass.cassandra.dao.SimpleCassandraDAO;
import nivance.simplecass.cassandra.exception.CQLGenException;

public class TestBeanDAO extends SimpleCassandraDAO {

	public TestBeanDAO() throws CQLGenException {
		super(TestBean.class,TableType.NORMAL);
	}

}
