package nivance.nio.impl.enums;

import static nivance.nio.impl.filter.ConnectionFilter.conInfo;
import static nivance.nio.impl.filter.ConnectionFilter.conns;
import lombok.Getter;
import nivance.nio.impl.ConnectionInfoImpl;
import nivance.nio.impl.pack.BeanHelper;
import nivance.nio.impl.pack.LogUtil;
import nivance.nio.impl.pack.PackBean;
import nivance.nio.ipojo.StringSocketImpl;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;

public enum Commands {

	HEARTBEAT("1000") {
		public NextAction process(StringSocketImpl ssi, FilterChainContext ctx) {
			PackBean pb = ctx.getMessage();
			ConnectionInfoImpl cif = conInfo.get(ctx.getConnection());
			if (cif == null || !cif.IsAuthorited()) {
				log.debug("HEART_REQ", ctx.getConnection().getPeerAddress().toString(), pb);
				pb = BeanHelper.buildRespBean(pb.getHeader(), ReturnCodes.NOTLOGIN.getCode());
				log.debug("HEART_RES", ctx.getConnection().getPeerAddress().toString(), pb);
				ctx.write(pb);// 未登录
				ctx.getConnection().closeSilently();
			} else {
				try {
					log.debug("HEART_REQ", cif.getRemoteAddress(), pb);
					ssi.onConnect(this.getCode(), cif, pb.header, pb.getBody());
				} catch (Exception e) {
					log.error(e, "给impl发送心跳信息异常");
				}
				pb = BeanHelper.buildRespBean(pb.getHeader(), ReturnCodes.SUCCESS.getCode());
				log.debug("HEART_RES", cif.getRemoteAddress(), pb);
				ctx.write(pb);
			}
			return ctx.getStopAction();
		}
	},

	LOGIN("1001") {
		public NextAction process(StringSocketImpl ssi, FilterChainContext ctx) {
			PackBean pb = ctx.getMessage();
			log.info("LOGIN_REQ", ctx.getConnection().getPeerAddress()
					.toString(), pb);
			ConnectionInfoImpl cif = conInfo.get(ctx.getConnection());
			if (cif == null) {// 判断连接
				cif = new ConnectionInfoImpl(ctx.getConnection());
			}
			try {
				String resp = ssi.onConnect(this.getCode(), cif, pb.header,
						pb.getBody());
				if (StringUtils.isBlank(resp)) {
					pb = BeanHelper.buildRespBean(pb.getHeader(),
							ReturnCodes.SUCCESS.getCode());
					log.info("LOGIN_RES", cif.getRemoteAddress(), pb);
					ctx.write(pb);
					cif.authorite(cif.getRemoteAddress().toString());
					conInfo.set(ctx.getConnection(), cif);
					conns.put(cif.getRemoteAddress(), cif);
				} else {
					pb = BeanHelper.buildRespBean(pb.getHeader(), resp);
					log.info("LOGIN_RES", cif.getRemoteAddress(), pb);
					ctx.write(pb);
					ctx.getConnection().closeSilently();
				}
			} catch (Exception e) {
				pb = BeanHelper.buildRespBean(pb.getHeader(), ReturnCodes.SYSERROR.getCode());
				log.error("QUERYWIN_EXCEPTION", ctx.getConnection().getPeerAddress().toString(), pb, e);
				ctx.write(pb);
				ctx.getConnection().closeSilently();
			}
			return ctx.getStopAction();
		}
	},

	LOGOUT("1002") {
		public NextAction process(StringSocketImpl ssi, FilterChainContext ctx) {
			PackBean pb = ctx.getMessage();
			ConnectionInfoImpl cif = conInfo.get(ctx.getConnection());
			log.info("LOGOUT_REQ", ctx.getConnection().getPeerAddress().toString(), pb);
			if (cif != null) {
				try {
					ssi.onConnect(this.getCode(), cif, pb.header, pb.getBody());
				} catch (Exception e) {
					log.error(e, "给impl发送logout信息异常");
				}
				pb = BeanHelper.buildRespBean(pb.getHeader(),
						ReturnCodes.SUCCESS.getCode());
				conInfo.remove(ctx.getConnection());
				conns.remove(ctx.getConnection().getPeerAddress().toString());
			} else {
				pb = BeanHelper.buildRespBean(pb.header, ReturnCodes.NOTLOGIN.getCode());
			}
			log.info("LOGOUT_RES", ctx.getConnection().getPeerAddress().toString(), pb);
			ctx.write(pb);
			ctx.getConnection().closeSilently();
			return ctx.getStopAction();
		}
	},

	ENCASH("2000") {
		public NextAction process(StringSocketImpl ssi, FilterChainContext ctx) {
			PackBean pb = ctx.getMessage();
			log.info("QUERYWIN_REQ", ctx.getConnection().getPeerAddress()
					.toString(), pb);
			return ctx.getInvokeAction();
		}
	},
	
	SECOND_ENCASH("2001"){
		public NextAction process(StringSocketImpl ssi, FilterChainContext ctx) {
			PackBean pb = ctx.getMessage();
			log.info("QUERYWIN_REQ", ctx.getConnection().getPeerAddress().toString(), pb);
			return ctx.getInvokeAction();
		}
	}
	;

	Commands(String code) {
		this.code = code;
	}

	@Getter
	private String code;

	public abstract NextAction process(StringSocketImpl ssi,
			FilterChainContext ctx);

	private static LogUtil log = new LogUtil(Commands.class);
}
