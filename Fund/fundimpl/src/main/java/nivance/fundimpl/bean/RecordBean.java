package nivance.fundimpl.bean;

import lombok.Data;

import org.apache.avro.reflect.Nullable;

public @Data class RecordBean {

	/** 投注序列号 */
	@Nullable
	private String orderno;
	/** 金额(正数：投注成功；负数：投注失败)*/
	@Nullable
	private double amount;
}
