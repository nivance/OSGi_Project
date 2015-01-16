package nivance.jpa.redis.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

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
public class LoadBalancerTest {

	private static String ip = "192.168.100.12";
	private static String addr1 = "[Connection host=" + ip + ", port=7379]";
	private static String addr2 = "[Connection host=" + ip + ", port=7380]";

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
//		end();
	}

	@Test
	public void test() throws IOException, InterruptedException, JPAException {
		// hangUp与heartBeat不能同时测试
//		this.hangUp();
	}

	public void hangUp() throws InterruptedException, JPAException {
		log.warn("*****************************************retry success*************************************************");

		Connection conn1 = balancer.nextConnection();
		Connection conn2 = balancer.nextConnection();

		// 需要手工停止redis服务 port=7379
		balancer.hangUp(conn1);

		Thread.sleep(30000);

		// 休眠时间内需要手工启动redis服务 port=7379
		conn1 = balancer.nextConnection();
		conn2 = balancer.nextConnection();

		assertTrue(addr2.equals(conn1.toString()));
		assertTrue(addr1.equals(conn2.toString()));

		log.warn("*****************************************retry failed*************************************************");

		// 需要手工停止redis服务 port=7379
		balancer.hangUp(conn2);

		Thread.sleep(100000);

		conn1 = balancer.nextConnection();
		conn2 = balancer.nextConnection();

		assertTrue(addr2.equals(conn1.toString()));
		assertTrue(addr2.equals(conn2.toString()));
	}

	public void start() {
		String xmlPath = Thread.currentThread().getContextClassLoader().getResource("applicationContext-core.xml").toString();
		CTX = new ClassPathXmlApplicationContext(xmlPath);
		cfg = CTX.getBean("connectionCfg", ConnectionCfg.class);
		balancer = CTX.getBean("loadBalancer", LoadBalancer.class);
		filePath = this.getClass().getClassLoader().getResource(FILE_NAME).getPath();
		cfg.start(StringUtils.substringBefore(filePath, FILE_NAME));
	}

	public void end() {
		cfg.close();
	}
}
