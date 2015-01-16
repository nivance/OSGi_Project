package nivance.jpa.redis.osgi;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("springCtxHolderImpl")
public class SpringCtxHolderImpl implements SpringCtxHolder {

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.ctx = ctx;
	}

	@Override
	public <T> T getBean(String name, Class<T> clazz) {
		return ctx.getBean(name, clazz);
	}

	private ApplicationContext ctx;
}