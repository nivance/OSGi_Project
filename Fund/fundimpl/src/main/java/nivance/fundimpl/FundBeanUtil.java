package nivance.fundimpl;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import nivance.fundimpl.bean.AccAccount;
import nivance.fundimpl.bean.AccAccountFlow;
import nivance.fundimpl.bean.Account;

public class FundBeanUtil {

	/**
	 * 账户信息转换为账户cache信息
	 * 
	 * @param accAccount
	 * @param cache
	 */
	public static Account account2Cache(AccAccount accAccount) {
		Account cache = new Account();
		cache.setMerchantid(accAccount.getMerchantid());
		cache.setAmount(new AtomicReference<BigDecimal>(accAccount.getAmount()));
		cache.setCreditamount(new AtomicLong(accAccount.getCreditamount()));
		cache.setDaysellamount(new AtomicReference<BigDecimal>(new BigDecimal(
				"0.0")));
		cache.setMaxsaleamount(new AtomicLong(accAccount.getMaxsaleamount()));
		return cache;
	}

	/**
	 * 账户cache信息转换为账户信息
	 * 
	 * @param accAccount
	 * @param cache
	 */
	public static AccAccount cache2account(Account cache) {
		AccAccount accAccount = new AccAccount();
		accAccount.setMerchantid(cache.getMerchantid());
		accAccount.setAmount(cache.getAmount().get());
		accAccount.setCreditamount(cache.getCreditamount().get());
		accAccount.setMaxsaleamount(cache.getMaxsaleamount().get());
		accAccount.setUpdatedate(System.currentTimeMillis());
		return accAccount;
	}

	/**
	 * 组装资金流水表
	 * 
	 * @param type
	 * @param amount
	 * @param period
	 * @param merchantid
	 * @param ltype
	 * @param orderNO
	 * @param balance
	 * @param relateUUID
	 * @param messageId
	 * @param description
	 * @return
	 */
	public static AccAccountFlow getAccAccountFlow(Short type, double amount,
			BigDecimal period, String merchantid, String ltype, String orderNO,
			BigDecimal balance, String messageId, String description) {
		AccAccountFlow aaf = new AccAccountFlow();
		aaf.setUuid(UUID.randomUUID().toString() + System.currentTimeMillis());//TODO IDGenerator.nextID()
		aaf.setType(type);
		aaf.setAmount(new BigDecimal(amount));
		aaf.setPeriod(period);
		aaf.setMerchantid(merchantid);
		aaf.setLtype(ltype);
		aaf.setOrderno(orderNO);
		aaf.setBalance(balance);
		aaf.setCreatedate(System.currentTimeMillis());
		aaf.setMessageid(messageId);
		aaf.setDescription(description);
		return aaf;
	}

}
