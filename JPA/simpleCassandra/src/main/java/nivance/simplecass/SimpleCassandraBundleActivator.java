package nivance.simplecass;


import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.osgi.context.support.OsgiBundleXmlApplicationContext;

@Slf4j
public class SimpleCassandraBundleActivator implements BundleActivator {

	private OsgiBundleXmlApplicationContext appCtx;
	@Override
	public void start(BundleContext context) throws Exception {
		log.debug("DBCassandra start..");
		appCtx = new OsgiBundleXmlApplicationContext(new String[] { "SpringContext-Common.xml" });
		appCtx.setBundleContext(context);
		appCtx.setPublishContextAsService(false);
		appCtx.refresh();
		log.debug("DBCassandra start success..");
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		appCtx.close();
	}

}