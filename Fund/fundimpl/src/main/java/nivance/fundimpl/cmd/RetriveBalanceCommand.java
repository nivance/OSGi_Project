package nivance.fundimpl.cmd;

import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.api.DomainDaoSupport;
import nivance.fundimpl.bean.Account;
import nivance.fundimpl.bean.BalanceOutputBean;
import nivance.serialize.SerializerUtil;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

/**
 * 余额查询
 */
@Slf4j
@Component
@Provides
@Instantiate
public class RetriveBalanceCommand implements Command {

	@Override
	public Object execute(Object object) throws Exception {
		String merchantid = SerializerUtil.deserialize(DomainDaoSupport.type, (byte[])object, String.class);
		log.debug("balance select. merchantid[" + merchantid + "]");
		Account eczit = accounts.get(merchantid);
		BalanceOutputBean result = new BalanceOutputBean();
		if (eczit != null) {
			result.setResult(true);
			result.setBalance(eczit.getAmount().get());
			result.setCreditAmount(eczit.getCreditamount().get());
			result.setMaxsaleamount(eczit.getMaxsaleamount().get());
		} else {
			log.info("fundid Does not exist. merchantid[" + merchantid	+ "]");
			// 返回信息
			result.setResult(false);
		}
		log.debug("balance select. fundid[" + merchantid + "], result[" + result + "]");
		return SerializerUtil.serialize(DomainDaoSupport.type, result);
	}
	
}
