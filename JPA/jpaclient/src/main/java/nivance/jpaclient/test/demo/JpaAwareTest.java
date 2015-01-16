package nivance.jpaclient.test.demo;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
public class JpaAwareTest {

	private static Logger log = LoggerFactory
			.getLogger(JpaAwareTest.class); 
	
//	@Resource(name="useJpaAwareWagerTest")
//	private UseJpaAwareWagerTest wagerAware;
//	
	@Resource(name = "useJpaAwareCassTest")
	private UseJpaAwareCassTest cassAware;
//	@Resource(name = "useJpaAwareISOTest")
//	private UseJpaAwareISOTest isoMysqlAware;
	
	
//	@PostConstruct
	public void init() {
		log.warn("JpaAwareTest...start");
//		springBeanTest();
		cassAware.doTest();
//		wagerAware.doTest();
//		isoMysqlAware.doTest();
	}
}
