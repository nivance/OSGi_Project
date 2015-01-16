package nivance.fund.bean;

import java.util.List;

import lombok.Data;

import org.apache.avro.reflect.Nullable;

public @Data class WagerAmountInputBean {

	/** 商户编号 如：888888 */
	@Nullable
	private String merchantid;
	/** 发生总金额(正数：投注成功；负数：投注失败) */
	@Nullable
	private double amount;
	/** 期号 */
	@Nullable
	private String period;
	/**游戏*/
	@Nullable
	private String ltype;
	/** 消息序列号*/
	@Nullable
	private String messageid;
	/**投注序列号list*/
	@Nullable
	private List<RecordBean> recordbeanlist;
}
