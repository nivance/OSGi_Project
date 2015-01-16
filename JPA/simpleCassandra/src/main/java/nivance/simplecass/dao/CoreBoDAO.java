package nivance.simplecass.dao;

import nivance.simplecass.cassandra.annotation.TableType;
import nivance.simplecass.cassandra.dao.SimpleCassandraDAO;
import nivance.simplecass.cassandra.entity.CoreBO;
import nivance.simplecass.cassandra.exception.CQLGenException;

public class CoreBoDAO extends SimpleCassandraDAO {

	public CoreBoDAO() throws CQLGenException {
		super(CoreBO.class,TableType.NORMAL);
	}


}
