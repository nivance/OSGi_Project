package nivance.fundimpl.bean;

import lombok.Data;


public @Data class CreditInputBean {

	/** 商户编号 如：888888 */
	private String merchantid;
	/** 信用金额 */
	private long creditAmount;
}
