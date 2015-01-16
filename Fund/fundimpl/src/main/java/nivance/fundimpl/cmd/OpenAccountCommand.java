package nivance.fundimpl.cmd;

import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.api.DomainDaoSupport;
import nivance.fund.exception.FundException;
import nivance.fundimpl.FundBeanUtil;
import nivance.fundimpl.bean.AccAccount;
import nivance.fundimpl.bean.Account;
import nivance.fundimpl.bean.OpenAccountInputBean;
import nivance.fundimpl.service.FundService;
import nivance.serialize.SerializerUtil;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

/**
 * 
 * 资金开户
 * 
 */
@Slf4j
@Component
@Provides
@Instantiate
public class OpenAccountCommand implements Command {

	@Requires
	private FundService fundService;

	@Override
	public Object execute(Object entity) throws Exception {
		OpenAccountInputBean inputBean = SerializerUtil.deserialize(DomainDaoSupport.type, (byte[])entity,
				OpenAccountInputBean.class);
		boolean result = false;
		try {
			AccAccount ac = fundService.insertAccount(inputBean);
			Account cache = FundBeanUtil.account2Cache(ac);
			accounts.put(inputBean.getMerchantid(), cache);
			result = true;
		} catch (Exception e) {
			log.error("openAccount. merchantid[" + inputBean.getMerchantid()
					+ "]. result[" + result + "]");
			throw new FundException(e);
		}
		return result;
	}

}
