package nivance.jpa.redis;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import nivance.util.JsonUtil;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConnectionCfg {

	public void start(final String dir) {
		this.filePath = dir + FILE_NAME;
		this.build(null, true);

		try {
			Path rootDir = Paths.get(dir);
			final WatchService watcher = FileSystems.getDefault().newWatchService();
			rootDir.register(watcher, ENTRY_MODIFY);
			cfgWatcher.execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						WatchKey key;
						try {
							key = watcher.take();
							for (WatchEvent<?> event : key.pollEvents()) {
								@SuppressWarnings("rawtypes") WatchEvent.Kind kind = event.kind();
								if (kind == OVERFLOW) {
									continue;
								}
								@SuppressWarnings("unchecked") WatchEvent<Path> evt = (WatchEvent<Path>) (event);
								Path name = evt.context();
								if (kind == ENTRY_MODIFY) {
									if (!Files.isDirectory(name, NOFOLLOW_LINKS) && FILE_NAME.equals(name.toString())) {
										// wait for readable of the file
										Thread.sleep(20);
										build(null, false);
									}
								}
							}
						} catch (InterruptedException x) {
							return;
						}
						if (key != null) key.reset();
					}
				}
			});
		} catch (IOException e) {
			log.error("build WatchService of directory [" + dir + "] error.", e);
		}
	}

	public void close() {
		cfgWatcher.shutdownNow();
	}

	public void turnoffAddress(String host, int port) {
		lock.lock();
		try {
			if (this.addresses.containsKey(host + port)) {
				Address mm = this.addresses.get(host + port);
				mm.setTurnoff(true);
				this.makBalance();
				this.writeFile();
				log.warn("address [host:" + host + " port: " + port + "] has turnoff to the cfg file.");
			}
		} catch (IOException e) {
			log.error("write cfg file [" + filePath + "] failed when turnoff address [host:" + host + " port:" + port + "].", e);
		} finally {
			lock.unlock();
		}
	}

	public void overrideCfg(String json) {
		lock.lock();
		try {
			this.build(json, true);
			this.writeFile();
		} catch (Throwable e) {
			log.error("error when override config file [" + filePath + "] with content >>>" + json + "<<<", e);
		} finally {
			lock.unlock();
		}
	}

	private void build(String json, boolean load) {
		lock.lock();
		try {
			if (StringUtils.isBlank(json)) {
				json = readFile();
			}
			if (this.buildAddress(json, load)) {
				this.makBalance();
			}
		} catch (IOException e) {
			log.error("read cfg file error when build redis connection.", e);
		} finally {
			lock.unlock();
		}
	}

	private boolean buildAddress(String jsontxt, boolean reload) throws InvalidPropertiesFormatException {
		boolean handReload = false;
		Map<String, Address> temp = new LinkedHashMap<String, Address>();
		try {
			ObjectNode on = JsonUtil.toObjectNode(jsontxt);
			JsonNode adds = on.findValue("address");
			for (JsonNode nd : adds) {
				String host = nd.get("host").asText();
				int port = nd.get("port").asInt();
				String password = nd.get("password").asText();

				int wt = nd.get("wtimeoutmillis").asInt();
				int wmaxa = nd.get("wmaxactive").asInt();
				int wmaxi = nd.get("wmaxidle").asInt();

				int rt = nd.get("rtimeoutmillis").asInt();
				int rmaxa = nd.get("rmaxactive").asInt();
				int rmaxi = nd.get("rmaxidle").asInt();

				boolean turnoff = nd.get("turnoff").asBoolean();

				Address as = new Address(host, port, password, turnoff, wt, wmaxa, wmaxi, rt, rmaxa, rmaxi);
				if (temp.containsKey(host + port)) {
					log.warn("configuration error.\n" + as + "this adress is duplicate so it's ignored.");
				} else {
					temp.put(host + port, as);
				}
			}
			handReload = on.get("handreload").asBoolean();
		} catch (Throwable e) {
			throw new InvalidPropertiesFormatException(e);
		}
		if (!reload && handReload) {
			log.warn("redis config file [" + filePath + "] was modified, so connections will be checked......");
		}
		if (reload || handReload) {// 如果是调用者要求重载或者配置中指定重载
			this.addresses.clear();
			this.addresses.putAll(temp);
			return true;
		} else {
			return false;
		}
	}

	private void makBalance() {
		List<Connection> conns = new ArrayList<Connection>();
		for (Address ads : this.addresses.values()) {
			if (!ads.isTurnoff()) {
				Connection conn = new Connection(ads.getHost(), ads.getPort(), ads.getPassword(), ads.getWtimeoutmillis(), ads.getWmaxactive(),
						ads.getWmaxidle(), ads.getRtimeoutmillis(), ads.getRmaxactive(), ads.getRmaxidle());
				conns.add(conn);
			}
		}
		this.balancer.reBalance(conns);
	}

	@Override
	public String toString() {
		lock.lock();
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("{\n");
			sb.append("\"address\":[\n");
			int k = 0;
			for (Address ads : this.addresses.values()) {
				sb.append(ads.toString());
				if (k < this.addresses.size() - 1) {
					sb.append(",");
				}
				sb.append("\n");
				k++;
			}
			sb.append("],\n");
			sb.append("\"handreload\": \"false\"\n");
			sb.append("}");
			return sb.toString();
		} finally {
			lock.unlock();
		}
	}

	private String readFile() throws IOException {
		BufferedReader reader = null;
		try {
			StringBuilder sb = new StringBuilder();
			Path file = Paths.get(filePath);
			if (Files.isReadable(file)) {
				reader = Files.newBufferedReader(file, Charset.defaultCharset());
				String line = "";
				while ((line = reader.readLine()) != null) {
					sb.append(line.trim());
				}
			} else {
				log.error(file + " is not readable......");
			}
			return sb.toString();
		} finally {
			if (reader != null) reader.close();
		}
	}

	private void writeFile() throws IOException {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filePath));
			bw.write(this.toString());
		} finally {
			if (bw != null) bw.close();
		}
	}

	private String filePath;
	private static final String FILE_NAME = "connection.cfg";

	private @Resource(name = "loadBalancer") LoadBalancer balancer;
	private final ReentrantLock lock = new ReentrantLock();
	private final LinkedHashMap<String, Address> addresses = new LinkedHashMap<String, Address>();
	private final ExecutorService cfgWatcher = Executors.newSingleThreadExecutor();

	@Data
	@EqualsAndHashCode(of = { "host", "port" })
	private class Address {
		public Address(String host, int port, String password, boolean turnoff, int wt, int wmaxa, int wmaxi, int rt, int rmaxa, int rmaxi) {
			this.host = host;
			this.port = port;
			this.password = password;
			this.turnoff = turnoff;
			this.wtimeoutmillis = wt;
			this.wmaxactive = wmaxa;
			this.wmaxidle = wmaxi;
			this.rtimeoutmillis = rt;
			this.rmaxactive = rmaxa;
			this.rmaxidle = rmaxi;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("    {\n");
			sb.append("        \"host\": \"").append(host).append("\",\n");
			sb.append("        \"port\": \"").append(port).append("\",\n");
			sb.append("        \"password\": \"").append(password).append("\",\n");
			sb.append("        \"wtimeoutmillis\": ").append(wtimeoutmillis).append(",\n");
			sb.append("        \"wmaxactive\": ").append(wmaxactive).append(",\n");
			sb.append("        \"wmaxidle\": ").append(wmaxidle).append(",\n");
			sb.append("        \"rtimeoutmillis\": ").append(rtimeoutmillis).append(",\n");
			sb.append("        \"rmaxactive\": ").append(rmaxactive).append(",\n");
			sb.append("        \"rmaxidle\": ").append(rmaxidle).append(",\n");
			sb.append("        \"turnoff\": \"").append(turnoff).append("\"\n");
			sb.append("    }");
			return sb.toString();
		}

		private String host;
		private int port;
		private String password;
		private int wtimeoutmillis;
		private int wmaxactive;
		private int wmaxidle;
		private int rtimeoutmillis;
		private int rmaxactive;
		private int rmaxidle;
		private boolean turnoff;
	}

}
