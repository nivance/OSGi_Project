package nivance.dbpapi.domain;

import lombok.Data;

@Data
public class DomainController {
	/**	表名,如果多个表用#分隔 **/
	private String names;
	/**数据存储方案   cassandra,mysql,wager 等... 默认Cassandra*/
	private String target;
	/**	存储操作发起者 **/
	private String from;
	/**	存储策略 **/
	private ControlPolicy policy;
	/**数据源*/
	private String dataSource;
	
	public DomainController(String names, String target, String from, ControlPolicy policy){
		this.names = names;
		this.target = target;
		this.from = from;
		this.policy = policy;
	}
	
	public DomainController(String names, String target, String from, ControlPolicy policy,String dataSource){
		this.names = names;
		this.target = target;
		this.from = from;
		this.policy = policy;
		this.dataSource = dataSource;
	}
	
	public DomainController(){};
	
}
