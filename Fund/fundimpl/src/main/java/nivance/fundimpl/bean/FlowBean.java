package nivance.fundimpl.bean;

import lombok.Data;

import org.apache.avro.reflect.Nullable;

public @Data class FlowBean {

	/**uuid*/
	@Nullable
	private String uuid;
	/**期号*/
	@Nullable
	private String period;
	/**游戏 */
	@Nullable
	private String ltype;
	/** 资金操作类型  (充值: 1 ,中奖返奖:4, 其他加款:6 , 佣金加款 :8 || 扣款：商户提款  5    其他扣款   7)*/
	@Nullable
	private String type;
	/** 消息序列号*/
	@Nullable
	private String messageid;
	/** 票号 */
	@Nullable
	private String orderno;
	/** 充值金额 */
	@Nullable
	private String amount;
    /** 交易时余额*/
	@Nullable
	private String balance;
	/**交易时间*/
	@Nullable
	private Long createdate;
	/** 描述*/
	@Nullable
	private String description;

}
