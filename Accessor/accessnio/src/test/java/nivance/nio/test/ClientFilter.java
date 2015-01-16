package nivance.nio.test;

import java.io.IOException;

import nivance.iso.nio.api.MessageListener;
import nivance.nio.impl.pack.PackBean;

import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientFilter extends BaseFilter {

	Logger log=LoggerFactory.getLogger(ClientFilter.class);
	MessageListener listener;
	public ClientFilter(MessageListener listner) {
		super();
		this.listener=listner;
	}

	@Override
	public NextAction handleRead(FilterChainContext ctx) throws IOException {
		PackBean pb = ctx.getMessage();
		listener.onMessage(null, pb.getHeader(), pb.getBody(), null);
		return ctx.getStopAction();
	}

	@Override
	public NextAction handleWrite(FilterChainContext ctx) throws IOException {
		return ctx.getInvokeAction();
	}

	@Override
	public NextAction handleAccept(FilterChainContext ctx) throws IOException {
		log.info("new connection:" + ctx.getConnection().getPeerAddress());
		return ctx.getInvokeAction();
	}

	@Override
	public NextAction handleClose(FilterChainContext ctx) throws IOException {
		log.info("close connection:" + ctx.getConnection().getPeerAddress());
		return ctx.getInvokeAction();
	}

	@Override
	public void exceptionOccurred(FilterChainContext ctx, Throwable error) {
		log.error("Exception:@" + ctx.getConnection().getPeerAddress(), error);
	}

}
