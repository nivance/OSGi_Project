package nivance.iso.nio.api;


public interface MessageListener {
	public void onMessage(ConnectionInfo connection, String header[], String body,OnMessageComplete completeHandler);
}
