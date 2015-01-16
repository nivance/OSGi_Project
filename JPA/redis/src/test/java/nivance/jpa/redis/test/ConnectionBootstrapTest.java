package nivance.jpa.redis.test;

import static org.junit.Assert.assertTrue;
import lombok.extern.slf4j.Slf4j;
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
public class ConnectionBootstrapTest {

	private static String IP = "192.168.100.12";
	private static String addr1 = "[Connection host=" + IP + ", port=7379]";
	private static String addr2 = "[Connection host=" + IP + ", port=7380]";

	private static ApplicationContext CTX;
	private static final String FILE_NAME = "connection.cfg";
	private ConnectionCfg cfg = null;
	private LoadBalancer balancer = null;
	private String filePath = null;

	@Before
	public void setUp() throws Exception {
//		start();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
//		openclose();
	}

	public void openclose() {
		log.warn("*****************************************setup*************************************************");
		cfg.start(StringUtils.substringBefore(filePath, FILE_NAME));

		Connection conn1 = balancer.nextConnection();
		Connection conn2 = balancer.nextConnection();

		assertTrue(addr1.equals(conn1.toString()));
		assertTrue(addr2.equals(conn2.toString()));

		log.warn("*****************************************tearDown*************************************************");
		cfg.close();
		try {
			balancer.destroy();
		} catch (Exception e) {
			log.error("close error.", e);
		}
	}

	public void start() {
		String xmlPath = Thread.currentThread().getContextClassLoader().getResource("applicationContext-core.xml").toString();
		CTX = new ClassPathXmlApplicationContext(xmlPath);
		cfg = CTX.getBean("connectionCfg", ConnectionCfg.class);
		balancer = CTX.getBean("loadBalancer", LoadBalancer.class);
		filePath = this.getClass().getClassLoader().getResource(FILE_NAME).getPath();
	}
}
