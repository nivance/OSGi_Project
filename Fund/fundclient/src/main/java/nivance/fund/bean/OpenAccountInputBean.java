package nivance.fund.bean;

import lombok.Data;

import org.apache.avro.reflect.Nullable;


/**
 * 
 *  商户开户
 *
 */
public @Data class OpenAccountInputBean {

	/** * 商户编号 */
	@Nullable
	private String merchantid;
	/**	 * 充值金额  */
	@Nullable
	private Long amount;
	/** 信用金额 */
	@Nullable
	private Long creditAmount;
	/** 每日最大销售金额 */
	@Nullable
	private Long sellamount;
}
