package nivance.fundimpl.bean;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Data;

public @Data class Account {

	/** 商户 **/
	private String merchantid;
	/** 余额 **/
	private AtomicReference<BigDecimal> amount;
	/** 信用额度 **/
	private AtomicLong creditamount;
	/** 最大销售金额 **/
	private AtomicLong maxsaleamount;
	/** 当天销售金额 **/
	private AtomicReference<BigDecimal> daysellamount;
}
