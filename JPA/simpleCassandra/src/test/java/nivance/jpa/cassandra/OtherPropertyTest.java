package nivance.jpa.cassandra;
//package nivance.jpa.cassandra;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//
//import javax.annotation.Resource;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Sets;
//import nivance.jpa.api.JPADao;
//import nivance.jpa.bean.TestBean;
//import nivance.jpa.cassandra.core.KeyspaceManager;
//import nivance.serialize.SerializerUtil;
//
//@ContextConfiguration("classpath:SpringContext-Common-test.xml")
//@RunWith(SpringJUnit4ClassRunner.class)
//public class OtherPropertyTest {
//
//	@Resource
//	private KeyspaceManager km;
//	
////	@Test
//	public void BigDecimalTest(){
//		try {
//			JPADao dao = km.getDAO(TestBean.class);
//			TestBean bean = new TestBean();
//			bean.setKey("key1");
//			bean.setPrinttime(System.currentTimeMillis());
//			bean.setAmount(new BigDecimal("100"));
//			HashMap<String,Object> mb = (HashMap<String,Object>) SerializerUtil.serialize(bean);
//			dao.insert(mb);
//			HashMap<String,Object> result = dao.findOne(mb);
//			System.out.println(result.get("amount"));
//			System.out.println(result.get("amount").getClass());
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void ListTest(){
//		try {
//			JPADao dao = km.getDAO(TestBean.class);
//			TestBean bean = new TestBean();
//			bean.setKey("key1");
//			bean.setOrs(Lists.newArrayList("123"));
//			bean.setNset(Sets.newHashSet("abc"));
//			HashMap<String,Object> mb = (HashMap<String,Object>) SerializerUtil.serialize(bean);
//			//
//			mb.put("nset", Sets.newHashSet("abc"));
//			dao.insert(mb);
//			HashMap<String,Object> result = dao.findOne(mb);
//			System.out.println(result.get("ors"));
//			System.out.println(result.get("nset"));
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
////	@Test
////	public void SetTest(){
////		try {
////			JPADao dao = km.getDAO(TestBean.class);
////			TestBean bean = new TestBean();
////			bean.setKey("key1");
////			Set<String> nset = Sets.newHashSet();
////			bean.setNset((HashSet<String>) nset);
////			HashMap<String,Object> mb = (HashMap<String,Object>) SerializerUtil.serialize(bean);
////			dao.insert(mb);
////			HashMap<String,Object> result = dao.findOne(mb);
////			System.out.println(result.getValueByName("nset").getClass());
////		} catch (Throwable e) {
////			e.printStackTrace();
////		}
////	}
//}
