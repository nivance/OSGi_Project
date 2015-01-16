package nivance.simplecass.dao;

import nivance.simplecass.cassandra.annotation.TableType;
import nivance.simplecass.cassandra.dao.SimpleCassandraDAO;
import nivance.simplecass.cassandra.entity.MessageCounter;
import nivance.simplecass.cassandra.exception.CQLGenException;

public class MessageCounterDAO extends SimpleCassandraDAO {

	public MessageCounterDAO() throws CQLGenException {
		super(MessageCounter.class,TableType.COUNTER);
	}


}
