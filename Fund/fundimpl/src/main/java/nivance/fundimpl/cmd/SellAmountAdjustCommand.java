package nivance.fundimpl.cmd;

import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.api.DomainDaoSupport;
import nivance.fundimpl.bean.Account;
import nivance.fundimpl.bean.SellAmountInputBean;
import nivance.serialize.SerializerUtil;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

/**
 * 最大销售金额调整
 * 
 */
@Slf4j
//@Service("sellAmountAdjustCommand")
@Component
@Provides
@Instantiate
public class SellAmountAdjustCommand implements Command {

	@Override
	public Object execute(Object entity) throws Exception {
		SellAmountInputBean sib = SerializerUtil.deserialize(DomainDaoSupport.type, (byte[])entity,
				SellAmountInputBean.class);
		boolean rb = false;
		Account ac = accounts.get(sib.getMerchantid());
		if (ac != null) {
			ac.getMaxsaleamount().set(sib.getSellamount());
			rb = true;
		} else {
			log.info("merchartid[" + sib.getMerchantid() + "] not exist.");
		}
		log.debug("sell amount adjust. input[" + sib + "]. result["	+ rb + "]");
		return rb;
	}
	
}
