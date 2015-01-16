package nivance.jpa.mysql.jpa.sub;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import lombok.Setter;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

	private @Setter DataSource defaultDataSource;
	private @Setter Map<Object,Object> targetDataSources;
	
	@PostConstruct
	private void init(){
		if(defaultDataSource==null){
			throw new RuntimeException(" default dataSource can not be null.");
		}
		super.setDefaultTargetDataSource(defaultDataSource);
		if(targetDataSources!=null){
			super.setTargetDataSources(targetDataSources);
		}
	}
	
	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourcesHolder.getDataSource();
	}

}
