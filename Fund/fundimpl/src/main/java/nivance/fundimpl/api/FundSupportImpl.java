package nivance.fundimpl.api;

import java.util.HashMap;
import java.util.Map;

import nivance.fund.api.FundSupport;
import nivance.fund.exception.FundException;
import nivance.fundimpl.cmd.Command;
import nivance.fundimpl.enums.CommandType;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;

@Component
@Provides
@Instantiate
public class FundSupportImpl implements FundSupport {

	@ServiceProperty(name = "key", value = "fundSupport")
	public String Provider;

	private Map<String, Command> cmds = new HashMap<>();

	@Override
	public Object retriveBalance(Object merchantid) throws FundException {
		try {
			return cmds.get(CommandType.RETRIVEBALANCECOMMAND.toString()).execute(
					merchantid);
		} catch (Exception e) {
			throw new FundException(e);
		}
	}

	@Override
	public Boolean adjustCredit(Object creditInfo) throws FundException {
		try {
			return (Boolean) cmds.get(CommandType.CREDITADJUSTCOMMAND.toString())
					.execute(creditInfo);
		} catch (Exception e) {
			throw new FundException(e);
		}
	}

	@Override
	public Object retriveFundflow(Object condition) throws FundException {
		try {
			return cmds.get(CommandType.RETRIVEFUNDFLOW.toString()).execute(
					condition);
		} catch (Exception e) {
			throw new FundException(e);
		}
	}

	@Override
	public Boolean recharge(Object account) throws FundException {
		try {
			return (Boolean) cmds.get(CommandType.RECHARGECOMMAND.toString()).execute(
					account);
		} catch (Exception e) {
			throw new FundException(e);
		}
	}

	@Override
	public Boolean withdraw(Object account) throws FundException {
		try {
			return (Boolean) cmds.get(CommandType.WITHDRAWCOMMAND.toString()).execute(
					account);
		} catch (Exception e) {
			throw new FundException(e);
		}
	}

	@Override
	public Boolean reward(Object account) throws FundException {
		try {
			return (Boolean) cmds.get(CommandType.REWARDCOMMAND.toString()).execute(
					account);
		} catch (Exception e) {
			throw new FundException(e);
		}
	}

	@Override
	public Boolean adjustSellamount(Object amountInfo) throws FundException {
		try {
			return (Boolean) cmds.get(CommandType.SELLAMOUNTADJUSTCOMMAND.toString())
					.execute(amountInfo);
		} catch (Exception e) {
			throw new FundException(e);
		}
	}

	@Override
	public Boolean wager(Object records) throws FundException {
		try {
			return (Boolean) cmds.get(CommandType.WAGERCOMMAND.toString()).execute(
					records);
		} catch (Exception e) {
			throw new FundException(e);
		}
	}

	@Override
	public Boolean openAccount(Object account) throws FundException {
		try {
			return (Boolean) cmds.get(CommandType.OPENACCOUNTCOMMAND.toString())
					.execute(account);
		} catch (Exception e) {
			throw new FundException(e);
		}
	}

	@Bind(aggregate = true, optional=true)
	private void bindCommand(Command cmd) {
		String key = StringUtils.upperCase(cmd.getClass().getSimpleName());
		cmds.put(key, cmd);
	}
	
}
