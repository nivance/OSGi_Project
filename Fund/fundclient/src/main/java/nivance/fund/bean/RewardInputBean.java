package nivance.fund.bean;

import lombok.Data;

import org.apache.avro.reflect.Nullable;


/**
 * 返奖输入bean
 * @author limingjian
 *
 */
@Data
public class RewardInputBean {

	/** 商户编号 如：888888 */
	@Nullable
	private String merchantid;
	/** 发生总金额 */
	@Nullable
	private long amount;
	/** 期号*/
	@Nullable
	private String period;
	/**游戏*/
	@Nullable
	private String ltype;
		
}
