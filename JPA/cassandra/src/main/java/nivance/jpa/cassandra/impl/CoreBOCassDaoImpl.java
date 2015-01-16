package nivance.jpa.cassandra.impl;

import nivance.jpa.cassandra.dao.CoreBOCassDao;
import nivance.jpa.cassandra.entity.CoreBO;
import nivance.jpa.cassandra.prepare.convert.CassandraConverter;
import nivance.jpa.cassandra.prepare.schema.ConsistencyLevel;
import nivance.jpa.cassandra.prepare.schema.RetryPolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Session;

/**
 * Sample repository managing {@link CoreBO} entities.
 * 
 */
public class CoreBOCassDaoImpl extends AbstractCassDaoImpl<CoreBO, String>
		implements CoreBOCassDao 
		{

	private static Logger logger = LoggerFactory
			.getLogger(CoreBOCassDaoImpl.class);

	public CoreBOCassDaoImpl(Session session,
			CassandraConverter converter, ConsistencyLevel consistencyLevel,
			RetryPolicy retryPolicy) {
		super(session, converter, CoreBO.class, consistencyLevel, retryPolicy);
		logger.debug("CoreBOCassDaoImpl is started!");
	}


}
