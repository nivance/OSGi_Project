package nivance.nio.impl.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import nivance.iso.nio.api.ConnectionInfo;
import nivance.iso.nio.enums.EReqHeader;
import nivance.nio.impl.ConnectionInfoImpl;
import nivance.nio.impl.enums.Commands;
import nivance.nio.impl.enums.ReturnCodes;
import nivance.nio.impl.pack.BeanHelper;
import nivance.nio.impl.pack.LogUtil;
import nivance.nio.impl.pack.PackBean;
import nivance.nio.ipojo.StringSocketImpl;

import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.attributes.Attribute;
import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;

public class ConnectionFilter extends BaseFilter {
	private LogUtil log = new LogUtil(ConnectionFilter.class);

	private StringSocketImpl ssi;
	private Map<String, Commands> cmds = new HashMap<>();

	public final static Attribute<ConnectionInfoImpl> conInfo = Grizzly.DEFAULT_ATTRIBUTE_BUILDER
			.createAttribute(ConnectionFilter.class.getName() + ".conInfo");
	/** 保存连接信息 */
	public final static Map<String, ConnectionInfo> conns = new HashMap<>();

	public ConnectionFilter(StringSocketImpl listener) {
		super();
		this.ssi = listener;
		for (Commands cmd : Commands.values()) {
			cmds.put(cmd.getCode(), cmd);
		}
	}

	@Override
	public NextAction handleRead(final FilterChainContext ctx)
			throws IOException {
		long start = System.currentTimeMillis();
		final PackBean pb = ctx.getMessage();
		log.debug("ConnectionInfo", ctx.getConnection().getPeerAddress()
				.toString(), pb);
		PackBean rspb = null;
		String command = pb.header[EReqHeader.command.ordinal()];
		String uuid = pb.header[EReqHeader.UUID.ordinal()];
		try {
			if (cmds.containsKey(command)) {
				return cmds.get(command).process(ssi, ctx);
			} else {
				rspb = BeanHelper.buildErrorRespBean(pb.header,
						ReturnCodes.CMDERROR.getCode());
				ctx.write(rspb);
				log.info("QUERYWIN_EXCEPTION", ctx.getConnection()
						.getPeerAddress().toString(), rspb);
				return super.handleRead(ctx);
			}
		} finally {
			log.debug("command[" + command + "] UUID[" + uuid + "]PROCESSTIME["
					+ (System.currentTimeMillis() - start) + "]ms");
		}
	}

	@Override
	public NextAction handleClose(FilterChainContext ctx) throws IOException {
		log.warn("Close connection:@:" + ctx.getConnection().getPeerAddress());
		if (ctx.getConnection().getPeerAddress() != null) {
			ConnectionInfoImpl ci = conInfo.get(ctx.getConnection());
			if (ci != null) {
				try {
					ssi.onConnect(Commands.LOGOUT.getCode(), ci, null, null);
				} catch (Exception e) {
					log.debug(e, "连接断开后给impl发送logout信息异常");
				}
				conInfo.remove(ctx.getConnection());
				conns.remove(ctx.getConnection().getPeerAddress().toString());
			}
		}
		return super.handleClose(ctx);
	}

}
