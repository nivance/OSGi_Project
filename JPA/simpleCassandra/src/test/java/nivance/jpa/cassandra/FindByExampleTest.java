package nivance.jpa.cassandra;
//package nivance.jpa.cassandra;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import nivance.db.cassandra.entity.CoreRO;
//import nivance.jpa.api.JPADao;
//import nivance.jpa.bean.TestBean;
//import nivance.jpa.cassandra.core.KeyspaceManager;
//import nivance.jpa.cassandra.exception.CQLGenException;
//import nivance.serialize.SerializerUtil;
//
//@ContextConfiguration("classpath:SpringContext-Common-test.xml")
//@RunWith(SpringJUnit4ClassRunner.class)
//public class FindByExampleTest {
//
//	@Resource
//	private KeyspaceManager km;
//	
////	@Test
//	public void test(){
//		TestBean tb = new TestBean();
//		tb.setKey("key111");
//		tb.setAmount(new BigDecimal("111"));
//		tb.setPrinttime(System.currentTimeMillis());
//		JPADao dao = km.getDAO(TestBean.class);
//		try {
//		List<HashMap<String,Object>> list = dao.findByExample( (HashMap<String, Object>) SerializerUtil.serialize(tb));
//		System.out.println(list.size());
//		} catch (CQLGenException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testRO(){
//		CoreRO ro = new CoreRO();
//		ro.setMessageid("msg11");;
//		ro.setOrderno("orderno");
////		ro.setMerchantid("merchantid");
//		ro.setAmount(11L);
//		ro.setBoards(1);
//		JPADao dao = km.getDAO(CoreRO.class);
//		try {
//		dao.insert( (HashMap<String, Object>) SerializerUtil.serialize(ro));
//		List<HashMap<String, Object>> list = dao.findByExample((HashMap<String, Object>) SerializerUtil.serialize(ro));
//		System.out.println(list.size());
//		} catch (CQLGenException e) {
//			e.printStackTrace();
//		}
//	}
//}
