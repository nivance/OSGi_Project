package nivance.fundimpl.cmd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nivance.fundimpl.bean.Account;

public interface Command {
	
	/**
	 * 账户资金信息
	 */
	Map<String, Account> accounts = new ConcurrentHashMap<String, Account>();
	
	/**
	 * @return
	 */
	public Object execute(Object object) throws Exception;
	

}
