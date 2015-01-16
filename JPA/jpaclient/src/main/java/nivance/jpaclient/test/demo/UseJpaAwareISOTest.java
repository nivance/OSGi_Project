package nivance.jpaclient.test.demo;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import nivance.dbpapi.api.DomainDaoSupport;
import nivance.dbpapi.domain.DomainController;
import nivance.dbpapi.exception.JPAException;
import nivance.dbpapi.spring.JpaProxyFactory;
import nivance.jpaclient.test.cass.entity.CoreBO;
import nivance.serialize.SerializerUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component("useJpaAwareISOTest")
public class UseJpaAwareISOTest{

	@Resource(name="jpaProxyFactory")
	private JpaProxyFactory jpaProxyFactory;
	private static Logger log = LoggerFactory
			.getLogger(UseJpaAwareISOTest.class);


	@PostConstruct
	public void doTest() {
		log.warn("UseJpaAwareISOTest...start");
		log.info("UseJpaAwareISOTest is not null: " + (jpaProxyFactory.getDomainDaoSupport() != null));
		insert();
		count();
	}
	
	private void insert() {
		DomainController domain = new DomainController();
		domain.setNames("prizelevel");
		domain.setTarget("cassandra");
		
		try {
			CoreBO info =  new CoreBO();
			info.setLtype("SLTO");
			jpaProxyFactory.getDomainDaoSupport().insert(domain, SerializerUtil.serialize(DomainDaoSupport.type, info));
		} catch (Throwable e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void count() {
		DomainController domain = new DomainController();
		domain.setNames("prizelevel");
		domain.setTarget("cassandra");
		try {
			Object count =jpaProxyFactory.getDomainDaoSupport().count(domain,null);
			log.info("[useJpaAwareMysqlTest][count]:" + count);
		} catch (JPAException e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	public void stop() {
		log.info("useJpaAwareMysqlTest: stop");
	}

}
