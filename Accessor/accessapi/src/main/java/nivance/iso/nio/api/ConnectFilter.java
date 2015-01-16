package nivance.iso.nio.api;


public interface ConnectFilter {
	
	/**
	 * @param header
	 * @param connInfo连接信息
	 * @param body报文体
	 * @return 错误信息(null||""为正常)
	 */
	public String onConnect(ConnectionInfo connInfo, String[] header, String body);
}
