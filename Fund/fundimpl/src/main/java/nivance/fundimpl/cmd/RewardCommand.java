package nivance.fundimpl.cmd;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.api.DomainDaoSupport;
import nivance.fundimpl.FundBeanUtil;
import nivance.fundimpl.bean.AccAccountFlow;
import nivance.fundimpl.bean.Account;
import nivance.fundimpl.bean.RewardInputBean;
import nivance.fundimpl.enums.FundTypeEnum;
import nivance.fundimpl.service.FundService;
import nivance.serialize.SerializerUtil;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

/**
 * 返奖
 * 
 */
@Slf4j
//@Service("rewardCommand")
@Component
@Provides
@Instantiate
public class RewardCommand implements Command {

	@Requires
	private FundService fundService;

	@Override
	public Object execute(Object entity) throws Exception {
		RewardInputBean rib = SerializerUtil.deserialize(DomainDaoSupport.type, (byte[])entity, RewardInputBean.class);
		Account accountCache = accounts.get(rib.getMerchantid());
		boolean result = false;
		if (accountCache != null) {
			short type = NumberUtils.toShort(FundTypeEnum.REWARD.getCode());
			BigDecimal periodB = NumberUtils.createBigDecimal(rib.getPeriod());
			BigDecimal balance = new BigDecimal(accountCache.getAmount().get().toString());
			accountCache.getAmount().set(accountCache.getAmount().get()
					.add(new BigDecimal(rib.getAmount())));
			// 初始化流水
			AccAccountFlow accountFlow = FundBeanUtil.getAccAccountFlow(type,
					rib.getAmount(), periodB, rib.getMerchantid(),
					rib.getLtype(), null, balance, null, null);
			try {
				fundService.insertFlow(accountFlow);
				result = true;
			} catch (Exception e) {
				log.warn("reward error. merchartid[" + rib.getMerchantid()
						+ "]period[" + periodB + "]ltype[" + rib.getLtype()
						+ "]", e);
				accountCache.getAmount().set(
						accountCache.getAmount().get()
								.subtract(new BigDecimal(rib.getAmount())));// 扣款
			}
		} else {
			log.warn("merchantid[" + rib.getMerchantid() + "]not exist.");
		}
		log.debug("reward. input[" + rib + "]. result[" + result + "]");
		return result;
	}
	
}
