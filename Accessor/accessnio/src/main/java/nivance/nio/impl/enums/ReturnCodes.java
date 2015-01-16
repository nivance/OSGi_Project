package nivance.nio.impl.enums;

import lombok.Getter;

public enum ReturnCodes {
	SUCCESS("000", "处理成功"),
	SYSERROR("001", "系统错误"),
	CMDERROR("004", "接口命令错误"),
	INVALIDATE("005", "身份验证未通过"),
	NOTLOGIN("007", "未登录"),
	
	FAILCMD("0000", "失败头部command"),
	;
	
	ReturnCodes(String code, String desc){
		this.code = code;
		this.desc = desc;
	}

	@Getter
	private String code;
	@Getter
	private String desc;
}
