package nivance.jpa.cassandra;
//package nivance.jpa.cassandra;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import lombok.extern.slf4j.Slf4j;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.google.common.collect.Sets;
//import nivance.jpa.api.JPADao;
//import nivance.jpa.bean.TestBean;
//import nivance.jpa.cassandra.core.KeyspaceManager;
//import nivance.jpa.cassandra.exception.CQLGenException;
//
//@Slf4j
//@ContextConfiguration("classpath:SpringContext-Common-test.xml")
//@RunWith(SpringJUnit4ClassRunner.class)
//public class SpringTest{
//
//	@Resource
//	private KeyspaceManager km ;
//	private JPADao dao = null;
//	
//	@Before
//	public void before(){
//		dao = km.getDAO(TestBean.class);
//	}
//	
//	public Object doBySQL(String sql) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	//@Test
//	public void insert() {
//		TestBean info = new TestBean();
//		info.setAmount(new BigDecimal("100"));
//		info.setKey("test1");
//		info.setPrinttime(System.currentTimeMillis());
//		info.setNset(Sets.newHashSet("a"));
//		info.setOrs(Lists.newArrayList("1"));
//		HashMap<String, Object> hb = Maps.newHashMap();
//		hb.put("amount", new BigDecimal("100"));
//		hb.put("printtime", System.currentTimeMillis());
//		hb.put("nset", Sets.newHashSet("a"));
//		hb.put("ors", Lists.newArrayList("1"));
//		hb.put("key", "test1");
//		dao.insert(hb);
//	}
//
//	//@Test
//	public void findOne() {
//		HashMap<String, Object> hb = Maps.newHashMap();
//		hb.put("key", "test1");
//		HashMap<String, Object> result = dao.findOne(hb);
//		log.info("findOne::"+result.get("printtime"));
//	}
//	
//	//@Test
//	public void insertIfNotExist() {
//		HashMap<String, Object> hb = Maps.newHashMap();
//		hb.put("amount", new BigDecimal("100"));
//		hb.put("printtime", System.currentTimeMillis());
//		hb.put("nset", Sets.newHashSet("a"));
//		hb.put("ors", Lists.newArrayList("1"));
//		hb.put("key", "test1");
//		dao.insertIfNotExist(hb);
//	}
//	
//	//@Test
//	public void exists() {
//		HashMap<String, Object> hb = Maps.newHashMap();
//		hb.put("key", "test1");
//		log.info("exists::"+dao.exists(hb));
//	}
//
//	//@Test
//	public void findAll() {
//		List<HashMap<String,Object>> list = dao.findAll();
//		for(Map<String,Object> map : list){
//			log.info("findAll::"+map);
//		}
//	}
//	
//	//@Test
//	public void insertBatch() {
//		HashMap<String, Object> hb = Maps.newHashMap();
//		hb.put("amount", new BigDecimal("100"));
//		hb.put("printtime", System.currentTimeMillis());
//		hb.put("nset", Sets.newHashSet("a"));
//		hb.put("ors", Lists.newArrayList("1"));
//		hb.put("key", "test1");
//		log.info("insertBatch::"+dao.insertBatch(Lists.newArrayList(hb)));
//	}
//
//	//@Test
//	public void insertBatchIfNotExist() {
//		HashMap<String, Object> hb = Maps.newHashMap();
//		hb.put("amount", new BigDecimal("100"));
//		hb.put("printtime", System.currentTimeMillis());
//		hb.put("nset", Sets.newHashSet("a"));
//		hb.put("ors", Lists.newArrayList("1"));
//		hb.put("key", "test1");
//		log.info("insertBatch::"+dao.insertBatchIfNotExist(Lists.newArrayList(hb)));
//	}
//
//	@Test
//	public void update() {
//		HashMap<String, Object> hb = Maps.newHashMap();
//		hb.put("amount", new BigDecimal("1000"));
//		hb.put("key", "test1");
//		log.info("update::"+dao.update(hb));
//	}
//
//	@Test
//	public void updateList() {
//		HashMap<String, Object> hb = Maps.newHashMap();
//		hb.put("amount", new BigDecimal("122"));
//		hb.put("key", "test1");
//		log.info("update::"+dao.update(Lists.newArrayList(hb)));
//	}
//
//	//@Test
//	public void findAllBykey() {
//		HashMap<String, Object> hb = Maps.newHashMap();
//		hb.put("key", "test1");
//		log.info("findAllBykey::"+dao.findAll(Lists.newArrayList(hb)));
//	}
//
//	//@Test
//	public void count() {
//		log.info("count::"+dao.count());
//	}
//
//	//@Test
//	public void countByKey() {
//		HashMap<String, Object> hb = Maps.newHashMap();
//		hb.put("key", "test1");
//		log.info("countByKey::"+dao.count(hb));
//	}
//
//	//@Test
//	public void delete() {
//		HashMap<String, Object> hb = Maps.newHashMap();
//		hb.put("key", "test1");
//		log.info("delete::"+dao.delete(hb));
//	}
//
//	//@Test
//	public void deleteIfExist() {
//		HashMap<String, Object> hb = Maps.newHashMap();
//		hb.put("key", "test1");
//		log.info("deleteIfExist::"+dao.deleteIfExist(hb));
//	}
//
//	//@Test
//	public void deleteAllByKey() {
//		HashMap<String, Object> hb = Maps.newHashMap();
//		hb.put("key", "test1");
//		log.info("deleteAllByKey::"+dao.deleteAll(Lists.newArrayList(hb)));
//	
//	}
//
//	//@Test
//	public void deleteAllIfExist() {
//		HashMap<String, Object> hb = Maps.newHashMap();
//		hb.put("key", "test1");
//		log.info("deleteAllIfExist::"+dao.deleteAllIfExist(Lists.newArrayList(hb)));
//	}
//
//	
//	//@Test
//	public void deleteAll() {
//		log.info("deleteAll::"+dao.deleteAll());
//	}
//
////	//@Test
//	public void truncate() {
//		log.info("truncate::"+dao.truncate());
//	}
//
//	//@Test 
//	public void findByExample() {
//		HashMap<String, Object> hb = Maps.newHashMap();
//		hb.put("key", "test1");
//		try {
//			log.info("findByExample::"+dao.findByExample(hb));
//		} catch (CQLGenException e) {
//			e.printStackTrace();
//		}
//	}
//	
//}
