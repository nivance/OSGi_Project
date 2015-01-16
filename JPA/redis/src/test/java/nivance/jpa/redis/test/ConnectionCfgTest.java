package nivance.jpa.redis.test;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.exception.JPAException;
import nivance.jpa.redis.Connection;
import nivance.jpa.redis.ConnectionCfg;
import nivance.jpa.redis.LoadBalancer;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Slf4j
public class ConnectionCfgTest {

	private static String ip = "192.168.100.12";
	private static String addr1 = "[Connection host=" + ip + ", port=7379]";
	private static String addr2 = "[Connection host=" + ip + ", port=7380]";
	private static String addr3 = "[Connection host=" + ip + ", port=7381]";
	private static int port2 = 7380;

	private static ApplicationContext CTX;
	private static final String FILE_NAME = "connection.cfg";
	private ConnectionCfg cfg = null;
	private LoadBalancer balancer = null;
	private String filePath = null;
	private String originalCfg = null;
	private String handoriginalCfg = null;
	private String missedCfg = null;
	private String changeCfg = null;
	private String overrideCfg = null;

	@Before
	public void setUp() throws Exception {
//		start();
	}

	@After
	public void tearDown() throws Exception {
//		end();
	}

	@Test
	public void test() throws JPAException, IOException, InterruptedException {
//		this.connect();
//		this.turnoffAddress();
//		this.addAndRemoveCfg();
//		this.changeParam();
//		this.testOverrideCfg();
	}

	public void connect() throws JPAException, IOException {
		log.warn("*****************************************connect 2 address and start*************************************************");

		cfg.start(StringUtils.substringBefore(filePath, FILE_NAME));

		Connection conn1 = balancer.nextConnection();
		Connection conn2 = balancer.nextConnection();

		assertTrue(addr1.equals(conn1.toString()));
		assertTrue(addr2.equals(conn2.toString()));
	}

	public void turnoffAddress() throws JPAException, InterruptedException, IOException {
		log.warn("*****************************************turnoff one address*************************************************");

		// turnoff one address
		cfg.turnoffAddress(ip, port2);

		// so long cause MacOS, validate file watcher
		Thread.sleep(15000);

		Connection conn1 = balancer.nextConnection();
		Connection conn2 = balancer.nextConnection();
		Connection conn3 = balancer.nextConnection();

		assertTrue(addr1.equals(conn1.toString()));
		assertTrue(addr3.equals(conn2.toString()));
		assertTrue(addr1.equals(conn3.toString()));

		log.warn("*****************************************turnone above address*************************************************");

		// rollback, mock modify cfg file by hand
		this.writeFile(handoriginalCfg);

		// so long cause MacOS, validate file watcher
		Thread.sleep(15000);

		conn1 = balancer.nextConnection();
		conn2 = balancer.nextConnection();
		conn3 = balancer.nextConnection();

		assertTrue(addr2.equals(conn1.toString()));
		assertTrue(addr1.equals(conn2.toString()));
		assertTrue(addr3.equals(conn3.toString()));

	}

	public void addAndRemoveCfg() throws JPAException, IOException, InterruptedException {
		log.warn("*****************************************remove one address*************************************************");

		// 删除地址
		this.writeFile(missedCfg);

		// sleep 15 seconds
		// because JDK 7 does not yet have a native implementation of
		// WatchService for MacOS. Rather than listening for native file system
		// events, it uses the fallback sun.nio.fs.PollingWatchService, which
		// periodically traverses the file system and checks the last modified
		// timestamp of each file and subdirectory in the tree.
		Thread.sleep(15000);

		Connection conn1 = balancer.nextConnection();
		Connection conn2 = balancer.nextConnection();
		Connection conn3 = balancer.nextConnection();

		assertTrue(addr3.equals(conn1.toString()));
		assertTrue(addr2.equals(conn2.toString()));
		assertTrue(addr3.equals(conn3.toString()));

		log.warn("*****************************************add above removed address*************************************************");

		// 新增地址(恢复)
		this.writeFile(handoriginalCfg);

		// so long cause MacOS
		Thread.sleep(15000);

		conn1 = balancer.nextConnection();
		conn2 = balancer.nextConnection();
		conn3 = balancer.nextConnection();

		assertTrue(addr1.equals(conn1.toString()));
		assertTrue(addr3.equals(conn2.toString()));
		assertTrue(addr2.equals(conn3.toString()));
	}

