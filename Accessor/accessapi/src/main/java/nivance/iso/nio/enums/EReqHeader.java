package nivance.iso.nio.enums;

import lombok.Getter;

/**
 * 协议包头
 * 
 * @author brew
 * 
 */
public enum EReqHeader implements ByteCounterEnum.iface {
	messageLength("报文长度", 4), //
	version("版本", 5), //
	command("接口命令", 4), //
	timestamp("请求时间", 13), //
	UUID("通用唯一识别码", 32), //

	LENGTH("最大长度", 0);

	public static final int BytesLength = ByteCounterEnum
			.calcLength(EReqHeader.class);

	@Getter
	private String desc;

	@Getter
	private int byteCount;

	EReqHeader(String desc, int byteCount) {
		this.desc = desc;
		this.byteCount = byteCount;
	}

	public static void main(String[] args) {
		System.out.println(EReqHeader.BytesLength);
	}

}
