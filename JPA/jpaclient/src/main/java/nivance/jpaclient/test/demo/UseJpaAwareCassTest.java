package nivance.jpaclient.test.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import nivance.dbpapi.api.DomainDaoSupport;
import nivance.dbpapi.domain.ControlPolicy;
import nivance.dbpapi.domain.DomainController;
import nivance.dbpapi.exception.JPAException;
import nivance.dbpapi.spring.JpaProxyFactory;
import nivance.jpaclient.test.cass.entity.CoreBO;
import nivance.serialize.SerializerUtil;
import nivance.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//extends JpaProxyFactory 
//@Component("useJpaAwareCassTest")
public class UseJpaAwareCassTest {

	@Resource(name="jpaProxyFactory")
	private JpaProxyFactory jpaProxyFactory;
	private static Logger log = LoggerFactory
			.getLogger(UseJpaAwareCassTest.class);


//	 @PostConstruct
	public void doTest() {
		 log.warn("UseJpaAwareCassTest...start");
		 log.warn("UseJpaAwareCassTest...jpaProxyFactory::"+jpaProxyFactory);
		log.info("useJpaAwareCassTest is not null: "
				+ (jpaProxyFactory.getDomainDaoSupport() != null));
		test();
		insert();
		count();
		findByExample();
		insertBO();
		findByExample();
		findByKey();
		update();
		batchUpdate();
		findAll();
		batchInsert();
		exists();
		doSqlTest();
		insertBOS();
		selectBOS();
	}
	
