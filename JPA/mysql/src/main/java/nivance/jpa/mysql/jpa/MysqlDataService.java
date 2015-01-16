package nivance.jpa.mysql.jpa;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.domain.DomainController;
import nivance.dbpapi.exception.JPAException;
import nivance.impl.service.MysqlDomainDataService;
import nivance.jpa.mysql.DBPMySqlBean;
import nivance.jpa.mysql.DataSource;
import nivance.jpa.mysql.Table2DbCommon;
import nivance.jpa.mysql.dao.CommonSqlMapper;
import nivance.jpa.mysql.jpa.iface.DynamicTableDaoSupport;
import nivance.jpa.mysql.jpa.iface.StaticTableDaoSupport;
import nivance.jpa.mysql.jpa.sub.DataSourcesHolder;
import nivance.serialize.SerializerFactory;
import nivance.serialize.SerializerUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


@Slf4j
@Component("mysqlDataService")
public class MysqlDataService implements MysqlDomainDataService {

	@SuppressWarnings("rawtypes")
	@Resource(name = "dynamicDaos") private Map<String, DynamicTableDaoSupport> dynamicDaos;
	@SuppressWarnings("rawtypes")
	@Resource(name = "staticDaos") private Map<String, StaticTableDaoSupport> staticDaos;
	@Resource private CommonSqlMapper commonSqlMapper;
	@Resource PlatformTransactionManager txManager; 

