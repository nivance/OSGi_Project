package nivance.nio.impl;

import nivance.iso.nio.api.ConnectionInfo;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.grizzly.Connection;

public class ConnectionInfoImpl implements ConnectionInfo {

	private Connection<?> conn;
	private String owner;
	private long heartbeantime = 0;	

	public ConnectionInfoImpl(Connection<?> conn) {
		this.conn = conn;
	}


	@Override
	public String getRemoteAddress() {
		return conn.getPeerAddress().toString();
	}

	@Override
	public String getRemoteIP() {
		return StringUtils.substringBetween(conn.getPeerAddress().toString(), "/", ":");
	}

	public boolean IsAuthorited() {
		return owner!=null;
	}

	@Override
	public void authorite(String owner) {
		this.owner=owner;
	}
	
	@Override
	public long getHeartBeatTime() {
		return this.heartbeantime;
	}

	@Override
	public void setHeartBeatTime(long time) {
		this.heartbeantime = time;		
	}

	@Override
	public boolean isConnectionOpen() {
		return conn.isOpen();
	}

}
