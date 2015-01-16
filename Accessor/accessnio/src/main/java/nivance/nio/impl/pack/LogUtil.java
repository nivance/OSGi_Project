package nivance.nio.impl.pack;

import nivance.iso.nio.enums.EReqHeader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

	public Logger log = null;

	public LogUtil(Class<?> clazz) {
		log = LoggerFactory.getLogger(clazz);
	}

	public void info(String... messages) {
		log.info(getMessageStr(messages));
	}
	
	public void warn(String... messages) {
		log.warn(getMessageStr(messages));
	}

	public void info(Throwable t, String... messages) {
		log.info(getMessageStr(messages), t);
	}

	public void debug(String... messages) {
		log.debug(getMessageStr(messages));
	}

	public void debug(Throwable t, String... messages) {
		log.debug(getMessageStr(messages), t);
	}

	public void error(Throwable t, String... messages) {
		log.error(getMessageStr(messages));
	}

	private String getMessageStr(String... messages) {
		StringBuffer sb = new StringBuffer();
		for (String message : messages) {
			sb.append(message);
			sb.append("    ");
		}
		return sb.toString();
	}

	public void info(String tagname, String ip, PackBean pb) {
		log.info(tagname + ":[" + ip + "]["
				+ pb.header[EReqHeader.UUID.ordinal()] + "][报文长度"
				+ hex2Dec(pb.header[EReqHeader.messageLength.ordinal()])
				+ "]@:[" + pb + "]");
	}

	public void debug(String tagname, String ip, PackBean pb) {
		log.debug(tagname + ":[" + ip + "]["
				+ pb.header[EReqHeader.UUID.ordinal()] + "][报文长度"
				+ hex2Dec(pb.header[EReqHeader.messageLength.ordinal()])
				+ "]@:[" + pb + "]");
	}

	public void error(String tagname, String ip, PackBean pb, Throwable t) {
		log.error(tagname + ":[" + ip + "]["
				+ pb.header[EReqHeader.UUID.ordinal()] + "]stationNum []", t);
	}
	
	/**
	 * 16进制数转成十进制
	 * 
	 * @param hex
	 * @return
	 */
	public long hex2Dec(String hex) {
		return Long.parseLong(hex, 16);
	}
}
