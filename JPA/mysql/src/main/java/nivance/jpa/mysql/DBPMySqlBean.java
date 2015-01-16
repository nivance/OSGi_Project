package nivance.jpa.mysql;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import nivance.jpa.mysql.entity.AdminOperator;

import org.apache.commons.lang3.StringUtils;


public enum DBPMySqlBean {
	adminoperator(AdminOperator.class,"T_ADMIN_OPERATOR",DataSource.ISO_PARA,true)
	;

	@Getter
	private Class<?> clazz;
	@Getter
	private String table;
	@Getter
	private boolean staticTable;
	@Getter
	private DataSource dataSource;
	
	private DBPMySqlBean(Class<?> clazz,String table,DataSource dataSource , boolean staticTable) {
		this.clazz = clazz;
		this.table = table;
		this.dataSource = dataSource;
		this.staticTable = staticTable;
	}
	
	private static Map<String,DBPMySqlBean> classMap = new HashMap<>();
	
	static{
		for(DBPMySqlBean info :DBPMySqlBean.values()){
			classMap.put(info.name(), info);
		}
	}
	
	public static Class<?> getClass2Name(String name){
		name = name.toLowerCase();
		if(StringUtils.contains(name, separator)){
			String[] str = StringUtils.split(name, separator);
			return classMap.get(str[0]).getClazz();
		}
		return classMap.get(name).getClazz();
	}
	
	public static DataSource getDataSource2Name(String name){
		name = name.toLowerCase();
		if(StringUtils.contains(name, separator)){
			String[] str = StringUtils.split(name, separator);
			return classMap.get(str[0]).getDataSource();
		}
		return classMap.get(name).getDataSource();
	}
	
	public static String getDaoName(String name){
		name = name.toLowerCase();
		if(StringUtils.contains(name, separator)){
			String[] str = StringUtils.split(name, separator);
			return classMap.get(str[0]).name();
		}
		return classMap.get(name).name();
	}
	
	public static String getTable2Name(String name){
		name = name.toLowerCase();
		if(StringUtils.contains(name, separator)){
			String[] str = StringUtils.split(name, separator);
			String tableName = classMap.get(str[0]).getTable();
			for(int i = 1 ;i<str.length;i++){
				tableName+=str[i];
				if(i<(str.length-1)){
					tableName+="_";
				}
			}
			return tableName;
		}
		return classMap.get(name).getTable();
	}
	
	public static boolean isStaticTable(String name){
		name = name.toLowerCase();
		return classMap.get(name)==null?false:classMap.get(name).isStaticTable();
	}
	
	private final static String separator = "_";
}
