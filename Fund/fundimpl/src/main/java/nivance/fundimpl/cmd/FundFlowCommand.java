package nivance.fundimpl.cmd;

import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.api.DomainDaoSupport;
import nivance.fundimpl.bean.FlowInputBean;
import nivance.fundimpl.bean.FlowOutputBean;
import nivance.fundimpl.service.FlowInfoServices;
import nivance.serialize.SerializerUtil;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

/**
 * 资金流水查询
 * 
 */
@Slf4j
//@Service("fundFlowCommand")
@Component
@Provides
@Instantiate
public class FundFlowCommand implements Command {

	@Requires
	private FlowInfoServices flowInfoServices;

	@Override
	public Object execute(Object entity) throws Exception {
		// 获得 router 传过来的实体类
		FlowInputBean inputBean = SerializerUtil.deserialize(DomainDaoSupport.type, (byte[])entity,
				FlowInputBean.class);
		log.debug("fund flow search. input[" + inputBean + "]");
		// 按条件查询数据库相应数据 并 返回数据
		FlowOutputBean outputBean = flowInfoServices.getInfo(inputBean);
		return SerializerUtil.serialize(DomainDaoSupport.type, outputBean);
	}

}
