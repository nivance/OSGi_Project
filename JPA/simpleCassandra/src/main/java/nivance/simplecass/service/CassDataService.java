package nivance.simplecass.service;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import nivance.dbpapi.domain.DomainController;
import nivance.impl.service.CassDomainDataService;
import nivance.simplecass.api.JPADao;
import nivance.simplecass.cassandra.core.KeyspaceManager;

import org.springframework.stereotype.Service;

@Service("cassDataService")
public class CassDataService implements CassDomainDataService {
	
	@Resource(name = "lotteryKM")
	private KeyspaceManager keyspaceManager;
	
	private String getDomainName(DomainController domainContro) {
		return domainContro.getNames();
	}
	
	private JPADao getDao(DomainController dc){
		Class<?> clazz = DBPCassBean.getClass2Name(getDomainName(dc));
		JPADao dao = keyspaceManager.getDAO(clazz);
		return dao;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Object insert(DomainController dc, Object entity) throws Exception {
		HashMap<String,Object> mb =  (HashMap<String,Object>) entity;
		return getDao(dc).insert(mb);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object batchInsert(DomainController dc, Object entities)
			throws Exception {
		List<HashMap<String,Object>> list = (List<HashMap<String,Object>>) entities;
		return getDao(dc).insertBatch(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object update(DomainController dc, Object entity) throws Exception {
		HashMap<String,Object> mb =  (HashMap<String,Object>) entity;
		return getDao(dc).update(mb);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object batchUpdate(DomainController dc, Object entities)
			throws Exception {
		List<HashMap<String,Object>> list = (List<HashMap<String,Object>>) entities;
		return getDao(dc).update(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object findOne(DomainController dc, Object entity) throws Exception {
		HashMap<String,Object> mb =  (HashMap<String,Object>) entity;
		return getDao(dc).findOne(mb);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object findByExample(DomainController dc, Object entity)
			throws Exception {
		HashMap<String,Object> mb =  (HashMap<String,Object>) entity;
		return getDao(dc).findByExample(mb);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object findAll(DomainController dc, Object entities)
			throws Exception {
		if(entities==null){
			return getDao(dc).findAll();
		}
		List<HashMap<String,Object>> list = (List<HashMap<String,Object>>) entities;
		return getDao(dc).findAll(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object exists(DomainController dc, Object entity) throws Exception {
		HashMap<String,Object> mb =  (HashMap<String,Object>) entity;
		return getDao(dc).exists(mb);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object count(DomainController dc, Object entity) throws Exception {
		HashMap<String,Object> mb =  (HashMap<String,Object>) entity;
		if(entity==null){
			return getDao(dc).count();
		}
		return getDao(dc).count(mb);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object delete(DomainController dc, Object entity) throws Exception {
		HashMap<String,Object> mb =  (HashMap<String,Object>) entity;
		return getDao(dc).delete(mb);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object batchDelete(DomainController dc, Object entities)
			throws Exception {
		List<HashMap<String,Object>> list = (List<HashMap<String,Object>>) entities;
		return getDao(dc).deleteAll(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object deleteAll(DomainController dc, Object entities)
			throws Exception {
		if(entities==null){
			return getDao(dc).deleteAll();
		}
		List<HashMap<String,Object>> list = (List<HashMap<String,Object>>) entities;
		return getDao(dc).deleteAll(list);
	}

	@Override
	public Object doBySQL(DomainController dc, String sql) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
