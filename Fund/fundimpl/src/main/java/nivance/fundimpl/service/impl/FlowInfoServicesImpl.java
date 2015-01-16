package nivance.fundimpl.service.impl;

import java.io.IOException;

import nivance.dbpapi.exception.JPAException;
import nivance.fundimpl.bean.FlowInputBean;
import nivance.fundimpl.bean.FlowOutputBean;
import nivance.fundimpl.service.FlowInfoServices;
import nivance.fundimpl.service.JPAService;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

@Component
@Provides(specifications = { FlowInfoServices.class })
@Instantiate
public class FlowInfoServicesImpl implements FlowInfoServices {

	@Requires
	private JPAService dp;

	/**
	 * 查询 资金流水 生产表
	 * 
	 * @param inputBean
	 * @return
	 * @throws JPAException
	 */
	@Override
	public FlowOutputBean getInfo(FlowInputBean inputBean) throws JPAException {
		FlowOutputBean result = new FlowOutputBean();
		//TODO....
		return result;
	}

	/**
	 * 查询 资金流水 生产表(count 用于分页)
	 * 
	 * @param inputBean
	 * @return
	 * @throws JPAException
	 * @throws IOException
	 */
	@Override
	public int countInfo(FlowInputBean inputBean) throws JPAException,
			IOException {
		int result = 0;
		//....
		return result;
	}


}
