package nivance.fundimpl.cmd;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.api.DomainDaoSupport;
import nivance.fundimpl.bean.Account;
import nivance.fundimpl.bean.WagerAmountInputBean;
import nivance.fundimpl.service.FundService;
import nivance.serialize.SerializerUtil;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

/**
 * 投注扣款\返款
 * 
 */
@Slf4j
@Component
@Provides
@Instantiate
public class WagerCommand implements Command {
	public static BigDecimal MAX_WAGER_AMOUNT_THRESHOLD = BigDecimal.valueOf(8,	1);

	@Requires
	private FundService fundService;

	@Override
	public Object execute(Object entity) throws Exception {
		boolean result = false;
		WagerAmountInputBean inputBean = SerializerUtil.deserialize(DomainDaoSupport.type, (byte[])entity,
				WagerAmountInputBean.class);
		Account acc = accounts.get(inputBean.getMerchantid());// 账户资金信息
		if (acc != null) {
			if (inputBean.getAmount() > 0) {// 投注扣款
				//result = wagerProc(inputBean, acc);
			} else {// 投注失败返款
				//result = wagerFailProc(inputBean, acc);
			}
		} else {
			log.warn("merchantid not exist. merchantid["
					+ inputBean.getMerchantid() + "]");
		}
		log.debug("wager. merchantid[" + inputBean.getMerchantid()
				+ "] amount[" + inputBean.getAmount() + "]ltype["
				+ inputBean.getLtype() + "]period[" + inputBean.getPeriod()
				+ "]messageid[" + inputBean.getMessageid() + "]. result["
				+ result + "]");
		return result;
	}


}