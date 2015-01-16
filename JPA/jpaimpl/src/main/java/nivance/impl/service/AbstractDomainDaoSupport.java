package nivance.impl.service;

import nivance.dbpapi.domain.DomainController;
import nivance.dbpapi.exception.JPAException;

public interface AbstractDomainDaoSupport {
	
	Object insert(DomainController dc, Object entity) throws Exception;

	/**
	 * @param dc 
	 * @param entities ListJson#ListJson#ListJson..
	 * @throws JPAException
	 */
	Object batchInsert(DomainController dc, Object entities)throws Exception;

	/**
	 * @param dc
	 * @param entity pojoJson
	 * @throws JPAException
	 */
	Object update(DomainController dc, Object entity)throws Exception;

	/**
	 * @param dc 
	 * @param entities ListJson#ListJson#ListJson..
	 * @throws JPAException
	 */
	Object batchUpdate(DomainController dc, Object entities)throws Exception;

	/**
	 * 
	 * @param dc
	 * @param entity ID
	 * @return pojoJson
	 * @throws JPAException
	 */
	Object findOne(DomainController dc, Object entity)throws Exception;
	
	/**
	 * @param dc
	 * @param entity
	 * @return
	 * @throws JPAException
	 */
	Object findByExample(DomainController dc, Object entity)throws Exception;

	/**
	 * @param dc
	 * @param entities   List<Pojo>
	 * @return ListJson
	 * @throws JPAException
	 */
	Object findAll(DomainController dc, Object entities)throws Exception;
	
	/**
	 * 
	 * @param dc
	 * @param entity pojoJson
	 * @return boolean
	 * @throws JPAException
	 */
	Object exists(DomainController dc, Object entity)throws Exception;

	/**
	 * 
	 * @param dc
	 * @throws JPAException
	 */
	Object count(DomainController dc, Object entity)throws Exception;

	/**
	 * @param dc 
	 * @param entities ListJson#ListJson#ListJson..
	 * @throws JPAException
	 */
	Object delete(DomainController dc, Object entity)throws Exception;
	/**
	 * @param dc 
	 * @param entities ListJson#ListJson#ListJson..
	 * @throws JPAException
	 */
	Object batchDelete(DomainController dc, Object entities)throws Exception;

	/**
	 * @param dc
	 * @throws JPAException
	 */
	Object deleteAll(DomainController dc, Object entities)throws Exception;

	/**
	 * @param domain
	 * @param sql
	 * @return
	 * @throws JPAException
	 */
	Object doBySQL(DomainController dc, String sql)throws Exception;
}
