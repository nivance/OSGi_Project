package nivance.simplecass.cassandra.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nivance.simplecass.api.JPADao;
import nivance.simplecass.cassandra.annotation.Table;
import nivance.simplecass.cassandra.annotation.TableType;
import nivance.simplecass.cassandra.exception.CQLGenException;
import nivance.simplecass.cassandra.mapping.CQLStatement;
import nivance.simplecass.cassandra.util.RowMapper;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;

public class SimpleCassandraDAO implements JPADao {

	private Session session;
	private Class<?> clazz;
	private StatementSet statements;
	private String tablename;
	private TableType tableType;

	public SimpleCassandraDAO(Class<?> clazz,TableType tableType) throws CQLGenException {
		this.clazz = clazz;
		this.tableType = tableType;
		Table tb = (Table) clazz.getAnnotation(Table.class);
		if (tb == null) {
			throw new CQLGenException("TableName not found");
		}
		tablename = tb.name();
	}

	public void initWithSession(Session session,ConsistencyLevel consistency) throws CQLGenException {
		this.session = session;
		statements = new StatementSet(clazz, session,tableType,consistency);
		statements.createTable(session);
		statements.createIndex(session);
		statements.prepare(session);
		
	}

	public ResultSet execute(Statement stmt) {
		return session.execute(stmt);
	}

	public boolean possible(ResultSet rs) {
		//TODO result is not null?
		if (rs == null)
			return false;
		Row one = rs.one();
		if (one != null)
			return one.getBool(0);
		return true;
	}

	public boolean executeTorF(Statement stmt) {
		return possible(session.execute(stmt));
	}

	@Override
	public boolean insert(HashMap<String,Object> mb) {
		return executeTorF(statements.insert.bind(mb));
	}

	@Override
	public boolean insertIfNotExist(HashMap<String,Object> mb) {
		return executeTorF(statements.insertIfNoExist.bind(mb));
	}

	@Override
	public boolean insertBatch(List<HashMap<String,Object>> mbs) {
		BatchStatement batch = new BatchStatement();
		for (HashMap<String,Object> mb : mbs) {
			batch.add(statements.insert.bind(mb));
		}
		return executeTorF(batch);
	}

	@Override
	public boolean insertBatchIfNotExist(List<HashMap<String,Object>> mbs) {
		BatchStatement batch = new BatchStatement();
		for (HashMap<String,Object> mb : mbs) {
			batch.add(statements.insertIfNoExist.bind(mb));
		}
		return executeTorF(batch);
	}

	@Override
	public String getTablename() {
		return this.tablename;
	}

	@Override
	public boolean CreateTable() {
		return executeTorF(statements.createTable);
	}

	@Override
	public Object doBySQL(String cql) {
		return execute(new SimpleStatement(cql));
	}

	@Override
	public boolean update(HashMap<String,Object> mb) {
		CQLStatement statement = statements.getUpdateStatement(session,mb);
		return executeTorF(statement.bind(mb));
	}

	@Override
	public boolean update(List<HashMap<String,Object>> mbs) {
		BatchStatement batch = new BatchStatement();
		for (HashMap<String,Object> mb : mbs) {
			batch.add(statements.getUpdateStatement(session,mb).bind(mb));
		}
		return executeTorF(batch);
	}

	@Override
	public HashMap<String,Object> findOne(HashMap<String,Object> id) {
		ResultSet set = (ResultSet) execute(statements.findOne.bind(id));
		Row row = set.one();
		if (row != null) {
			return RowMapper.parseFromRow(row);
		}
		return null;
	}

	@Override
	public boolean exists(HashMap<String,Object> id) {
		ResultSet set = (ResultSet) execute(statements.findOne.bind(id));
		Row row = set.one();
		return row!=null;
	}

	public List<HashMap<String,Object>> getAllList(ResultSet set) {
		ArrayList<HashMap<String,Object>> list = new ArrayList<>();
		for(Row row : set.all()){
			list.add(RowMapper.parseFromRow(row));
		}
		return list;
	}

	@Override
	public List<HashMap<String,Object>> findAll() {
		ResultSet set = (ResultSet) execute(statements.findAll.bind());
		return getAllList(set);
	}

	@Override
	public List<HashMap<String,Object>> findAll(List<HashMap<String,Object>> mbs) {
		List<HashMap<String,Object>> list = new ArrayList<>();
		for(HashMap<String,Object>mb: mbs){
			HashMap<String,Object> obj = this.findOne(mb);
			if(obj!=null){
				list.add(obj);
			}
		}
		return list;
	}

	@Override
	public long count() {
		ResultSet set = (ResultSet) execute(statements.countAll.bind());
		return set.one().getLong(0);
	}

	@Override
	public long count(HashMap<String,Object> id) {
		ResultSet set = (ResultSet) execute(statements.countAll.bind(id));
		return set.one().getLong(0);
	}

	@Override
	public boolean delete(HashMap<String,Object> mb) {
		return executeTorF(statements.deleteByKey.bind(mb));
	}

	@Override
	public boolean deleteIfExist(HashMap<String,Object> mb) {
		return executeTorF(statements.deleteIfExist.bind(mb));
	}

	@Override
	public boolean deleteAll(List<HashMap<String,Object>> mbs) {
		BatchStatement batch = new BatchStatement();
		for (HashMap<String,Object> mb : mbs) {
			batch.add(statements.deleteByKey.bind(mb));
		}
		return executeTorF(batch);
	}

	@Override
	public boolean deleteAllIfExist(List<HashMap<String,Object>> mbs) {
		BatchStatement batch = new BatchStatement();
		for (HashMap<String,Object> mb : mbs) {
			batch.add(statements.deleteIfExist.bind(mb));
		}
		return executeTorF(batch);
	}

	@Override
	public boolean deleteAll() {
		return executeTorF(statements.truncateTable);
	}

	@Override
	public boolean truncate() {
		return executeTorF(statements.truncateTable);
	}

	@Override
	public List<HashMap<String,Object>> findByExample(HashMap<String,Object> mb) throws CQLGenException {
		CQLStatement statement = statements.getFindByExampleStatement(session,mb);
		ResultSet set = (ResultSet) execute(statement.bind(mb));
		return getAllList(set);
	}
	
}
