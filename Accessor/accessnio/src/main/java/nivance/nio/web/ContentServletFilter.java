package nivance.nio.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;

@Component(immediate = true)
@Provides(specifications = Filter.class,strategy="METHOD")
@Instantiate
public class ContentServletFilter implements Filter {
	
	@ServiceProperty
	private String pattern = "/Accessor/.*";
	@ServiceProperty
	private int ranking = 100;
	
	private static final Log log = LogFactory.getLog(ContentServletFilter.class);
	public final static String XSTREAM_TYPE_ENCODE="application/x-www-form-urlencoded";
	public final static String XSTREAM_TYPE="application/x-www-form";
	public final static String XSTREAM_JD_TYPE="text/xml charset=utf-8";

	public final static String XSTREAM_REPLACE_ATTR="stupid-filter";

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
/*		if(XSTREAM_TYPE_ENCODE.equals(request.getContentType()))
		{
//			log.debug("reset Xtream ContentType:"+request.getContentType());
			String reqContent=getRequestContent(request);
			request.setAttribute(XSTREAM_REPLACE_ATTR, URLDecoder.decode(reqContent,"UTF-8"));
//			log.debug("request.body="+URLDecoder.decode(reqContent,"UTF-8"));
		}
		else if(XSTREAM_TYPE.equals(request.getContentType()))
		{
//			log.debug("reset Xtream ContentType:"+request.getContentType());
			String reqContent=getRequestContent(request);
			request.setAttribute(XSTREAM_REPLACE_ATTR, reqContent);
//			log.debug("request.body="+reqContent);
		}else if(XSTREAM_JD_TYPE.equals(request.getContentType()))
		{
//			log.debug("reset Xtream ContentType:"+request.getContentType());
			String reqContent=getRequestContent(request);
			request.setAttribute(XSTREAM_REPLACE_ATTR, reqContent);
//			log.debug("request.body="+reqContent);
		}
        chain.doFilter(request, response);*/

	}

	public void destroy() {

	}
	
	public String getRequestContent(ServletRequest request) {
		InputStream is = null;
		String xml = null;
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
		} catch (IOException e) {
			log.error("", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("io Exception", e);
				}
			}
		}

		return xml;
	}


}
