package nivance.fundimpl.bean;

import lombok.Data;

import org.apache.avro.reflect.Nullable;

public @Data class FlowInputBean {

	/** 商户编号 如：888888 */
	@Nullable
	private String merchantid;
	/**
	 * 资金交易类型 (充值: 1 ,中奖返奖:4, 其他加款:6 , 佣金加款 :8 || 扣款：商户提款 5 其他扣款 7 || 投注扣款2,
	 * 投注失败返款3)
	 */
	@Nullable
	private String type;
	/** 消息序列号 */
	@Nullable
	private String messageid;
	/** 投注序列号 */
	@Nullable
	private String orderno;
	/** 开始时间 */
	@Nullable
	private Long startdate;
	/** 结束时间 */
	@Nullable
	private Long enddate;
	/** 开始条数（即:从第几条数据开始） */
	@Nullable
	private int startRow;
	/** 结束条数(即:从开始需要多少条数据) */
	@Nullable
	private int endRow;
}
