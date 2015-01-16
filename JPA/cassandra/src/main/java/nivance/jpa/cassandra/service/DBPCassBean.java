package nivance.jpa.cassandra.service;

import java.util.HashMap;
import java.util.Map;

import nivance.jpa.cassandra.entity.CoreBO;
import lombok.Getter;


public enum DBPCassBean {
	corebo(CoreBO.class)
	;
	
	@Getter
	private Class<?> clazz;
	
	private DBPCassBean(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	
	private static Map<String,DBPCassBean> dbInfo = new HashMap<>();
	
	static{
		for(DBPCassBean info :DBPCassBean.values()){
			dbInfo.put(info.name(), info);
		}
	}
	
	public static Class<?> getClass2Name(String name){
		return dbInfo.get(name.toLowerCase()).getClazz();
	}
	
}
