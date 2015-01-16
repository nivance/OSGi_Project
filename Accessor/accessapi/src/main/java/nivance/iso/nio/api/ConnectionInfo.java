package nivance.iso.nio.api;

public interface ConnectionInfo {

	/**
	 * 连接是否连接
	 */
	public boolean isConnectionOpen();
	/**
	 * 取得连接对象
	 */
	public String getRemoteAddress();

	/**
	 * 取得对方地址ip
	 * @return
	 */
	public String getRemoteIP();
	
	/**
	 * @return
	 */
	public long getHeartBeatTime();
	
	/**
	 * @return
	 */
	public void setHeartBeatTime(long time);
	
	/**
	 * 	设定该连接是经过登陆验证过的
	 * @param owner 表示省份等所属
	 */
	public void authorite(String owner);
	
	public boolean IsAuthorited();
	
}
