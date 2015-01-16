package nivance.nio.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.ipojo.annotations.Validate;

@Slf4j
@Component(immediate = true)
@Provides(specifications = Servlet.class,strategy="METHOD")
@Instantiate(name="accessor")
public class ServletPc extends HttpServlet {

	private static final long serialVersionUID = -3039165877563617457L;

	private @ServiceProperty(name = "Web-ContextPath") String ctx = "/Accessor/accept.action";
	
	@Validate
	public void bundleActive() {
		log.info("Accessor HttpService...start:");
	}
	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
 
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		long starttime = System.currentTimeMillis();
		String reqStr = null;
		String respJson = "";
		String clientIp = this.getIpAddr(request);
		reqStr = this.getRequestContent(request);
		log.debug("RequestClientIp:[" + clientIp + "] RequestJson----->[" + reqStr + "]");
		try {
			// QUERYWIN_REQ: [请求端IP] [UUID] @:[请求json串]
//			QUERYWIN_RES: [请求端IP] [UUID] @:[响应 json串]
		} catch (Exception e) {
			log.error("程序异常:{}", e);
		} finally {
			try {
				log.debug("Access_RESPONSE: costtime is ["
						+ (System.currentTimeMillis() - starttime)
						+ "] Json is [" + respJson + "]");
				response.getWriter().write(respJson);
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				log.error("响应异常", e);
			}
		}
		
	}

	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private String getRequestContent(HttpServletRequest request) throws IOException {
		InputStream is = null;
		String xml = null;
		if ("application/x-www-form-urlencoded".equals(request.getContentType())) {
			return (String) request.getAttribute(ContentServletFilter.XSTREAM_REPLACE_ATTR);
		}
		try {
			is = request.getInputStream();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] data = new byte[8192];
			int rsize = 0;
			do {
				rsize = is.read(data);
				if (rsize > 0) {
					bout.write(data, 0, rsize);
				}
			} while (rsize > 0);
			xml = new String(bout.toByteArray());
			bout.close();
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return xml;
	}
}
