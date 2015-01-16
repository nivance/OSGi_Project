package nivance.fundimpl.service.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.api.DomainDaoSupport;
import nivance.fundimpl.service.JPAService;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.apache.felix.ipojo.whiteboard.Whiteboards;
import org.osgi.framework.ServiceReference;

@Slf4j
@Component
@Provides
@Instantiate
@Whiteboards(whiteboards = { @Wbp(filter = "(key=domainDaoSupport)", onArrival = "onArrival", onDeparture = "onDeparture") })
public class DataPersistencer implements JPAService {
	
	@Getter @Setter private DomainDaoSupport daoSupport;
	
	public synchronized void onArrival(ServiceReference<?> ref) {
		daoSupport = (DomainDaoSupport) ref.getBundle().getBundleContext().getService(ref);
		log.debug("###########jpa onArrivals#############" + daoSupport);
	}
	
	public synchronized void onDeparture(ServiceReference<?> ref) {
		daoSupport = null;
		log.debug("###########jpa onDepartures#############");
	}
	
}
