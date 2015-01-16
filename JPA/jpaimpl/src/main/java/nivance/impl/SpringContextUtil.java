package nivance.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

	Logger log = LoggerFactory.getLogger(SpringContextUtil.class);
	private static ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringContextUtil.ctx = applicationContext;
	}

	public static Object getBean(String name) {
		return ctx.getBean(name);
	}

	public static <T> Object getBean(Class<T> clzz) {
		return ctx.getBean(clzz);
	}

}
