package nivance.fund.api;

import nivance.fund.exception.FundException;

public interface FundSupport {

	/**
	 * 查询余额
	 * 
	 * @param merchantid资金账号
	 *            byte[] of jsonstring
	 * @return
	 */
	public Object retriveBalance(Object merchantid) throws FundException;

	/**
	 * 调整信用额度
	 * 
	 * @param creditInfo信用金额信息
	 *            byte[] of jsonstring(merchantid,creditAmount)
	 * @return
	 */
	public Boolean adjustCredit(Object creditInfo) throws FundException;

	/**
	 * 查询资金流水
	 * 
	 * @param condition查询条件
	 * @return
	 */
	public Object retriveFundflow(Object condition) throws FundException;// FlowInputBean

	/**
	 * 充值
	 * 
	 * @param account充值信息
	 * @return
	 */
	public Boolean recharge(Object account) throws FundException;// AmountInputBean

	/**
	 * 商户扣款
	 * 
	 * @param account扣款信息
	 * @return
	 */
	public Boolean withdraw(Object account) throws FundException;// RewardInputBean

	/**
	 * 返奖
	 * 
	 * @param account扣款信息
	 * @return
	 */
	public Boolean reward(Object account) throws FundException;// AmountInputBean

	/**
	 * 每日最大销售金额调整
	 * 
	 * @param amountInfo每日最大销售金额信息
	 * @return
	 */
	public Boolean adjustSellamount(Object amountInfo) throws FundException;

	/**
	 * 投注
	 * 
	 * @param records
	 * @return
	 */
	public Boolean wager(Object records) throws FundException;

	/**
	 * 开户
	 * 
	 * @param account账号信息
	 * @return
	 */
	public Boolean openAccount(Object account) throws FundException;// id,amount,creditAmount,maxsaleamount,sellamount

}
