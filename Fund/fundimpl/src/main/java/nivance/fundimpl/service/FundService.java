package nivance.fundimpl.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import nivance.dbpapi.exception.JPAException;
import nivance.fundimpl.bean.AccAccount;
import nivance.fundimpl.bean.AccAccountFlow;
import nivance.fundimpl.bean.OpenAccountInputBean;
import nivance.fundimpl.bean.WagerAmountInputBean;

public interface FundService {

	/**
	 * 加载所有投注账号信息
	 * 
	 * @return
	 */
	public void loadAllAccount() throws Exception;

	/**
	 * 插入流水
	 * 
	 * @param accAccountFlow
	 */
	public void insertFlow(AccAccountFlow accAccountFlow) throws Exception;

	/**
	 * 插入流水
	 * 
	 * @param accAccountFlow
	 */
	public void batchInsertFlows(List<AccAccountFlow> accAccountFlow) throws Exception;

	/**
	 * 批次更新账户资金信息
	 * 
	 * @param accAccount
	 */
	public void updateAccounts(List<AccAccount> accAccounts) throws Exception;

	/**
	 * 投注扣款
	 * 
	 * @param inputBean
	 * @param balance
	 *            :投注扣款前余额
	 */
	public void wager(WagerAmountInputBean inputBean, BigDecimal balance) throws Exception;

	/**
	 * 投注 返款
	 * 
	 * 
	 * @param inputBean
	 * @param accAccount
	 *            :投注扣款前余额
	 */
	public void wagerFail(WagerAmountInputBean inputBean, BigDecimal balance) throws Exception;

	/***
	 * 开户
	 * @throws IOException 
	 * @throws JPAException 
	 * 
	 */
	public AccAccount insertAccount(OpenAccountInputBean input) throws Exception;

}