	private void insertBO() {
		DomainController domain = new DomainController();
		domain.setNames("corebo");
		domain.setTarget("wager");
		try {
			//CoreBO(ltype=ltype1, period=period, merchantid=merchantid, messageid=messageid, caskey=caskey, isvalid=1, region=regoin, listF=[list, Test], setF=[Test, set], mapF={key=value})
			CoreBO bo = new CoreBO();
			bo.setLtype("ltype");
			bo.setPeriod("period");
			bo.setMerchantid("merchantid");
			bo.setMessageid("messageid");
			@SuppressWarnings("unused")
			Object object = jpaProxyFactory.getDomainDaoSupport().insert(domain,SerializerUtil.serialize(DomainDaoSupport.type, bo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void findByKey() {
		DomainController domain = new DomainController();
		domain.setNames("corebo");
		domain.setTarget("wager");
		try {
			//CoreBO(ltype=ltype1, period=period, merchantid=merchantid, messageid=messageid, caskey=caskey, isvalid=1, region=regoin, listF=[list, Test], setF=[Test, set], mapF={key=value})
			CoreBO bo = new CoreBO();
			bo.setLtype("ltype");
			bo.setPeriod("period");
			bo.setMerchantid("merchantid");
			bo.setMessageid("messageid");
			Object object = jpaProxyFactory.getDomainDaoSupport().findOne(domain,SerializerUtil.serialize(DomainDaoSupport.type, bo));
			if(object!=null){
				CoreBO result = SerializerUtil.deserialize(DomainDaoSupport.type, (byte[]) object, CoreBO.class);
				log.info("[useJpaAwareCassTest][findByKey]::{}",JsonUtil.bean2Json(result));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<CoreBO> getBOS(){
		CoreBO bo = new CoreBO();
		bo.setLtype("QGSLTO");
		bo.setPeriod("2012001");
		bo.setMessageid("messageid1");
		bo.setMerchantid("test");
		
		CoreBO bo1 = new CoreBO();
		bo1.setLtype("QGSLTO");
		bo1.setPeriod("2012001");
		bo1.setMessageid("messageid2");
		bo1.setMerchantid("test");
		
		List<CoreBO> bos = new ArrayList<>();
		bos.add(bo1);
		bos.add(bo1);
		return bos;
	}
	private void insertBOS(){
		try {
			
			DomainController domain = new DomainController();
			domain.setNames("corebo");
			domain.setTarget("cassandra");
			jpaProxyFactory.getDomainDaoSupport().batchInsert(domain, SerializerUtil.serializeArray(DomainDaoSupport.type, getBOS()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private void selectBOS(){
		try {
			DomainController domain = new DomainController();
			domain.setNames("corebo");
			domain.setTarget("cassandra");
			Object obj = jpaProxyFactory.getDomainDaoSupport().findAll(domain, SerializerUtil.serializeArray(DomainDaoSupport.type, getBOS()));
			if(obj!=null){
				List<CoreBO> result = SerializerUtil.deserializeArray(DomainDaoSupport.type, (byte[])obj, CoreBO.class);
				log.info("selectBOS::"+JsonUtil.list2Json(result));
			}else{
				log.info("selectBOS::null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void test() {
		try {
			CoreBO ro = new CoreBO();
			ro.setLtype("QGSLTO");
			ro.setMerchantid("test");
			Object bytes = SerializerUtil.serialize(DomainDaoSupport.type, ro);
			Object object = SerializerUtil.deserialize(DomainDaoSupport.type, bytes, CoreBO.class);
			System.out.println(object.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void delete() {
		DomainController domain = new DomainController();
		domain.setNames("CoreBO");
		domain.setTarget("cassandra");
		CoreBO ro = new CoreBO();
		try {
			jpaProxyFactory.getDomainDaoSupport().delete(domain, SerializerUtil.serialize(DomainDaoSupport.type, ro));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void exists() {
		DomainController domain = new DomainController();
		domain.setNames("CoreBO");
		domain.setTarget("cassandra");
		CoreBO ro = new CoreBO();
		ro.setMessageid("orderno1");
		try {
			boolean obj = (boolean) jpaProxyFactory.getDomainDaoSupport().exists(domain,
					SerializerUtil.serialize(DomainDaoSupport.type, ro));
			log.info("[useJpaAwareCassTest][exists] " + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void doSqlTest() {
		DomainController domain = new DomainController();
		domain.setNames("CoreBO");
		domain.setTarget("cassandra");
		domain.setFrom("[FUND]");
		domain.setPolicy(ControlPolicy.ASYN);
		try {
			Object obj = jpaProxyFactory.getDomainDaoSupport().doBySQL(domain, "select * from t_core_ro");
			if (obj == null) {
				log.info("[useJpaAwareCassTest][doSqlTest] result is null");
			} else {
				@SuppressWarnings("rawtypes")
				List<HashMap> list = JsonUtil.json2List(
						new String((byte[]) obj), HashMap.class);
				log.info("[useJpaAwareCassTest][doSqlTest]"
						+ new String((byte[]) obj));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void batchInsert() {
		// String domain = "corevo";
		DomainController domain = new DomainController();
		domain.setNames("corevo");
		domain.setTarget("cassandra");
		try {
			List<CoreBO> list = new ArrayList<>();
			CoreBO vo = new CoreBO();
			vo.setPeriod("");
			vo.setIsvalid(1);;
			list.add(vo);
			jpaProxyFactory.getDomainDaoSupport().batchInsert(domain,
					SerializerUtil.serializeArray(DomainDaoSupport.type, list));
			log.info("[useJpaAwareCassTest][batchInsert]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void count() {
		DomainController domain = new DomainController();
		domain.setNames("award");
		domain.setTarget("cassandra");
		try {
			long count = (long) jpaProxyFactory.getDomainDaoSupport().count(domain, null);
			log.info("[useJpaAwareCassTest][count]:" + count);
		} catch (JPAException e) {
			e.printStackTrace();
		}
	}

	private void insert() {
		try {
			CoreBO ro = new CoreBO();
			ro.setLtype("QGSLTO");
			ro.setPeriod("2012001");
			ro.setMessageid("messageid1");
			ro.setMerchantid("test");
			DomainController domain = new DomainController();
			domain.setNames("CoreBO");
			domain.setTarget("cassandra");
			jpaProxyFactory.getDomainDaoSupport().insert(domain, SerializerUtil.serialize(DomainDaoSupport.type, ro));
			log.info("[useJpaAwareCassTest][insert]:" + JsonUtil.bean2Json(ro));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private void update() {
		try {
			CoreBO ro = new CoreBO();
			ro.setMessageid("messageid1");
			DomainController domain = new DomainController();
			domain.setNames("CoreBO");
			domain.setTarget("cassandra");
			jpaProxyFactory.getDomainDaoSupport().update(domain, SerializerUtil.serialize(DomainDaoSupport.type, ro));
			log.info("[useJpaAwareCassTest][update]:" + JsonUtil.bean2Json(ro));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private void findByExample() {
		DomainController domain = new DomainController();
		domain.setNames("corebo");
		domain.setTarget("wager");
		try {
			CoreBO bo = new CoreBO();
			Object object = jpaProxyFactory.getDomainDaoSupport().findByExample(domain,
					SerializerUtil.serialize(DomainDaoSupport.type, bo));
			if(object!=null){
				List<CoreBO> bos = SerializerUtil.deserializeArray(DomainDaoSupport.type, (byte[]) object, CoreBO.class);
				log.info("[useJpaAwareCassTest][findByExample]{}",JsonUtil.list2Json(bos));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void batchUpdate() {
		DomainController domain = new DomainController();
		domain.setNames("CoreBO");
		domain.setTarget("cassandra");
		try {
			List<CoreBO> list = new ArrayList<>();
			CoreBO ro = new CoreBO();
			ro.setMessageid("orderno1");
			list.add(ro);
			Object object = jpaProxyFactory.getDomainDaoSupport().batchUpdate(domain,
					SerializerUtil.serializeArray(DomainDaoSupport.type, list));
			log.info("[useJpaAwareCassTest][batchUpdate]"
					+ JsonUtil.list2Json(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void findAll() {
		DomainController domain = new DomainController();
		domain.setNames("CoreBO");
		domain.setTarget("cassandra");
		try {
			List<CoreBO> list = new ArrayList<>();
			CoreBO ro = new CoreBO();
			list.add(ro);
			Object object = jpaProxyFactory.getDomainDaoSupport().findAll(domain,
					SerializerUtil.serializeArray(DomainDaoSupport.type, list));
			log.info("[useJpaAwareCassTest][findAll]" + object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	public void stop() {
		log.info("useJpaAwareCassTest: stop");
	}

}