	public void changeParam() throws JPAException, IOException, InterruptedException {
		log.warn("*****************************************modify parameter*************************************************");

		// 修改第一次
		this.writeFile(changeCfg);

		Thread.sleep(15000);

		Connection conn1 = balancer.nextConnection();
		Connection conn2 = balancer.nextConnection();
		Connection conn3 = balancer.nextConnection();

		assertTrue(addr2.equals(conn1.toString()));
		assertTrue(100 == conn1.getWriteTimeoutMillis());
		assertTrue(addr3.equals(conn2.toString()));
		assertTrue(3000 == conn2.getWriteTimeoutMillis());
		assertTrue(addr1.equals(conn3.toString()));
		assertTrue(3000 == conn3.getWriteTimeoutMillis());

		log.warn("*****************************************rollback above midify*************************************************");

		// 修改第二次(恢复)
		this.writeFile(handoriginalCfg);

		Thread.sleep(15000);

		conn1 = balancer.nextConnection();
		conn2 = balancer.nextConnection();
		conn3 = balancer.nextConnection();

		assertTrue(addr2.equals(conn1.toString()));
		assertTrue(3000 == conn1.getWriteTimeoutMillis());
		assertTrue(addr3.equals(conn2.toString()));
		assertTrue(3000 == conn2.getWriteTimeoutMillis());
		assertTrue(addr1.equals(conn3.toString()));
		assertTrue(3000 == conn3.getWriteTimeoutMillis());

	}

	public void testOverrideCfg() throws IOException, InterruptedException, JPAException {
		log.warn("*****************************************override json config*************************************************");

		cfg.overrideCfg(overrideCfg);

		Thread.sleep(15000);
		Connection conn1 = balancer.nextConnection();
		Connection conn2 = balancer.nextConnection();
		Connection conn3 = balancer.nextConnection();

		assertTrue(addr2.equals(conn1.toString()));
		assertTrue(100 == conn1.getWriteTimeoutMillis());
		assertTrue(addr3.equals(conn2.toString()));
		assertTrue(3000 == conn2.getWriteTimeoutMillis());
		assertTrue(addr1.equals(conn3.toString()));
		assertTrue(3000 == conn3.getWriteTimeoutMillis());

		log.warn("*****************************************rollback above override*************************************************");

		cfg.overrideCfg(originalCfg);

		Thread.sleep(15000);
		conn1 = balancer.nextConnection();
		conn2 = balancer.nextConnection();
		conn3 = balancer.nextConnection();

		assertTrue(addr2.equals(conn1.toString()));
		assertTrue(3000 == conn1.getWriteTimeoutMillis());
		assertTrue(addr3.equals(conn2.toString()));
		assertTrue(3000 == conn2.getWriteTimeoutMillis());
		assertTrue(addr1.equals(conn3.toString()));
		assertTrue(3000 == conn3.getWriteTimeoutMillis());
	}

	private void writeFile(String cnt) throws IOException {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filePath));
			bw.write(cnt);
		} finally {
			if (bw != null) bw.close();
		}
	}

	public void start() throws Exception {
		String xmlPath = Thread.currentThread().getContextClassLoader().getResource("applicationContext-core.xml").toString();
		CTX = new ClassPathXmlApplicationContext(xmlPath);
		cfg = CTX.getBean("connectionCfg", ConnectionCfg.class);
		balancer = CTX.getBean("loadBalancer", LoadBalancer.class);
		filePath = this.getClass().getClassLoader().getResource(FILE_NAME).getPath();

		BufferedReader reader = null;
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		StringBuilder sb4 = new StringBuilder();
		StringBuilder sb5 = new StringBuilder();
		try {
			Path file = Paths.get(filePath);
			if (Files.isReadable(file)) {
				reader = Files.newBufferedReader(file, Charset.defaultCharset());
				String line = "";
				int linenum = 0;
				while ((line = reader.readLine()) != null) {
					linenum++;
					sb1.append(line + "\n");
					if(linenum >= 3 && linenum <= 14) {
						sb2.append(line + "\n");
						sb3.append("");
						sb4.append(line + "\n");
						sb5.append(line + "\n");
					} else if (linenum == 40) {
						sb2.append("\"handreload\": \"true\"\n");
						sb3.append("\"handreload\": \"true\"\n");
						sb4.append("\"handreload\": \"true\"\n");
						sb5.append(line + "\n");
					} else if (linenum == 19) {
						sb2.append(line + "\n");
						sb3.append(line + "\n");
						sb4.append("        \"wtimeoutmillis\": 100,\n");
						sb5.append("        \"wtimeoutmillis\": 100,\n");
					} else {
						sb2.append(line + "\n");
						sb3.append(line + "\n");
						sb4.append(line + "\n");
						sb5.append(line + "\n");
					}
				}
			}
		} finally {
			if (reader != null) reader.close();
		}
		originalCfg = sb1.toString();
		handoriginalCfg = sb2.toString();
		missedCfg = sb3.toString();
		changeCfg = sb4.toString();
		overrideCfg = sb5.toString();
	}

	public void end() throws Exception {
		log.warn("*****************************************rollback cfg file*************************************************");
		this.writeFile(originalCfg);
		cfg.close();
	}

}
