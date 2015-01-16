package nivance.fundimpl.service.impl;

import java.math.BigDecimal;
import java.util.List;

import nivance.fundimpl.bean.AccAccount;
import nivance.fundimpl.bean.AccAccountFlow;
import nivance.fundimpl.bean.OpenAccountInputBean;
import nivance.fundimpl.bean.WagerAmountInputBean;
import nivance.fundimpl.service.FundService;
import nivance.fundimpl.service.JPAService;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

@Component
@Provides(specifications={FundService.class})
@Instantiate
public class FundServiceImpl implements FundService {
	
	@Requires
	private JPAService dp;
	
	@Override
	public void loadAllAccount() {
		//TODO ...	
	}

	
	@Override
	public void wager(WagerAmountInputBean inputBean, BigDecimal amount) throws Exception {
		//TODO ...	
	}

	@Override
	public void wagerFail(WagerAmountInputBean inputBean, BigDecimal amount) throws Exception {
		//TODO ...	
	}

	@Override
	public void insertFlow(AccAccountFlow accAccountFlow) throws Exception {
		//TODO ...	
	}

	@Override
	public void updateAccounts(List<AccAccount> accAccounts) throws Exception {
		//TODO ...	
	}

	@Override
	public void batchInsertFlows(List<AccAccountFlow> accAccountFlows) throws Exception {
		//TODO ...	
	}

	@Override
	public AccAccount insertAccount(OpenAccountInputBean input) throws Exception {
		AccAccount ac = new AccAccount();
		//TODO ...	
		return ac;
	}

}
