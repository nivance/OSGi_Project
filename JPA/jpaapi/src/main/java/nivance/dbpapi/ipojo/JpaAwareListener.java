package nivance.dbpapi.ipojo;

import java.io.Serializable;

import nivance.dbpapi.api.DomainDaoSupport;
import nivance.dbpapi.spring.JpaProxyFactory;

import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.ConfigurationException;
import org.apache.felix.ipojo.MissingHandlerException;
import org.apache.felix.ipojo.UnacceptableConfiguration;
import org.apache.felix.ipojo.api.PrimitiveComponentType;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JpaAwareListener implements Serializable{

	private static final long serialVersionUID = -5817687224828998271L;
	
	Logger log = LoggerFactory.getLogger(JpaAwareListener.class);
	
	
	public synchronized void onArrival(ServiceReference<?> ref) {
		try {
			JpaProxyFactory jpaProxyFactory = (JpaProxyFactory) JpaProxyFactory.getBean(JpaProxyFactory.class);
			log.debug("Jpa onArrival::class [{JpaProxyFactory}] object[{}] ServiceReference[{}]", new Object[]{ jpaProxyFactory,ref});
			DomainDaoSupport daoSupport = (DomainDaoSupport) ref.getBundle().getBundleContext().getService(ref);
			jpaProxyFactory.setDomainDaoSupport(daoSupport);
		} catch (Exception e) {
			log.warn("JPA onArrival error..",e);
		}
	}

	public synchronized void onDeparture(ServiceReference<?> ref) {
		try {
			JpaProxyFactory jpaProxyFactory = (JpaProxyFactory) JpaProxyFactory.getBean(JpaProxyFactory.class);
			log.debug("Jpa onArrival::class [{JpaProxyFactory}] object[{}] ServiceReference[{}]", new Object[]{ jpaProxyFactory,ref});
			jpaProxyFactory.setDomainDaoSupport(null);
		} catch (Exception e) {
			log.warn("JPA onDeparture error..",e);
		}
	}
	
	public static ComponentInstance createService() throws UnacceptableConfiguration, MissingHandlerException, ConfigurationException {
		BundleContext context = FrameworkUtil.getBundle(JpaProxyFactory.class).getBundleContext();
		ComponentInstance proxyinstance = new PrimitiveComponentType().setBundleContext(context).setClassName(JpaAwareListener.class.getName())
				.addHandler(new Jpawbp().onArrival("onArrival").onDeparture("onDeparture").setFilter("(key=domainDaoSupport)"))
				.createInstance();
		return proxyinstance;
	}

}
