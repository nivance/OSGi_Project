package nivance.jpaclient.test.demo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.api.DomainDaoSupport;
import nivance.dbpapi.domain.DomainController;
import nivance.dbpapi.spring.JpaProxyFactory;
import nivance.jpaclient.test.cass.entity.CoreBO;
import nivance.serialize.SerializerUtil;

//@Component
@Slf4j
public class InsertPresure {

	public AtomicLong successNum = new AtomicLong();
	public AtomicLong faildNum = new AtomicLong();
	@Resource(name="jpaProxyFactory") private JpaProxyFactory jpaProxyFactory;
	
	public static boolean STOP = false;
	@PostConstruct
	public void test() throws InterruptedException {
		int threadnum = 100;
		for(int i=0;i<threadnum;i++){
			InsertThread jpaLockAndUpdate = new InsertThread(jpaProxyFactory);
			jpaLockAndUpdate.start();
			
		}
		int timeSeconde = 1000;
		int sleepTime = 10;
		int time = 0;
		while(true){
			TimeUnit.SECONDS.sleep(sleepTime);
			time = time + sleepTime;
			log.warn("Thread["+threadnum+"]"+"time["+time+"] count["+successNum.get()+"] error["+faildNum.get()+"] TPS["+(successNum.get()/time)+"]");
			if(time>=timeSeconde){
				break;
			}
		}
		log.warn("Thread["+threadnum+"]"+"time["+time+"] count["+successNum.get()+"] error["+faildNum.get()+"] TPS["+(successNum.get()/time)+"]");
	}
	
	class InsertThread extends Thread {
		private JpaProxyFactory jpaProxyFactory;
		public InsertThread(JpaProxyFactory jpaProxyFactory) {
			this.jpaProxyFactory = jpaProxyFactory;
		}
		public void run() {
			DomainController domain = new DomainController();
			domain.setNames("Award_20140502");
			domain.setTarget("mysql");
			while(!STOP){
				try {
					CoreBO data = new CoreBO();
					data.setCommand("corebo");
					jpaProxyFactory.getDomainDaoSupport().insert(domain, SerializerUtil.serialize(DomainDaoSupport.type, data));
					successNum.incrementAndGet();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
