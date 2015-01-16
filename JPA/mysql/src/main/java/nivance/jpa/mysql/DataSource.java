package nivance.jpa.mysql;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum DataSource {

	ISO_PARA("DS_isopara","DS_isopara_bak","iso_para"),
	ISO_LOG("DS_isolog","DS_isolog_bak","iso_log"),
	ISO_STAT("DS_isostat","DS_isostat_bak","iso_stat"),
	ISO_MIG("DS_isomig","DS_isomig","iso_mig");// bak is main
	
	private @Getter String mainSource;
	private @Getter String bakSource;
	private @Getter String dbName;
	
	public static final String MAIN = "MAIN"; 
	public static final String BAK = "BAK";
	
	private DataSource(String mainSource,String bakSource,String dbName) {
		this.mainSource = mainSource;
		this.bakSource = bakSource;
		this.dbName = dbName;
	}
	
	private static Map<String,DataSource> sources = new HashMap<>();
	
	static{
		for(DataSource ds : DataSource.values()){
			sources.put(ds.name(), ds);
		}
	}
	
	public static DataSource getSource(String name){
		return sources.get(name);
	}
	
	public static String getDbName(String name){
		return sources.get(name).getDbName();
	}
	
}
