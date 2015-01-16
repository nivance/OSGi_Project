package nivance.dbpapi.api;

import nivance.dbpapi.domain.DomainController;
import nivance.dbpapi.exception.JPAException;
import nivance.serialize.SerializerFactory;
import nivance.serialize.SerializerFactory.Type;

/**
 * 参数说明：<br>
 * domains: 设置查询类型，表名等信息<br>
 * entity: avro 序列化后的数据<br>
 * 
 * exam:<br>
 * domains: mysql.CoreVO.CoreRO; <br>
 * entity: jsonString1.jsonString2
 * 
 */
public interface DomainDaoSupport {
	String DELIMITER = "#";
	Type type = SerializerFactory.Type.JSON;

	/**
	 * @param dc
	 * @param entity pojoJson
	 * @throws JPAException
	 */
	Object insert(DomainController dc, Object entity) throws JPAException;

	/**
	 * @param dc 
	 * @param entities ListJson#ListJson#ListJson..
	 * @throws JPAException
	 */
	Object batchInsert(DomainController dc, Object entities) throws JPAException;

	/**
	 * @param dc
	 * @param entity pojoJson
	 * @throws JPAException
	 */
	Object update(DomainController dc, Object entity) throws JPAException;

	/**
	 * @param dc 
	 * @param entities ListJson#ListJson#ListJson..
	 * @throws JPAException
	 */
	Object batchUpdate(DomainController dc, Object entities) throws JPAException;

	/**
	 * 
	 * @param dc
	 * @param entity ID
	 * @return pojoJson
	 * @throws JPAException
	 */
	Object findOne(DomainController dc, Object entity) throws JPAException;
	
	/**
	 * @param dc
	 * @param entity
	 * @return
	 * @throws JPAException
	 */
	Object findByExample(DomainController dc, Object entity) throws JPAException;

	/**
	 * @param dc
	 * @param entities   List<Pojo>
	 * @return ListJson
	 * @throws JPAException
	 */
	Object findAll(DomainController dc, Object entities) throws JPAException;
	
	/**
	 * 
	 * @param dc
	 * @param entity pojoJson
	 * @return boolean
	 * @throws JPAException
	 */
	Object exists(DomainController dc, Object entity) throws JPAException;

	/**
	 * 
	 * @param dc
	 * @throws JPAException
	 */
	Object count(DomainController dc, Object entity) throws JPAException;

	/**
	 * @param dc 
	 * @param entities ListJson#ListJson#ListJson..
	 * @throws JPAException
	 */
	Object delete(DomainController dc, Object entity) throws JPAException;
	/**
	 * @param dc 
	 * @param entities ListJson#ListJson#ListJson..
	 * @throws JPAException
	 */
	Object batchDelete(DomainController dc, Object entities) throws JPAException;

	/**
	 * @param dc
	 * @throws JPAException
	 */
	Object deleteAll(DomainController dc, Object entity) throws JPAException;

	/**
	 * @param domain
	 * @param sql
	 * @return
	 * @throws JPAException
	 */
	Object doBySQL(DomainController dc, String sql) throws JPAException;
}
