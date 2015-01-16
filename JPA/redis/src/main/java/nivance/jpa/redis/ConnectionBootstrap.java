package nivance.jpa.redis;

import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import nivance.jpa.redis.osgi.SpringCtxHolder;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;

@Slf4j
@Component(immediate = true)
@Instantiate
public class ConnectionBootstrap {

	@Validate
	public void setup() throws IOException {
		ConnectionCfg cfg = ctx.getBean("connectionCfg", ConnectionCfg.class);
		cfg.start(System.getenv("HOME") + File.separator + "rdscfg" + File.separator);
	}

	@Invalidate
	public void tearDown() {
		ConnectionCfg cfg = ctx.getBean("connectionCfg", ConnectionCfg.class);
		cfg.close();
		
		LoadBalancer balancer = ctx.getBean("loadBalancer", LoadBalancer.class);
		try {
			balancer.destroy();
		} catch (Exception e) {
			log.error("close error.", e);
		}
	}

	private @Requires SpringCtxHolder ctx;
}
