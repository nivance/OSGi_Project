package nivance.jpa.redis;

import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.osgi.context.support.OsgiBundleXmlApplicationContext;

@Slf4j
public class DBISORedisActivator implements BundleActivator {

	private OsgiBundleXmlApplicationContext appCtx;

	@Override
	public void start(BundleContext arg0) throws Exception {
		log.debug("DBISORedis start..");
		appCtx = new OsgiBundleXmlApplicationContext(
				new String[] { "applicationContext-core.xml"});
		appCtx.setBundleContext(arg0);
		appCtx.setPublishContextAsService(false);
		appCtx.refresh();
		log.debug("DBISORedis start success..");
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		appCtx.close();
	}

}
