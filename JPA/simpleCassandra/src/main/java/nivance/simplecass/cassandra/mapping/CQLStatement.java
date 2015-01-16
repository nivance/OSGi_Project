package nivance.simplecass.cassandra.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nivance.simplecass.cassandra.exception.CQLGenException;
import nivance.simplecass.cassandra.util.BeanPropertyMapper;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

public abstract class CQLStatement {

	protected List<String> fieldnames = new ArrayList<>();
	protected List<String> postFieldnames = new ArrayList<>();

	protected PreparedStatement prestmt;
	protected String cachedCQL;

	protected Object[] getFieldValues(HashMap<String,Object> hb) {
		Object[] ret = new Object[fieldnames.size() + postFieldnames.size()];
		int i = 0;
		for (String name : fieldnames) {
			ret[i++] = hb.get(name);
		}
		for (String name : postFieldnames) {
			ret[i++] = hb.get(name);
		}
		return ret;
	}

	public CQLStatement() {
		super();
	}

	public BoundStatement bind(HashMap<String,Object> hb){
		BoundStatement ret = new BoundStatement(prestmt);
		ret.bind(getFieldValues(hb));
		return ret;
	}

	public BoundStatement bind(){
		BoundStatement ret = new BoundStatement(prestmt);
		return ret;
	}

	public BoundStatement getStatement(Object obj) throws CQLGenException {
		return bind(BeanPropertyMapper.hashbeanFrom(obj));
	}

	public CQLStatement prepare(Session session,ConsistencyLevel consistency){
		prestmt=session.prepare(cachedCQL);
		prestmt.setConsistencyLevel(consistency);
		return this;
	}
	
	public String toString(){
		return "CQLStatement["+cachedCQL+"]@"+this.hashCode();
	}


}