package nivance.fundimpl.cmd;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.api.DomainDaoSupport;
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
 * 充值
 * 
 */
@Slf4j
//@Service("rechargeCommand")
@Component
@Provides
@Instantiate
public class RechargeCommand  implements Command {

	@Requires
	private FundService fundService;

	private final String Recharge_Code = "1";
	@Override
	public Object execute(Object entity) throws Exception {
		boolean result = false;
		AmountInputBean inputBean = SerializerUtil.deserialize(DomainDaoSupport.type, (byte[])entity, 
				AmountInputBean.class);
		log.debug("recharge info:" + inputBean);
		Account eczit = accounts.get(inputBean.getMerchantid());
		String typeName = FundTypeEnum.code2Des(inputBean.getType());
		String merchantid = inputBean.getMerchantid();
		if (eczit != null) {// 如果存在该商户
			BigDecimal balance = new BigDecimal(eczit.getAmount().get().toString());
			eczit.getAmount().set(eczit.getAmount().get().add(new BigDecimal(inputBean.getAmount())));
			try {
				AccAccountFlow flow = FundBeanUtil.getAccAccountFlow(
						Short.parseShort(inputBean.getType()),
						inputBean.getAmount(), null, merchantid, Recharge_Code, null,
						balance, null, inputBean.getDesc());
				fundService.insertFlow(flow);
				result = true;
			} catch (Exception e) {
				log.warn("recharge error. merchartid[" + merchantid + "]+type["	+ typeName + "]", e);
				eczit.getAmount().set(eczit.getAmount()
								.get().subtract(new BigDecimal(inputBean.getAmount())));
			}
		} else {
			log.error("merchantid[" + merchantid + "]not exist.");
		}
		log.debug("recharge. input[" + inputBean + "]. result[" + result + "]");
		return result;
	}
}
