package nivance.nio.web;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import lombok.extern.slf4j.Slf4j;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Unbind;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

@SuppressWarnings("rawtypes")
@Component(immediate=true,name="accessor_servlet")
@Instantiate
@Slf4j
public class ServletManager {

	private LinkedList<HttpService> services = new LinkedList<HttpService>();
	private Map<String, Servlet> servlets = new HashMap<String, Servlet>();
	
	@Bind(aggregate=true)
	public void bindHttp(HttpService http) {
		services.add(http);
	}
	
	@Bind(aggregate=true)
	public void bindServlet(Servlet servlet, Map attrs) {
		String ctx = (String) attrs.get("Web-ContextPath");
		if (CTX.equals(ctx)) {
			servlets.put(ctx, servlet);
			for (HttpService s : services) {
				try {
					/*ExtHttpService ehs = (ExtHttpService)s;
					ehs.registerFilter(filter, "/Front/.*", null, 100, null);*/
					s.registerServlet(ctx, servlet, null, null);
					log.debug("method bindServlet ctx: [" + ctx + "]   execute...  bind httpservice....Servlet..." + servlet);
				} catch (ServletException e) {
					log.warn("注册servlet失败", e);
				} catch (NamespaceException e) {
					log.warn("注册servlet失败", e);
				} catch (Exception e) {
					log.warn("注册servlet失败", e);
				}
			}
		}
	}

	@Unbind
	public void unbindHttp(HttpService http) {
		for (String ctx : servlets.keySet()) {
			http.unregister(ctx);
			log.debug("Method unbindHttp  execute  ctx[" + ctx + "]...");
		}
		services.remove(http);
	}

	@Unbind
	public void unbindServlet(Servlet servlet, Map attrs) {
		String ctx = (String) attrs.get("Web-ContextPath");
		if (CTX.equals(ctx)) {
			servlets.remove(ctx);
			for (HttpService s : services) {
				/*ExtHttpService ehs = (ExtHttpService)s;
				ehs.unregisterFilter(filter);*/
				s.unregister(ctx);
				log.debug("Method unbindServlet  execute ctx:[" + ctx + "] ....unbind httpservice....Servlet...");
			}
		}
	}
	
	private static final String CTX = "/Accessor/accept.action";
}
