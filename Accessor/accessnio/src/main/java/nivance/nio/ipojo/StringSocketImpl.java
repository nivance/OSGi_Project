package nivance.nio.ipojo;

import static nivance.nio.impl.filter.ConnectionFilter.conns;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import nivance.iso.nio.api.ConnectFilter;
import nivance.iso.nio.api.ConnectionInfo;
import nivance.iso.nio.api.MessageListener;
import nivance.iso.nio.api.OnMessageComplete;
import nivance.iso.nio.enums.EReqHeader;
import nivance.nio.impl.enums.Commands;
import nivance.nio.impl.enums.ReturnCodes;
import nivance.nio.impl.filter.ConnectionFilter;
import nivance.nio.impl.filter.ServerFilter;
import nivance.nio.impl.filter.StringTransFilter;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.apache.felix.ipojo.whiteboard.Whiteboards;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.filterchain.TransportFilter;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.glassfish.grizzly.threadpool.GrizzlyExecutorService;
import org.glassfish.grizzly.utils.DelayedExecutor;
import org.glassfish.grizzly.utils.IdleTimeoutFilter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

@Component
@Provides
@Slf4j
@Instantiate
@Whiteboards(whiteboards = { @Wbp(filter = "(msgactor=*)", onArrival = "onArrival", onDeparture = "onDeparture", onModification = "onModification") })
public class StringSocketImpl implements MessageListener {

	HashMap<String, MessageListener> cmd2Listner = new HashMap<>();
	Map<String, ConnectFilter> connectionListner = new HashMap<>();

	public synchronized void onArrival(ServiceReference<?> ref) {
		Object listener = ref.getBundle().getBundleContext().getService(ref);
		if (listener instanceof MessageListener) {
			log.info("onArrival:MessageListener:" + listener + ",msgactor="
					+ ref.getProperty("msgactor"));
			cmd2Listner.put((String) ref.getProperty("msgactor"),
					(MessageListener) listener);
		}
		if (listener instanceof ConnectFilter) {
			String msgactor = ref.getProperty("msgactor").toString();
			log.info("onArrival:ConnectFilter:" + listener + ",msgactor="
					+ ref.getProperty("msgactor"));
			connectionListner.put(msgactor, (ConnectFilter) listener);
		}
		if (connectionListner.containsKey(Commands.LOGIN.getCode())
				&& conns.size() > 0) {// 如果impl update将连接传过去
			for (String connKey : conns.keySet()) {
				ConnectionInfo connInfo = conns.get(connKey);
				connectionListner.get(Commands.LOGIN.getCode()).onConnect(
						connInfo, null, null);
			}
		}
	}

	public synchronized void onModification(ServiceReference<?> ref) {
		Object listener = ref.getBundle().getBundleContext().getService(ref);
		log.info("onModification::" + listener + ",messageactor="
				+ ref.getProperty("msgactor"));
	}

	public synchronized void onDeparture(ServiceReference<?> ref) {
		BundleContext bc = ref.getBundle().getBundleContext();
		if(bc == null){
			return;
		}
		Object listener = bc.getService(ref);
		log.info("onDeparture::" + listener + ",messageactor="
				+ ref.getProperty("msgactor"));
		if (listener instanceof MessageListener) {
			log.info("onArrival:MessageListener:" + listener + ",msgactor="
					+ ref.getProperty("msgactor"));
			cmd2Listner.remove((String) ref.getProperty("msgactor"));
		}

		if (listener instanceof ConnectFilter) {
			log.info("onArrival:ConnectFilter:" + listener + ",msgactor="
					+ ref.getProperty("msgactor"));
			connectionListner.remove((String) ref.getProperty("msgactor"));
		}
	}

	@Override
	public void onMessage(ConnectionInfo remoteAddr, String header[],
			String body, OnMessageComplete completeHandler) {
		MessageListener listener = cmd2Listner.get(header[EReqHeader.command
				.ordinal()]);
		if (listener != null) {
			listener.onMessage(remoteAddr, header, body, completeHandler);
		} else {
			completeHandler.onComplete(header, ReturnCodes.SYSERROR.getCode());// 系统错误
		}
	}

	public String onConnect(String command, ConnectionInfo connInfo,
			String[] header, String body) throws Exception {
		if (connectionListner.containsKey(command)) {
			return connectionListner.get(command).onConnect(connInfo, header,
					body);
		}
//		return null;
		return ReturnCodes.CMDERROR.getCode();
	}

	private TCPNIOTransport transport;
	private DelayedExecutor de;
	private final static long heartBeatTime = 60;

	@Validate
	public void startServer() {
		String str = System.getProperty("ISO_PORT");
		int port = 10036;
		if (str != null) {
			port = Integer.parseInt(str);
		}
		// Create a FilterChain using FilterChainBuilder
//		beatFilter = new HeartBeatFilter(true);
		FilterChainBuilder filterChainBuilder = FilterChainBuilder.stateless();
		// for reading and writing data to the connection
		filterChainBuilder.add(new TransportFilter());
		de = new DelayedExecutor(GrizzlyExecutorService.createInstance(), heartBeatTime, TimeUnit.SECONDS);
		// Add IdleTimeoutFilter, which will close connections, which stay idle longer than heartBeatTime seconds.
		filterChainBuilder.add(new IdleTimeoutFilter(de, heartBeatTime, TimeUnit.SECONDS));
		filterChainBuilder.add(new StringTransFilter());
		filterChainBuilder.add(new ConnectionFilter(this));
//		filterChainBuilder.add(beatFilter);
		filterChainBuilder.add(new ServerFilter(this));
		
		// Create TCP transport
		transport = TCPNIOTransportBuilder.newInstance().build();
		transport.setProcessor(filterChainBuilder.build());
		try {
			// binding transport to start listen on certain host and port
			transport.bind("0.0.0.0", port);
			de.start();
			transport.start();
			log.info("socket服务开启成功. 0.0.0.0:" + port);
		} catch (IOException e) {
			log.error("socket服务开启失败:", e);
		} 
	}

	@Invalidate
	public void stop() {
		try {
			de.stop();
			transport.shutdownNow();
		} catch (IOException e) {
			log.error("socket服务关闭异常", e);
		} finally {
			log.info("socket服务关闭");
		}

	}

}
