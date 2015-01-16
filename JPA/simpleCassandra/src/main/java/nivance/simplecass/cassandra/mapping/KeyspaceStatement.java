package nivance.simplecass.cassandra.mapping;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.cassandra.config.DataCenterReplication;
import org.springframework.cassandra.core.cql.generator.CreateKeyspaceCqlGenerator;
import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification;

public class KeyspaceStatement {
	public static final String SIMPLE_STRATEGY = "SimpleStrategy";
	public static final String NETWORK_TOPOLOGY_STRATEGY = "NetworkTopologyStrategy";
	
	public static String getCreateKeySpace(String name,String strategy, String replica) {
		if(SIMPLE_STRATEGY.equalsIgnoreCase(strategy)){
			return getSimpleKeySpace(name, NumberUtils.toInt(replica));
		}else if(NETWORK_TOPOLOGY_STRATEGY.equalsIgnoreCase(strategy)){
			String[] dataCenters = StringUtils.split(replica, ",");
			DataCenterReplication[] dcrs = new DataCenterReplication[dataCenters.length];
			for(int i=0 ;i < dataCenters.length;i++){
				String[] dcInfo = StringUtils.split(dataCenters[i], ":");
				DataCenterReplication dcr = DataCenterReplication.dcr(dcInfo[0], NumberUtils.toLong(dcInfo[1]));
				dcrs[i] = dcr;
			}
			return getNetworkKeySpace(name, dcrs);
		}
		throw new RuntimeException("kespace strategy is error..["+strategy+"]");
	}
	
	private static String getSimpleKeySpace(String name,int replica){
		CreateKeyspaceSpecification ksSpec = CreateKeyspaceSpecification.createKeyspace(name).ifNotExists(true).withSimpleReplication(replica);
		return CreateKeyspaceCqlGenerator.toCql(ksSpec);
	}
	
	//dc1:1,dc2:1
	private static String getNetworkKeySpace(String name,DataCenterReplication... dcrs){
		CreateKeyspaceSpecification ksSpec = CreateKeyspaceSpecification.createKeyspace(name).ifNotExists(true).withNetworkReplication(dcrs);
		return CreateKeyspaceCqlGenerator.toCql(ksSpec);
	}

}
