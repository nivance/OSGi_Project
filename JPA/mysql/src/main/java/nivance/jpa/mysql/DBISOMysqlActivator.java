package nivance.jpa.mysql;

import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.osgi.context.support.OsgiBundleXmlApplicationContext;

@Slf4j
public class DBISOMysqlActivator implements BundleActivator {

	private OsgiBundleXmlApplicationContext appCtx;

	@Override
	public void start(BundleContext arg0) throws Exception {
		log.debug("DBISOMysql start..");
		appCtx = new OsgiBundleXmlApplicationContext(new String[] { "SpringContext-Mysql.xml" ,"SpringContext-Common.xml" ,"SpringContext-Mapper.xml"});
		appCtx.setBundleContext(arg0);
		appCtx.setPublishContextAsService(false);
		appCtx.refresh();
		log.debug("DBISOMysql start success..");
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		appCtx.close();
	}

}
