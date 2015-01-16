package nivance.jpa.redis.dao;

import java.io.IOException;
import java.util.List;

import nivance.dbpapi.exception.JPAException;

public interface RedisDataInfoDao {

	public final static String LINK = ":";

	public boolean batchInsert(Object data) throws JPAException, IOException;

	public Object findOne(Object data) throws JPAException, IOException;

	public List<Object> findAll(Object data) throws JPAException, IOException;

	public boolean deleteOne(Object data) throws JPAException, IOException;

	public boolean deleteAll(Object data) throws JPAException, IOException;

	public long count(Object data) throws JPAException, IOException;
}
