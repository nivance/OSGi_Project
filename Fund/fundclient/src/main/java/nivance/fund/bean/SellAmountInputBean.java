package nivance.fund.bean;

import lombok.Data;

import org.apache.avro.reflect.Nullable;

public @Data class SellAmountInputBean {

	/** 商户编号 如：888888 */
	@Nullable
	private String merchantid;
	/** 每日最大销售金额 */
	@Nullable
	private long sellamount;
}
