package nivance.jpa.redis.test.dao;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nivance.dbpapi.exception.JPAException;
import nivance.jpa.redis.ConnectionCfg;
import nivance.jpa.redis.bean.GameCodeInfo;
import nivance.jpa.redis.dao.GameCodeInfoDao;
import nivance.serialize.SerializerFactory;
import nivance.serialize.SerializerFactory.Type;
import nivance.serialize.SerializerUtil;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GameCodeInfoDaoTest {

	private static ApplicationContext CTX;
	private ConnectionCfg cfg = null;
	private static final String FILE_NAME = "connection.cfg";
	private final String GAME_CODE = "T0001";
	private final String GAME_NAME = "测试游戏";
	private Type type = SerializerFactory.Type.JSON;

	@Before
	public void setUp() throws Exception {
//		start();
	}

	@After
	public void tearDown() throws Exception {
//		end();
	}

	@Test
	public void test() throws IOException, JPAException {
//		this.insert();
//		this.findOne();
//		this.findAll();
//		this.count();
//		this.delete();
	}

	public void insert() {
		GameCodeInfoDao dao = CTX.getBean("gameCodeInfoDao", GameCodeInfoDao.class);
		GameCodeInfo gci = new GameCodeInfo();
		gci.setGamecode(GAME_CODE);
		gci.setGamename(GAME_NAME);
		gci.setDatacreatetime(System.currentTimeMillis());
		
		Object bs = SerializerUtil.serialize(type, gci);
		dao.batchInsert(bs);
	}

	public void findOne() throws JPAException, IOException {
		GameCodeInfoDao dao = CTX.getBean("gameCodeInfoDao", GameCodeInfoDao.class);
		
		GameCodeInfo gci = new GameCodeInfo();
		gci.setGamecode(GAME_CODE);
		gci.setGamename(GAME_NAME);
		
		Object bs = dao.findOne(SerializerUtil.serialize(type, gci));
		gci = SerializerUtil.deserialize(type, bs, GameCodeInfo.class);
		
		assertTrue(GAME_CODE.equals(gci.getGamecode()));
		assertTrue(GAME_NAME.equals(gci.getGamename()));
	}

	public void findAll() throws JPAException, IOException {
		GameCodeInfoDao dao = CTX.getBean("gameCodeInfoDao", GameCodeInfoDao.class);
		
		List<GameCodeInfo> gci = new ArrayList<GameCodeInfo>();
		
		List<Object> bs = dao.findAll(null);
		for(Object b : bs) {
			gci.add(SerializerUtil.deserialize(type, b, GameCodeInfo.class));
		}
		
		assertTrue(GAME_CODE.equals(gci.get(0).getGamecode()));
		assertTrue(GAME_NAME.equals(gci.get(0).getGamename()));
	}

	public void count() throws JPAException, IOException {
		GameCodeInfoDao dao = CTX.getBean("gameCodeInfoDao", GameCodeInfoDao.class);
		
		GameCodeInfo gci = new GameCodeInfo();
		Object bs = SerializerUtil.serialize(type, gci);
		long size = dao.count(bs);
		
		assertTrue(1 == size);
	}

	public void delete() throws JPAException, IOException {
		GameCodeInfoDao dao = CTX.getBean("gameCodeInfoDao", GameCodeInfoDao.class);
		
		GameCodeInfo gci = new GameCodeInfo();
		gci.setGamecode(GAME_CODE);
		gci.setGamename(GAME_NAME);
		
		Object bs = SerializerUtil.serialize(type, gci);
		dao.deleteOne(bs);
	}

	public void start() throws Exception {
		String xmlPath = Thread.currentThread().getContextClassLoader().getResource("applicationContext-core.xml").toString();
		CTX = new ClassPathXmlApplicationContext(xmlPath);

		String dir = this.getClass().getClassLoader().getResource(FILE_NAME).getPath();
		dir = StringUtils.substringBefore(dir, FILE_NAME);
		
		cfg = CTX.getBean("connectionCfg", ConnectionCfg.class);
		cfg.start(dir);
	}

	public void end() throws Exception {
		cfg.close();
	}

}
