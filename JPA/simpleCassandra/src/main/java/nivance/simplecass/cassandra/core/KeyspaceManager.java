package nivance.simplecass.cassandra.core;

import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nivance.simplecass.api.JPADao;
import nivance.simplecass.cassandra.annotation.Table;
import nivance.simplecass.cassandra.dao.SimpleCassandraDAO;

import org.apache.commons.lang3.StringUtils;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;

public class KeyspaceManager {

	private @Setter HashMap<String, JPADao> tableJPADao = new HashMap<>();
	private @Getter @Setter Session session;
	private @Getter @Setter String keyspace;
	private @Setter ConsistencyLevel consistency = ConsistencyLevel.QUORUM;// default QUORUM
	private @Setter String strategy = "SimpleStrategy";//NetworkTopologyStrategy
	private @Setter String replica = "1";//default
	private @Setter String basePackage = null;
	
	public KeyspaceManager(CassandraClusterFactory factory, String keyspace,
			String strategy,String replica,ConsistencyLevel consistency) {
		this.session = factory.openKeyspace(keyspace,strategy, replica);
		this.keyspace = keyspace;
		this.consistency = consistency;
	}

	public JPADao getDAO(Class<?> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		if (StringUtils.isBlank(table.name())) {
			throw new RuntimeException("class table can not be empty:" + clazz.getName());
		}
		return tableJPADao.get(table.name());
	}

	public void warmup(Class<?> clazz) {
		try {
			if(clazz.getSuperclass().equals(SimpleCassandraDAO.class)){
				SimpleCassandraDAO dao = (SimpleCassandraDAO) clazz.getDeclaredConstructor().newInstance();
				dao.initWithSession(session,consistency);
				tableJPADao.put(dao.getTablename(), dao);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("error in warming up table for clazz:"+ clazz);
		}
	}

	public void initTables() {
		if(StringUtils.isBlank(basePackage)){
			return ;
		}
		try {
			List<String> daoNames = ScanResolver.scanBasePackage(basePackage);
			for(String className : daoNames){
				Class<?> clazz = Class.forName(className, true, this.getClass().getClassLoader());
				warmup(clazz);
			}
		} catch (Exception e) {
			throw new RuntimeException("init dao error..",e);
		}
	}

}
