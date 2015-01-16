package nivance.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.Getter;
import lombok.Setter;
import nivance.impl.service.AbstractDomainDaoSupport;

import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.ConfigurationException;
import org.apache.felix.ipojo.MissingHandlerException;
import org.apache.felix.ipojo.UnacceptableConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JpaImplFactory {

	Logger log = LoggerFactory.getLogger(JpaImplFactory.class);
	private List<ComponentInstance> pojoInstances = new ArrayList<>();
	
	@Getter
	@Setter
	private Map<String, AbstractDomainDaoSupport> targetDataServices = new ConcurrentHashMap<String, AbstractDomainDaoSupport>();

	@PostConstruct
	void injectJpaAware() {
		try {
			pojoInstances.add(JpaImplListener.createService(buildFilter("mysql")));
			pojoInstances.add(JpaImplListener.createService(buildFilter("cassandra")));
			pojoInstances.add(JpaImplListener.createService(buildFilter("wager")));
			pojoInstances.add(JpaImplListener.createService(buildFilter("redis")));
			pojoInstances.add(JpaImplListener.createService(buildFilter("oracle")));
		} catch (UnacceptableConfiguration | MissingHandlerException
				| ConfigurationException e) {
			log.warn("JPAImpl writeborad inject error..",e);
		}
	}
	
	private String buildFilter(String name){
		return new StringBuilder("key=").append(name).toString();
	}
	
	@PreDestroy
	public void destory() {
		for (ComponentInstance instance : pojoInstances) {
			instance.dispose();
		}
	}
}
