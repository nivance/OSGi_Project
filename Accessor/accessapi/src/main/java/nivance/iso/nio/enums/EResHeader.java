package nivance.iso.nio.enums;

import lombok.Getter;

/**
 * 协议包头
 * @author brew
 *
 */
public enum EResHeader implements ByteCounterEnum.iface  {
	messageLength("报文长度",4),//
	version("版本",5),//
	command("接口命令",4),//
	timestamp("响应时间",13),//
	UUID("对应请求中的UUID",32),//
	returnCode("返回码",3),//
	
	LENGTH("最大长度",0);//
	
	
	public static final int BytesLength = ByteCounterEnum
			.calcLength(EResHeader.class);

	@Getter
	private String desc;

	@Getter
	private int byteCount;

	EResHeader(String desc, int byteCount) {
		this.desc = desc;
		this.byteCount = byteCount;
	}

	public static void main(String[] args) {
		System.out.println(EResHeader.BytesLength);
	}

}
