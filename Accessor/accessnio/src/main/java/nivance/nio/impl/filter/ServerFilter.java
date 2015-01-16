package nivance.nio.impl.filter;

import java.io.IOException;

import nivance.iso.nio.api.MessageListener;
import nivance.iso.nio.api.OnMessageComplete;
import nivance.nio.impl.ConnectionInfoImpl;
import nivance.nio.impl.enums.ReturnCodes;
import nivance.nio.impl.pack.BeanHelper;
import nivance.nio.impl.pack.LogUtil;
import nivance.nio.impl.pack.PackBean;

import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;

public class ServerFilter extends BaseFilter {
	private LogUtil log = new LogUtil(ServerFilter.class);
	private MessageListener listener;

	public ServerFilter(MessageListener listener) {
		super();
		this.listener = listener;
	}

	@Override
	public NextAction handleRead(final FilterChainContext ctx)
			throws IOException {
		final PackBean pb = ctx.getMessage();
		final ConnectionInfoImpl connInfo = ConnectionFilter.conInfo.get(ctx
				.getConnection());
		PackBean rspb = null;
		if (connInfo == null || !connInfo.IsAuthorited()) {
			rspb = BeanHelper.buildRespBean(pb.getHeader(),	ReturnCodes.NOTLOGIN.getCode());
			log.info("QUERYWIN_RES", ctx.getConnection().getPeerAddress().toString(), rspb);
			ctx.write(rspb);// 身份验证未通过
			ctx.getConnection().closeSilently();
			return ctx.getStopAction();
		}
		try{
			listener.onMessage(connInfo, pb.getHeader(), pb.getBody(),
				new OnMessageComplete() {
					@Override
					public void onComplete(String header[], String writeresult) {
						PackBean rspb = BeanHelper.buildRespBean(header, writeresult);
						ctx.write(rspb);
						log.info("QUERYWIN_RES", connInfo.getRemoteAddress(), rspb);
					}
				});
		}catch(Exception e){
			rspb = BeanHelper.buildRespBean(pb.getHeader(),	ReturnCodes.SYSERROR.getCode());
			log.error("QUERYWIN_EXCEPTION", connInfo.getRemoteAddress(), pb, e);
			ctx.write(rspb);
		}
		return ctx.getInvokeAction();
	}

	@Override
	public NextAction handleClose(FilterChainContext ctx) throws IOException {
		log.warn("Close connection:" + ctx.getConnection().getPeerAddress());
		return ctx.getInvokeAction();
	}

	@Override
	public void exceptionOccurred(FilterChainContext ctx, Throwable error) {
		final PackBean pb = ctx.getMessage();
		log.error("QUERYWIN_EXCEPTION", ctx.getConnection().getPeerAddress().toString(), pb, error);
	}

}
