package nivance.simplecass.service;

import java.util.HashMap;
import java.util.Map;

import nivance.simplecass.cassandra.entity.CoreBO;
import nivance.simplecass.cassandra.entity.MessageCounter;
import lombok.Getter;


public enum DBPCassBean {
	corebo(CoreBO.class),
	messagecounter(MessageCounter.class)
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
