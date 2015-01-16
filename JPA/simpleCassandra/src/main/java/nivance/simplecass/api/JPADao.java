package nivance.simplecass.api;

import java.util.HashMap;
import java.util.List;

import nivance.simplecass.cassandra.exception.CQLGenException;

public interface JPADao {
	
	public String getTablename();

	public boolean CreateTable();

	public Object doBySQL(String sql);

	public boolean insert(HashMap<String,Object> hb);

	public boolean insertIfNotExist(HashMap<String,Object> hb);

	public boolean insertBatch(List<HashMap<String,Object>> hbs);

	public boolean insertBatchIfNotExist(List<HashMap<String,Object>> hbs);

	public boolean update(HashMap<String,Object> hb);

	public boolean update(List<HashMap<String,Object>> hbs);

	public HashMap<String,Object> findOne(HashMap<String,Object> id);

	public boolean exists(HashMap<String,Object> s);

	public List<HashMap<String,Object>> findAll();
	
	public List<HashMap<String,Object>> findAll(List<HashMap<String,Object>> entities);

	public long count();

	public long count(HashMap<String,Object> s);

	public boolean delete(HashMap<String,Object> hb);

	public boolean deleteIfExist(HashMap<String,Object> hb);

	public boolean deleteAll(List<HashMap<String,Object>> hbs);

	public boolean deleteAllIfExist(List<HashMap<String,Object>> hbs);

	public boolean deleteAll();

	public boolean truncate();
	
	public List<HashMap<String,Object>> findByExample(HashMap<String,Object> hb) throws CQLGenException;
	
}
