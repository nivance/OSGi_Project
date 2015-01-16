package nivance.fundimpl.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * 资金交易类型枚举类
 * 
 */
public enum FundTypeEnum {

	RECHARGE("1", "充值"), WAGER("2", "投注扣款"), WAGER_FAILED("3", "投注失败退款"), REWARD(
			"4", "中奖返奖"), ENCASH("5", "商户提款"), OTHER_RECEIVABLE("6", "其它加款"), OTHER_DEDUCTION(
			"7", "其它扣款"), COMMISSION_PAYMENT("8", "佣金加款 ");

	private FundTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	private @Getter
	final String code;
	private @Getter
	final String desc;

	public static Map<String, String> FundTypeMaps = new HashMap<String, String>();

	static {
		for (FundTypeEnum typeEnum : FundTypeEnum.values()) {
			FundTypeMaps.put(typeEnum.getCode(), typeEnum.getDesc());
		}
	}

	public static String code2Des(String code) {
		String des = FundTypeMaps.get(code);
		return des != null ? des : null;
	}

}
