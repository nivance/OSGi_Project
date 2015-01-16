package nivance.nio.test;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import nivance.iso.nio.api.ConnectionInfo;
import nivance.iso.nio.api.MessageListener;
import nivance.iso.nio.api.OnMessageComplete;
import nivance.nio.impl.filter.StringTransFilter;
import nivance.nio.impl.pack.PackBean;

import org.apache.commons.codec.digest.DigestUtils;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.attributes.Attribute;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.filterchain.TransportFilter;
import org.glassfish.grizzly.impl.FutureImpl;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringClient implements MessageListener {
	Logger log = LoggerFactory.getLogger(StringClient.class);
	
	protected final Attribute<FutureImpl<PackBean>> attrSendBuffer = Grizzly.DEFAULT_ATTRIBUTE_BUILDER
			.createAttribute(StringClient.class.getName() + '-'
					+ System.identityHashCode(this) + ".sendbuff");

	private TCPNIOTransport transport;
	private Connection<?> conn;
//	public int PORT = 10036;
//	public String ip = "172.16.98.53";
	public String ip = "172.16.7.85";
	public int PORT = 11036;
	
	public static void main(String[] args) {
		StringClient sc = new StringClient();
		sc.init();
//		normarCase(sc);
//		login(sc);
//		logout(sc);
		heartbeat(sc);
//		encash(sc);
		
//		errorString(sc);
	}
	
	public static void errorString(StringClient sc){
		String header[] = { "0000",// bodylength
				"1.0.0",// version(5)
				"1002",// command(4)
				"12323",
				"12323",
				"12323",
				String.valueOf(System.currentTimeMillis()),// timestamp(13)
				UUID.randomUUID().toString().replaceAll("-", ""),// UUID(32)
		};
		PackBean pb = new PackBean(header, "");
		System.out.println("send:" + pb);
		sc.writeSync(pb);
//		System.out.println("receive:" + reps);
	}
	
	public static void normarCase(StringClient sc){
		try {
			login(sc);
			for(int i=0;i < 500;i++){
				heartbeat(sc);
				Thread.sleep(60000);
			}
			logout(sc);
//			Thread.sleep(10000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sc.stop();
	}
	
	public void init() {
		// Create a FilterChain using FilterChainBuilder
		FilterChainBuilder filterChainBuilder = FilterChainBuilder.stateless();
		// Add TransportFilter, which is responsible
		filterChainBuilder.add(new TransportFilter());
		filterChainBuilder.add(new StringTransFilter());
		filterChainBuilder.add(new ClientFilter(this));
		// Create TCP transport
		final TCPNIOTransportBuilder builder = TCPNIOTransportBuilder
				.newInstance();
		transport = builder.build();
		transport.setKeepAlive(true);
		transport.setProcessor(filterChainBuilder.build());
		try {
			transport.start();
			conn = transport.connect(ip, PORT).get();
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void login(StringClient sc) {
//		String content = "00000fdfsfd15e9b4f94a0e6";
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String content =DigestUtils.md5Hex("1.0.0"+uuid+"0123456789ABCDEF");
		String header[] = { "0000",// bodylength
				"1.0.0",// version(5)
				"1001",// command(4)
				String.valueOf(System.currentTimeMillis()),// timestamp(13)
				uuid,// UUID(32)
		};
		PackBean pb = new PackBean(header, content);
		System.out.println("send:" + pb);
		PackBean reps = sc.writeSync(pb);
		System.out.println("receive:" + reps);
	}
	
	public static void logout(StringClient sc) {
		String header[] = { "0000",// bodylength
				"1.0.0",// version(5)
				"1002",// command(4)
				String.valueOf(System.currentTimeMillis()),// timestamp(13)
				UUID.randomUUID().toString().replaceAll("-", ""),// UUID(32)
		};
		PackBean pb = new PackBean(header, null);
		System.out.println("发送登出信息:" + pb);
		PackBean reps = sc.writeSync(pb);
		System.out.println("接受登出响应:" + reps);
	}

	public static void heartbeat(StringClient sc) {
		String header[] = { "0000",// bodylength
				"1.0.0",// version(5)
				"1000",// command(4)
				String.valueOf(System.currentTimeMillis()),// timestamp(13)
				UUID.randomUUID().toString().replaceAll("-", ""),// UUID(32)
		};
		PackBean pb = new PackBean(header, "");
		System.out.println("发送心跳信息:" + pb);
		sc.writeSync(pb);
//		System.out.println("接受心跳响应:" + reps);
	}
	
	public static void encash(StringClient sc) {
		login(sc);
		String header[] = { "0000",// bodylength
				"1.0.0",// version(5)
				"2000",// command(4)
				String.valueOf(System.currentTimeMillis()),// timestamp(13)
				UUID.randomUUID().toString().replaceAll("-", ""),// UUID(32)
		};
		PackBean pb = new PackBean(header, "010999999,G0218,00001,0209901,001,1000000001,1000000001");
		System.out.println("发送查奖信息:" + pb);
		PackBean reps = sc.writeSync(pb);
		System.out.println("接受查奖响应:" + reps);
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public PackBean writeSync(PackBean pb) {
		try {
			conn.write(pb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void stop() {
		try {
			transport.shutdownNow();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			log.info("socket服务关闭");
		}
	}

	@Override
	public void onMessage(ConnectionInfo connection, String[] header,
			String body, OnMessageComplete completeHandler) {
		PackBean pb = new PackBean(header, body);
		System.out.println("receive11111:" + pb);
	}

}