	@SuppressWarnings("rawtypes")
	private DynamicTableDaoSupport getDynamicTableDaoSupport(String name) throws Exception {
		try {
			return dynamicDaos.get(DBPMySqlBean.getDaoName(name));
		} catch (NullPointerException e) {
			throw new Exception(String.format("domain names is error..[%s]", name),e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private StaticTableDaoSupport getStaticTableDaoSupport(String name) throws Exception {
		try {
			return staticDaos.get(DBPMySqlBean.getDaoName(name));
		} catch (NullPointerException e) {
			throw new Exception(String.format("domain names is error..[%s]", name),e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object insert(DomainController domain, Object entity)
			throws Exception {
		try {
			String name = getDomainAndSetDataSource(domain);
			Object object = convert2Pojo(entity, name);
			if(DBPMySqlBean.isStaticTable(name)){
				return getStaticTableDaoSupport(name).insert(object);
			}
			String table = getTable(domain);
			return this.getDynamicTableDaoSupport(name).insert(object,table);
		} finally{
			DataSourcesHolder.removeDataSource();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object batchInsert(DomainController domain, Object entities) throws Exception {
		try {
			String name = getDomainAndSetDataSource(domain);
			List<?> list = convert2ListPojo(entities, name);
			if (DBPMySqlBean.isStaticTable(name)) {
				return getStaticTableDaoSupport(name).batchInsert(list);
			}
			String table = getTable(domain);
			return this.getDynamicTableDaoSupport(name)
					.batchInsert(list, table);
		} finally{
			DataSourcesHolder.removeDataSource();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object update(DomainController domain, Object entity)
			throws Exception {
		try {
			String name = getDomainAndSetDataSource(domain);
			Object object = convert2Pojo(entity, name);
			if (DBPMySqlBean.isStaticTable(name)) {
				return getStaticTableDaoSupport(name)
						.updateByPrimaryKeySelective(object);
			}
			String table = getTable(domain);
			return this.getDynamicTableDaoSupport(name)
					.updateByPrimaryKeySelective(object, table);
		} finally{
			DataSourcesHolder.removeDataSource();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object batchUpdate(DomainController domain, Object entities) throws Exception {
		try {
			String name = getDomainAndSetDataSource(domain);
			List<?> list = convert2ListPojo(entities, name);
			if (DBPMySqlBean.isStaticTable(name)) {
				return getStaticTableDaoSupport(name).batchUpdate(list);
			}
			String table = getTable(domain);
			return this.getDynamicTableDaoSupport(name)
					.batchUpdate(list, table);
		}finally{
			DataSourcesHolder.removeDataSource();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object findOne(DomainController domain, Object entity) throws Exception {
		try {
			String name = getDomainAndSetDataSource(domain);
			Object object = convert2Pojo(entity, name);
			Object value = null;
			if (DBPMySqlBean.isStaticTable(name)) {
				value = getStaticTableDaoSupport(name).selectByPrimaryKey(
						object);
			} else {
				String table = getTable(domain);
				value = this.getDynamicTableDaoSupport(name)
						.selectByPrimaryKey(object, table);
			}
			return value == null ? null : SerializerUtil.serialize(SerializerFactory.Type.JSON, value);
		} finally{
			DataSourcesHolder.removeDataSource();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object findAll(DomainController domain, Object entity) throws Exception {
		try {
			String name = getDomainAndSetDataSource(domain);
			List<?> list = convert2ListPojo(entity, name);
			List<Object> listNew = null;
			if (DBPMySqlBean.isStaticTable(name)) {
				listNew = getStaticTableDaoSupport(name).findAll(list);
			} else {
				String table = getTable(domain);
				listNew = this.getDynamicTableDaoSupport(name).findAll(list,
						table);
			}
			return (listNew != null && listNew.size() > 0) ? SerializerUtil
					.serializeArray(SerializerFactory.Type.JSON, listNew) : null;
		} finally{
			DataSourcesHolder.removeDataSource();
		}
	}

	@Override
	public Object exists(DomainController domain, Object entity) throws Exception {
		try {
			return findOne(domain, entity) != null ? true : false;
		} finally{
			DataSourcesHolder.removeDataSource();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object count(DomainController domainContro, Object entity) throws Exception {
		try {
			String name = getDomainAndSetDataSource(domainContro);
			if (entity != null) {
				entity = convert2Pojo(entity, name);
			}
			if (DBPMySqlBean.isStaticTable(name)) {
				return getStaticTableDaoSupport(name).countByExample(
						this.getStaticTableDaoSupport(name).getExample(entity));
			}
			String table = getTable(domainContro);
			Object example = this.getDynamicTableDaoSupport(name).getExample(
					entity);
			return this.getDynamicTableDaoSupport(name).countByExample(example,
					table);
		} finally{
			DataSourcesHolder.removeDataSource();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object delete(DomainController domainContro, Object entities) throws Exception {
		try {
			String name = getDomainAndSetDataSource(domainContro);
			Object object = convert2Pojo(entities, name);
			if(DBPMySqlBean.isStaticTable(name)){
				return getStaticTableDaoSupport(name).deleteByPrimaryKey(object);
			}
			String table = getTable(domainContro);
			return this.getDynamicTableDaoSupport(name).deleteByPrimaryKey(object,table);
		} finally{
			DataSourcesHolder.removeDataSource();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object batchDelete(DomainController dc, Object entities) throws Exception {
		try {
			String name = getDomainAndSetDataSource(dc);
			List<?> list = convert2ListPojo(entities, name);
			if (DBPMySqlBean.isStaticTable(name)) {
				return getStaticTableDaoSupport(name).batchDelete(list);
			}
			String table = getTable(dc);
			return this.getDynamicTableDaoSupport(name)
					.batchDelete(list, table);
		} finally{
			DataSourcesHolder.removeDataSource();
		}
	}

	@Override
	public Object deleteAll(DomainController domainContro,Object entiry) throws Exception {
		try {
			String name = getDomainAndSetDataSource(domainContro);
			if (DBPMySqlBean.isStaticTable(name)) {
				getStaticTableDaoSupport(name).deleteAll();
			} else {
				String table = getTable(domainContro);
				this.getDynamicTableDaoSupport(name).deleteAll(table);
			}
			return 1;
		} finally{
			DataSourcesHolder.removeDataSource();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object findByExample(DomainController domain, Object entity) throws Exception {
		try {
			List result = null;
			String name = getDomainAndSetDataSource(domain);
			entity = convert2Pojo(entity, name);
			if (DBPMySqlBean.isStaticTable(name)) {
				result = getStaticTableDaoSupport(name).selectByExample(
						getStaticTableDaoSupport(name).getExample(entity));
			} else {
				String table = getTable(domain);
				Object example = getDynamicTableDaoSupport(name).getExample(
						entity);
				result = getDynamicTableDaoSupport(name).selectByExample(
						example, table);
			}
			return (result != null && result.size() > 0) ? SerializerUtil
					.serializeArray(SerializerFactory.Type.JSON, result) : null;
		} finally{
			DataSourcesHolder.removeDataSource();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object doBySQL(DomainController domain, String sql)
			throws Exception {
		sql = buildSqlDataSourceAndDB(sql, domain);
		log.debug("DbMysql do sql [{}] ",sql);
		List<Map> newMap = new ArrayList<>();
		List<HashMap<String,Object>> maps = null;
		if(needTransaction(sql)){
			maps = doSqlByTransaction(sql);
		}else{
			maps = doSqlByNoTransaction(sql);
		}
		if (maps != null && maps.size() > 0) {
			for (Map map : maps) { 
				if (map != null) {
					for (Object key : map.keySet()) {
						Object obj = map.get(key);
						if (obj instanceof Timestamp) {
							map.put(key, ((Timestamp) obj).getTime());
						}
					}
					newMap.add(map);
				}
			}
			maps.clear();
			if (newMap.size() > 0) {
				return SerializerUtil.serializeArray(SerializerFactory.Type.JSON, newMap);
			}
		}
		return null;
	}
	
	private boolean needTransaction(String sql){
		if(StringUtils.containsIgnoreCase(sql, "INSERT")||
				StringUtils.containsIgnoreCase(sql, "UPDATE")||
				StringUtils.containsIgnoreCase(sql, "DELETE")){
			return true;
		}
		return false;
	}
	
	public List<HashMap<String,Object>> doSqlByTransaction(String sql) throws Exception{
		String[] sqls = StringUtils.split(sql, ";");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		List<HashMap<String,Object>> list = new ArrayList<>();
		try {
			for(String sqlinfo : sqls){
				list.addAll(commonSqlMapper.executeSql(sqlinfo));
			}
		}catch (Exception ex) {
		  txManager.rollback(status);
		  throw new Exception("do transaction is error..sql["+sql+"]",ex);
		}
		txManager.commit(status);
		return list;
	}
	
	public List<HashMap<String,Object>> doSqlByNoTransaction(String sql){
		return commonSqlMapper.executeSql(sql);
	}

	private Object convert2Pojo(Object entity, String name) throws Exception {
		try{
			return SerializerUtil.deserialize(SerializerFactory.Type.JSON, entity,DBPMySqlBean.getClass2Name(name));
		} catch (NullPointerException e) {
			throw new Exception(String.format("domain names is error.. [%s]",name),e);
		}
	}

	private List<?> convert2ListPojo(Object entity, String name)throws Exception {
		try{
			return SerializerUtil.deserializeArray(SerializerFactory.Type.JSON, entity,DBPMySqlBean.getClass2Name(name));
		} catch (NullPointerException e) {
			throw new Exception(String.format("domain names is error.. [%s]",name),e);
		}
	}
	
	private String getDomainAndSetDataSource(DomainController dc) {
		try {
			if(StringUtils.isBlank(dc.getDataSource())||DataSource.MAIN.equalsIgnoreCase(dc.getDataSource())){
				//main data source
				DataSource dataSource = DBPMySqlBean.getDataSource2Name(dc.getNames());
				DataSourcesHolder.setDataSource(dataSource.getMainSource());
			}else{
				DataSource dataSource = DBPMySqlBean.getDataSource2Name(dc.getNames());
				DataSourcesHolder.setDataSource(dataSource.getBakSource());
			}
			return dc.getNames();
		} catch (Exception e) {
			Object[] objs = new Object[3];
			objs[0] = dc.getFrom();
			objs[1] = dc.getNames();
			objs[2] = dc.getDataSource();
			String errorlog = String.format(" request dataSource error ..FROM[%s] name[%s] dataSource[%s] ",objs);
			throw new RuntimeException(errorlog,e);
		}
	}
	
	private String buildSqlDataSourceAndDB(String sql,DomainController dc) throws JPAException{
		if(StringUtils.isBlank(dc.getDataSource())||DataSource.MAIN.equalsIgnoreCase(dc.getDataSource())){
			DataSourcesHolder.setDataSource(Table2DbCommon.getDataSource(sql).getMainSource());//默认一个数据源
		}else{
			DataSourcesHolder.setDataSource(Table2DbCommon.getDataSource(sql).getBakSource());
		}
		return Table2DbCommon.sqlAddDBName(sql);
	}

	private String getTable(DomainController domainContro) throws Exception {
		try {
			return DBPMySqlBean.getTable2Name(domainContro.getNames());
		} catch (NullPointerException e) {
			throw new Exception(String.format("Table '%s' doesn't exist",domainContro.getNames()),e);
		}
	}

}
