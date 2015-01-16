package nivance.fundimpl.cmd;

import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.api.DomainDaoSupport;
import nivance.fundimpl.bean.Account;
import nivance.fundimpl.bean.CreditInputBean;
import nivance.serialize.SerializerUtil;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

/**
 * 信用额度调整
 * 
 */
@Slf4j
//@Service("creditAdjustCommand")
@Component
@Provides
@Instantiate
public class CreditAdjustCommand implements Command {

	@Override
	public Object execute(Object entity) throws Exception {
		CreditInputBean cib = SerializerUtil.deserialize(DomainDaoSupport.type, (byte[]) entity,
				CreditInputBean.class);
		boolean result = false;
		Account ac = accounts.get(cib.getMerchantid());
		if (ac != null) {
			ac.getCreditamount().set(cib.getCreditAmount());
			result = true;
		} else {
			log.info("merchartid[" + cib.getMerchantid() + "] not exist.");
		}
		log.debug("credit adjust. input[" + cib + "], result[" + result + "]");
		return result;
	}
	
}
