package nivance.fund.ipojo;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import nivance.fund.api.FundSupport;
import nivance.fund.spring.FundProxyFactory;

import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.ConfigurationException;
import org.apache.felix.ipojo.MissingHandlerException;
import org.apache.felix.ipojo.UnacceptableConfiguration;
import org.apache.felix.ipojo.api.PrimitiveComponentType;
import org.apache.felix.ipojo.api.Service;
import org.apache.felix.ipojo.api.ServiceProperty;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.springframework.util.StringUtils;

@Slf4j
public class FundAwareListener implements Serializable{
	private static final long serialVersionUID = 2004125611453753698L;
	
	public String classname;
	
	public String beanname;

	public synchronized void onArrival(ServiceReference<?> ref) {
		try {
			Class<?> clazz = Class.forName(classname);
//			Object object = FundProxyFactory.getBean(clazz);
			Object object = FundProxyFactory.getBean(beanname);
			log.debug("Fund onArrival::class [{}] object[{}]", clazz, object);
			for (Field field : allDeclaredField(clazz)) {
				if (field.getType() == FundSupport.class){
					try {
						Method method = clazz.getMethod("set" + StringUtils.capitalize(field.getName()), field.getType());
						FundSupport daoSupport = (FundSupport) ref.getBundle().getBundleContext().getService(ref);
						method.invoke(object, daoSupport);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void onDeparture(ServiceReference<?> ref) {
		try {
			Class<?> clazz = Class.forName(classname);
//			Object object = FundProxyFactory.getBean(clazz);
			Object object = FundProxyFactory.getBean(beanname);
			log.warn("FundSupport onDeparture::class [{}] object[{}]", clazz, object);
			for (Field field : allDeclaredField(clazz)) {
				if (field.getType() == FundSupport.class){
					try {
						Method method = clazz.getMethod("set" + StringUtils.capitalize(field.getName()), field.getType());
						Object value = null;
						method.invoke(object, value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ComponentInstance createService(String beanName,Class<?> clazz) throws UnacceptableConfiguration, MissingHandlerException, ConfigurationException {
		BundleContext context = FrameworkUtil.getBundle(clazz).getBundleContext();
		ComponentInstance proxyinstance = new PrimitiveComponentType().setBundleContext(context).setClassName(FundAwareListener.class.getName())
				.addService(new Service().addProperty(new ServiceProperty().setField("classname").setName("classname").setValue(clazz.getName()))
						.addProperty(new ServiceProperty().setField("beanname").setName("beanname").setValue(beanName)))
				.addHandler(new Fundwbp().onArrival("onArrival").onDeparture("onDeparture").setFilter("(key=fundSupport)"))
				.createInstance();
		return proxyinstance;
	}
	
	/**
	 * 获得所有的field
	 * @param clazz
	 * @return
	 */
	public static List<Field> allDeclaredField(Class<?> clazz) {
		List<Field> fieldList = new ArrayList<>();
		Class<?> targetClass = clazz;
		do {
			Field[] fields = targetClass.getDeclaredFields();
			for (Field field : fields) {
				fieldList.add(field);
			}
			targetClass = targetClass.getSuperclass();
		} while (targetClass != null && targetClass != Object.class);
		return fieldList;
	}

}
