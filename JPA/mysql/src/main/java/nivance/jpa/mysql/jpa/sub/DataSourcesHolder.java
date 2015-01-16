package nivance.jpa.mysql.jpa.sub;

public class DataSourcesHolder {
	private static ThreadLocal<String> dataSources = new ThreadLocal<String>();
	
	public static String getDataSource(){
		return dataSources.get();
	}

	public static void setDataSource(String source){
		dataSources.set(source);
	}
	
	public static void removeDataSource(){
		dataSources.remove();
	}
	
}
