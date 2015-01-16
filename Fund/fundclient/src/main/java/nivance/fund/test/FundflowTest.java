package nivance.fund.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.api.DomainDaoSupport;
import nivance.fund.api.FundSupport;
import nivance.fund.bean.AmountInputBean;
import nivance.fund.bean.BalanceOutputBean;
import nivance.fund.bean.CreditInputBean;
import nivance.fund.bean.FlowInputBean;
import nivance.fund.bean.FlowOutputBean;
import nivance.fund.bean.OpenAccountInputBean;
import nivance.fund.bean.RecordBean;
import nivance.fund.bean.RewardInputBean;
import nivance.fund.bean.SellAmountInputBean;
import nivance.fund.bean.WagerAmountInputBean;
import nivance.fund.exception.FundException;
import nivance.serialize.SerializerUtil;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.apache.felix.ipojo.whiteboard.Whiteboards;
import org.osgi.framework.ServiceReference;

@Slf4j
@Component
@Provides
@Instantiate
@Whiteboards(whiteboards = { @Wbp(filter = "(key=fundSupport)", onArrival = "onArrival", onDeparture = "onDeparture") })
public class FundflowTest implements ITest { 

	@Getter	@Setter
	private FundSupport fundSupport;
	
	public synchronized void onArrival(ServiceReference<?> ref) {
		fundSupport = (FundSupport) ref.getBundle().getBundleContext().getService(ref);
		log.debug("###########fund onArrivals#############" + fundSupport);
		start();
	}
	
	public synchronized void onDeparture(ServiceReference<?> ref) {
		fundSupport = null;
		log.debug("###########jpa onDepartures#############");
	}

	/* (non-Javadoc)
	 */
	@Override
	public void start() {
		retriveBalanceTest();
		creditAdjustCommandTest();
		fundFlowCommand();
		rechargeCommand();
		rewardCommand();
		sellAmountAdjustCommand();
		wagerCommand();
		withdrawCommand();
		openAccountCommand();
	}

	/**
	 * 查询余额
	 * 
	 * @throws IOException
	 * @throws FundException
	 */
	public void retriveBalanceTest() {
		try {
			String merchantid = "834781";

			Object obj = fundSupport.retriveBalance(SerializerUtil.serialize(DomainDaoSupport.type, 
					merchantid));
			BalanceOutputBean outputBean = SerializerUtil.deserialize(DomainDaoSupport.type, (byte[]) obj,
					BalanceOutputBean.class);
			log.info("[FUND_FLOW_TEST][retriveBalanceTest]" + outputBean);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 信用额度调整
	 * 
	 * @throws IOException
	 * @throws FundException
	 */
	private void creditAdjustCommandTest()  {
		try {
			CreditInputBean bean = new CreditInputBean();
			bean.setMerchantid("834781");
			bean.setCreditAmount(123456L);
			boolean flag = getFundSupport().adjustCredit(
					SerializerUtil.serialize(DomainDaoSupport.type, bean));
			log.info("[FUND_FLOW_TEST][CreditAdjustCommandTest]" + flag);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 查询资金流水
	 * 
	 * @throws FundException
	 * @throws IOException
	 */
	public void retriveFundflowTest()  {
		try {
			FlowInputBean bean = new FlowInputBean();
			Object object = fundSupport.retriveFundflow(bean);
			FlowOutputBean flowOutputBean = SerializerUtil.deserialize(DomainDaoSupport.type, 
					(byte[]) object, FlowOutputBean.class);
			System.out.println(flowOutputBean);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private void fundFlowCommand()  {
		try {
			FlowInputBean info = new FlowInputBean();
			info.setMerchantid("834781");
			Object obj = getFundSupport().retriveFundflow(
					SerializerUtil.serialize(DomainDaoSupport.type, info));
			FlowOutputBean flowOutputBean = SerializerUtil.deserialize(DomainDaoSupport.type, (byte[]) obj,
					FlowOutputBean.class);
			log.info("[FUND_FLOW_TEST][FundFlowCommand]" + flowOutputBean);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private void rechargeCommand()  {
		try {
			AmountInputBean info = new AmountInputBean();
			info.setMerchantid("834781");
			info.setType("1");// 充值
			info.setDesc("测试充值");
			info.setAmount(9999999L);
			boolean flag = getFundSupport().recharge(
					SerializerUtil.serialize(DomainDaoSupport.type, info));
			log.info("[FUND_FLOW_TEST][RechargeCommand]" + flag);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 返款
	 * 
	 * @throws IOException
	 * @throws FundException
	 */
	private void rewardCommand()  {
		try {
			RewardInputBean info = new RewardInputBean();
			info.setMerchantid("834781");
			info.setPeriod("0");
			info.setAmount(123L);
			info.setLtype("QGSLTO");
			boolean flag = getFundSupport().reward(
					SerializerUtil.serialize(DomainDaoSupport.type, info));
			log.info("[FUND_FLOW_TEST][RechargeCommand]" + flag);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private void withdrawCommand()  {
		try {
			AmountInputBean info = new AmountInputBean();
			info.setAmount(11111L);
			info.setDesc("商户扣款");
			info.setMerchantid("834781");
			info.setType("7");
			boolean flag = getFundSupport().withdraw(
					SerializerUtil.serialize(DomainDaoSupport.type, info));
			log.info("[FUND_FLOW_TEST][RechargeCommand]" + flag);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 最大销售金额调整
	 * 
	 * @throws IOException
	 * @throws FundException
	 */
	private void sellAmountAdjustCommand()  {
		try {
			SellAmountInputBean info = new SellAmountInputBean();
			info.setMerchantid("834781");
			info.setSellamount(99999L);
			boolean flag = getFundSupport().adjustSellamount(
					SerializerUtil.serialize(DomainDaoSupport.type, info));
			log.info("[FUND_FLOW_TEST][RechargeCommand]" + flag);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 投注扣款\返款
	 * 
	 * @throws IOException
	 * @throws FundException
	 * 
	 */
	private void wagerCommand()  {
		try {
			List<RecordBean> list = new ArrayList<>();
			RecordBean rb = new RecordBean();
			rb.setAmount(123D);
			rb.setOrderno("orderno1");
			list.add(rb);
			WagerAmountInputBean info = new WagerAmountInputBean();
			info.setLtype("QGSLTO");
			info.setMerchantid("834781");
			info.setAmount(8888L);
			info.setMessageid("messageid1");
			info.setPeriod("2012001");
			info.setRecordbeanlist(list);
			byte[] sd = (byte[])SerializerUtil.serialize(DomainDaoSupport.type, info);
			// WagerAmountInputBean a = SerializerUtil.deserialize(sd,
			// WagerAmountInputBean.class);
			boolean flag = getFundSupport().wager(sd);
			log.info("[FUND_FLOW_TEST][RechargeCommand]" + flag);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private void openAccountCommand()  {
		try {
			OpenAccountInputBean info = new OpenAccountInputBean();
			info.setMerchantid("yang");
			info.setAmount(123456789L);
			info.setSellamount(1234567L);
			info.setCreditAmount(123456L);
			boolean flag = getFundSupport().openAccount(
					SerializerUtil.serialize(DomainDaoSupport.type, info));
			log.info("[FUND_FLOW_TEST][OpenAccountCommand]" + flag);
		} catch (Exception e) {
			log.error("", e);
		}
	}

}
