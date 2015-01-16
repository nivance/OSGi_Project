package nivance.iso.nio.ipojo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;
import nivance.iso.nio.annotation.MessageWare;

import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.api.PrimitiveComponentType;
import org.apache.felix.ipojo.api.Service;
import org.apache.felix.ipojo.api.ServiceProperty;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Slf4j
public class IPojoFactory implements ApplicationContextAware {
	List<ComponentInstance> pojoInstances=new ArrayList<>();

	@Autowired
	private ListableBeanFactory listBeanFactory;

	@PostConstruct
	public void init() {
		
		log.debug("IPojoFactory::init():" + listBeanFactory.getBeanDefinitionNames().length + ":" + listBeanFactory.getBeansWithAnnotation(MessageWare.class));
		for (Object bean : listBeanFactory.getBeansWithAnnotation(MessageWare.class).values()) {
			registClass(bean);
		}
		
	}

	public void registClass(Object bean) {
		Class<?> beanClass = bean.getClass();
		log.debug("beanclass:" + beanClass + "::bean:" + bean);
//		for (Field field : beanClass.getDeclaredFields()) {
//			try {
//				Method method = beanClass.getMethod("get" + StringUtils.capitalize(field.getName()));
//				log.info("method;"+method.invoke(bean));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//		}
		BundleContext context = FrameworkUtil.getBundle(beanClass).getBundleContext();
		MessageWare ware = (MessageWare) beanClass.getAnnotation(MessageWare.class);
		try {
			ComponentInstance proxyinstance = new PrimitiveComponentType().setBundleContext(context).setClassName(beanClass.getName())
					.setFactoryMethod("springInstance")//
					.addService(new Service().addProperty(new ServiceProperty().setField("msgactor").setName("msgactor").setValue(ware.command())))
//					.addProperty(new Property().setField("msgactor").setName("msgactor").setValue(ware.command()))//
					.createInstance();
			System.out.println("create Instance::" + proxyinstance);
			pojoInstances.add(proxyinstance);
		} catch (Exception e) {
			log.error("error in regist service:" + beanClass, e);
			e.printStackTrace();
		}
	}

	static ApplicationContext applicationContext;

	@PreDestroy
	public void destory() {
		// proxy.get();
		for (ComponentInstance instance : pojoInstances) {
			instance.dispose();
		}
	}
	
	public static <T> T getBean(String name, Class<T> clazz){
		return applicationContext.getBean(name, clazz);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		log.debug("setContext"+applicationContext);
		IPojoFactory.applicationContext = applicationContext;
	}

	public static Object getBean(Class<?> beanclazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Component comp=(Component)beanclazz.getAnnotation(Component.class);
		Object from=applicationContext.getBean(comp.value());
		Object to=beanclazz.newInstance();
		copy(from,to);
//log.debug("---"+JsonUtil.bean2Json(to));
		return to;
	}
		
	public static void copy(Object from,Object to){
		BeanUtils.copyProperties(from,to);
	}
}
