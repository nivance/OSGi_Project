package nivance.jpa.redis.osgi;

import org.springframework.context.ApplicationContextAware;

public interface SpringCtxHolder extends ApplicationContextAware {
	<T> T getBean(String name, Class<T> clazz);
}
