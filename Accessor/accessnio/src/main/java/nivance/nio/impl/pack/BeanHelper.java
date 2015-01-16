package nivance.nio.impl.pack;

import nivance.iso.exception.HeaderTranslateException;
import nivance.iso.nio.api.MessageBean;
import nivance.iso.nio.enums.ByteCounterEnum;
import nivance.iso.nio.enums.EReqHeader;
import nivance.iso.nio.enums.EResHeader;
import nivance.nio.impl.enums.ReturnCodes;

public class BeanHelper {

	public static MessageBean buildReqBean(String headStr, String bodyStr)
			throws HeaderTranslateException {
		String[] header = headStr.split(",");
		String[] body = bodyStr.split(",");
		if (header.length != EReqHeader.LENGTH.ordinal()) {
			throw new HeaderTranslateException(headStr);
		}
		return new MessageBean(header, body);
	}

	public static String[] buildHeader(String headStr)
			throws HeaderTranslateException {
		String[] header = headStr.split(",");
		if (header.length != EReqHeader.LENGTH.ordinal()) {
			throw new HeaderTranslateException(headStr);
		}
		return header;
	}

	/**
	 * @param header
	 * @param body包含returnCode
	 * @return
	 */
	public static PackBean buildErrorRespBean(String[] header, String body) {
//		String orgc = header[EResHeader.command.ordinal()];
		header[EResHeader.command.ordinal()] = ReturnCodes.FAILCMD.getCode();
		PackBean pb = new PackBean(header, body);
//		log.debug("orginalcommand::" + orgc + ", response msg:" + pb);
		return pb;
	}

	/**
	 * @param header
	 * @param body包含returnCode
	 * @return
	 */
	public static PackBean buildRespBean(String[] header, String body) {
		PackBean pb = new PackBean(header, body);
//		log.debug("response msg:" + pb);
		return pb;
	}

	public final static int getEnumLength(ByteCounterEnum enums) {
		return 0;
	}
}
