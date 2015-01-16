package nivance.fund.bean;

import java.math.BigDecimal;

import lombok.Data;

import org.apache.avro.reflect.Nullable;

public @Data class BalanceOutputBean {

	/** 返回结果 （true 成功   false 失败） */
	@Nullable
	private boolean result;
	/** 余额 */
	@Nullable
	private BigDecimal balance;
	/** 信用金额*/
	@Nullable
	private long creditAmount;
	/** 信用金额*/
	@Nullable
	private long maxsaleamount;
}
