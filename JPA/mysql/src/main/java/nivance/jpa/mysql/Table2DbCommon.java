package nivance.jpa.mysql;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nivance.dbpapi.exception.JPAException;

import org.apache.commons.lang3.StringUtils;

public class Table2DbCommon {

	private static Map<String,DataSource> RELATION_ALL = new HashMap<>();
	
	static{
		//PARA
		RELATION_ALL.put("T_ADMIN", DataSource.ISO_PARA);
		RELATION_ALL.put("T_PARA", DataSource.ISO_PARA);
		RELATION_ALL.put("T_SECPRIOVINCE_PRIZEGROUP", DataSource.ISO_PARA);
		RELATION_ALL.put("T_PRIOVINCE_PRIZEGROUP", DataSource.ISO_PARA);
		//MIG
		RELATION_ALL.put("T_CORE", DataSource.ISO_MIG);
		//STAT
		RELATION_ALL.put("T_STAT", DataSource.ISO_STAT);
		//LOG
		RELATION_ALL.put("T_LOG", DataSource.ISO_LOG);
		RELATION_ALL.put("T_AWARD", DataSource.ISO_LOG);
		RELATION_ALL.put("T_UNAWARD", DataSource.ISO_LOG);
		RELATION_ALL.put("T_PRIOVINCE_LOGINOUT", DataSource.ISO_LOG);
	}
	
	private static String addPerfixSql(String table,DataSource ds){
		return ds.getDbName()+"."+table;
	}
	
	public static String sqlAddDBName(String sql){
		for(Entry<String,DataSource> entry : RELATION_ALL.entrySet()){
			if(StringUtils.contains(sql, entry.getKey())){
				sql = StringUtils.replace(sql, entry.getKey(), addPerfixSql(entry.getKey(), entry.getValue()));
			}
		}
		return sql;
	}
	
	public static DataSource getDataSource(String sql) throws JPAException{
		// 统计包含所有其他库数据,但是其他库不包含统计
		if(StringUtils.contains(sql, "T_STAT")){
			return DataSource.ISO_STAT;
		}
		for(Entry<String,DataSource> entry : RELATION_ALL.entrySet()){
			if(StringUtils.contains(sql, entry.getKey())){
				return entry.getValue();
			}
		}
		throw new JPAException(" sql is error .not find data base name ["+sql+"]");
	}
	
}
