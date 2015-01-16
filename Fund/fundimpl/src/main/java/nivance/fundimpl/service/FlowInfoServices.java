package nivance.fundimpl.service;

import java.io.IOException;

import nivance.dbpapi.exception.JPAException;
import nivance.fundimpl.bean.FlowInputBean;
import nivance.fundimpl.bean.FlowOutputBean;

public interface FlowInfoServices {

	/**
	 * 查询 资金流水 生产表
	 * @param inputBean
	 * @return
	 * @throws JPAException 
	 */
	public FlowOutputBean getInfo(FlowInputBean inputBean) throws JPAException;
	
	/**
	 * 查询 资金流水 生产表(count 用于分页)
	 * @param inputBean
	 * @return
	 * @throws JPAException 
	 * @throws IOException 
	 */
	public int countInfo(FlowInputBean inputBean) throws JPAException, IOException;
	
	
}
