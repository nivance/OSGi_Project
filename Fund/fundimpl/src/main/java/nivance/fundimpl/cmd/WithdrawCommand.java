package nivance.fundimpl.cmd;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.api.DomainDaoSupport;
import nivance.fund.exception.FundException;
import nivance.fundimpl.FundBeanUtil;
import nivance.fundimpl.bean.AccAccountFlow;
import nivance.fundimpl.bean.Account;
import nivance.fundimpl.bean.AmountInputBean;
import nivance.fundimpl.enums.FundTypeEnum;
import nivance.fundimpl.service.FundService;
import nivance.serialize.SerializerUtil;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

/**
 * 商户扣款
 * 
 */
@Slf4j
//@Service("withdrawCommand")
@Component
@Provides
@Instantiate
public class WithdrawCommand implements Command {
	
	@Requires
	private FundService fundService;

	@Override
	public Object execute(Object entity) throws Exception {
		AmountInputBean aib = SerializerUtil.deserialize(DomainDaoSupport.type, (byte[])entity, AmountInputBean.class);
		boolean r = false;
		Account ac = accounts.get(aib.getMerchantid());
		if (ac != null) {
			BigDecimal balance = new BigDecimal(ac.getAmount().get().toString());
			ac.getAmount().set(ac.getAmount().get().subtract(new BigDecimal(aib.getAmount())));
			try {
				AccAccountFlow accAccountFlow = FundBeanUtil.getAccAccountFlow(
						Short.valueOf(aib.getType()), -aib.getAmount(), null,
						aib.getMerchantid(), null, null, balance, null,
						aib.getDesc());
				fundService.insertFlow(accAccountFlow);
				r = true;
			} catch (Exception e) {
				ac.getAmount().set(ac.getAmount().get()
								.add(new BigDecimal(aib.getAmount())));// 扣款
				throw new FundException("withdeaw error .merchartid[" + aib.getMerchantid()
						+ "] ltype[" + FundTypeEnum.code2Des(aib.getType())
						+ "].", e);
			}
		} else {
			log.warn("merchantid[" + aib.getMerchantid() + "]not exist.");
		}
		log.debug("withdraw. merchantid[" + aib.getMerchantid() + "] amount["
				+ aib.getAmount() + "]type[" + FundTypeEnum.code2Des(aib.getType()) + "]desc["
				+ aib.getDesc() + "]. result[" + r + "]");
		return r;
	}
	
}
