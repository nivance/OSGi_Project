package nivance.fund.spring;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.Getter;
import lombok.Setter;
import nivance.fund.annotataion.FundAware;
import nivance.fund.api.FundSupport;
import nivance.fund.ipojo.FundAwareListener;

import org.apache.felix.ipojo.ComponentInstance;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

public class FundProxyFactory implements ApplicationContextAware
{
	
	Logger log = LoggerFactory.getLogger(FundProxyFactory.class);
	@Autowired
	private ListableBeanFactory listBeanFactory;
	private List<ComponentInstance> pojoInstances = new ArrayList<>();

	@Getter
	@Setter
	private FundSupport fundSupport;
	
	@PostConstruct
	public void injectFundAware() {
		for(Entry<String, Object> entiry : listBeanFactory.getBeansWithAnnotation(FundAware.class).entrySet()){
			Object bean = entiry.getValue();
			String beanName = entiry.getKey();
			registClass(beanName, bean);;
		}
	}
	
	public void registClass(String beanName, Object bean) {
		Class<?> beanClass = bean.getClass();
		for (Field field : FundAwareListener.allDeclaredField(beanClass)) {
			if (field.getType() == FundSupport.class)
				try {
					Method method = beanClass.getMethod("set" + StringUtils.capitalize(field.getName()), field.getType());
					if (fundSupport == null) {
						pojoInstances.add(FundAwareListener.createService(beanName,beanClass));
						ServiceReference<FundSupport> sref = (ServiceReference<FundSupport>) FrameworkUtil.getBundle(beanClass).getBundleContext()
								.getServiceReference(FundSupport.class);
						fundSupport = FrameworkUtil.getBundle(beanClass).getBundleContext().getService(sref);
					}
					method.invoke(bean, fundSupport);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	@PreDestroy
	public void destory() {
		for (ComponentInstance instance : pojoInstances) {
			instance.dispose();
		}
	}
	
	public static Object getBean(Class<?> requiredType){
		return ac.getBean(requiredType);
	}
	
	public static Object getBean(String beanName){
		return ac.getBean(beanName);
	}
	
	private static ApplicationContext ac;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		FundProxyFactory.ac = applicationContext;
	}

}
