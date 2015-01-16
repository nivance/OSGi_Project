package nivance.impl;

import java.io.Serializable;

import nivance.dbpapi.ipojo.Jpawbp;
import nivance.impl.service.AbstractDomainDaoSupport;

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

public class JpaImplListener implements Serializable{

	private static final long serialVersionUID = -5817687224828998271L;
	
	Logger log = LoggerFactory.getLogger(JpaImplListener.class);
	

	public synchronized void onArrival(ServiceReference<?> ref) {
		String key = (String) ref.getProperty("key");
		try {
			AbstractDomainDaoSupport daoSupport = (AbstractDomainDaoSupport) ref.getBundle().getBundleContext().getService(ref);
			JpaImplFactory object = (JpaImplFactory) SpringContextUtil.getBean(JpaImplFactory.class);
			log.info("domainDataService onArrival::key [{}] class[{}] ServiceReference[{}]", new Object[]{key, object, daoSupport});
			object.getTargetDataServices().put(key, daoSupport);
		} catch (Exception e) {
			log.error("domainDataService onArrival::key [{}] failed." , e);
		}
	}

	public synchronized void onDeparture(ServiceReference<?> ref) {
		String key = (String) ref.getProperty("key");
		try {
			JpaImplFactory object = (JpaImplFactory) SpringContextUtil.getBean(JpaImplFactory.class);
			log.info("domainDataService onArrival::key [{}] class[{}] ", new Object[]{key, object});
			object.getTargetDataServices().remove(key);
		} catch (Exception e) {
			log.error("domainDataService onDeparture::key [{}] failed." , e);
		}
	}
	
	public static ComponentInstance createService(String filter) throws UnacceptableConfiguration, MissingHandlerException, ConfigurationException {
		BundleContext context = FrameworkUtil.getBundle(JpaImplFactory.class).getBundleContext();
		PrimitiveComponentType pct =  new PrimitiveComponentType().setBundleContext(context).setClassName(JpaImplListener.class.getName())
		.addHandler(new Jpawbp().onArrival("onArrival").onDeparture("onDeparture").setFilter("("+filter+")"));
		ComponentInstance proxyinstance =pct.createInstance();
		return proxyinstance;
	}

}
