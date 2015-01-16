package nivance.fundimpl.bean;

import lombok.Data;

import org.apache.avro.reflect.Nullable;

public @Data class AmountInputBean {

	/** 商户编号 如：888888 */
	@Nullable
	private String merchantid;
	/** 金额 */
	@Nullable
	private long amount;
	/** 资金操作类型  (充值: 1 , 其他加款:6 , 佣金加款 :8 || 扣款：商户提款  5    其他扣款   7)*/
	@Nullable
	private String type;
	/** 描述*/
	@Nullable
	private String desc;
}
