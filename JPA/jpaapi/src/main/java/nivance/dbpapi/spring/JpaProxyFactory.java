package nivance.dbpapi.spring;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.Getter;
import lombok.Setter;
import nivance.dbpapi.api.DomainDaoSupport;
import nivance.dbpapi.ipojo.JpaAwareListener;

import org.apache.felix.ipojo.ComponentInstance;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class JpaProxyFactory implements ApplicationContextAware{

	static Logger log = LoggerFactory.getLogger(JpaProxyFactory.class);
	private static List<ComponentInstance> pojoInstances = new ArrayList<>();
	@Getter @Setter
	public DomainDaoSupport domainDaoSupport;
	
	@PostConstruct
	public void injectJpaAware(){
		try {
			pojoInstances.add(JpaAwareListener.createService());
			ServiceReference<DomainDaoSupport> sref = (ServiceReference<DomainDaoSupport>) FrameworkUtil.getBundle(this.getClass()).getBundleContext()
					.getServiceReference(DomainDaoSupport.class);
			if(sref!=null){
				domainDaoSupport = FrameworkUtil.getBundle(this.getClass()).getBundleContext().getService(sref);
			}
		} catch (Exception e) {
			throw new RuntimeException("JPA白板注入失败..",e);
		}
	}
	
	@PreDestroy
	public void destory() {
		for (ComponentInstance instance : pojoInstances) {
			instance.dispose();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		JpaProxyFactory.ctx = applicationContext;
	}

	public static Object getBean(String name) {
		return ctx.getBean(name);
	}

	public static <T> Object getBean(Class<T> clzz) {
		return ctx.getBean(clzz);
	}
	
	private static ApplicationContext ctx;
}
