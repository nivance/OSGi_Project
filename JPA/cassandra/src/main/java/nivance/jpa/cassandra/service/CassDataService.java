package nivance.jpa.cassandra.service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import nivance.dbpapi.api.DomainDaoSupport;
import nivance.dbpapi.domain.DomainController;
import nivance.impl.service.CassDomainDataService;
import nivance.jpa.cassandra.iface.CassDaoSupport;
import nivance.serialize.SerializerUtil;

import org.springframework.stereotype.Component;

import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

@Component("cassDataService")
public class CassDataService implements CassDomainDataService {

	@SuppressWarnings("rawtypes")
	@Resource(name = "cassDaos")
	private Map<String, CassDaoSupport> cassDaos;
	@Resource(name = "cassandra-session")
	protected Session session;

	@SuppressWarnings("rawtypes")
	private CassDaoSupport getCassDaoSupport(String name) throws Exception {
		return cassDaos.get(name.toLowerCase());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object insert(DomainController domain, Object entity)
			throws Exception {
		String name = getDomain(domain);
		Object object = convert2Pojo(entity, name);
		return getCassDaoSupport(name).insert(object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object batchInsert(DomainController domains, Object entities)
			throws Exception {
		String name = getDomain(domains);
		List<?> list = convert2ListPojo(entities, name);
		return getCassDaoSupport(name).insert(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object update(DomainController domain, Object entity)
			throws Exception {
		String name = getDomain(domain);
		Object object = convert2Pojo(entity, name);
		return getCassDaoSupport(name).update(object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object batchUpdate(DomainController domains, Object entities)
			throws Exception {
		String name = getDomain(domains);
		List<?> list = convert2ListPojo(entities, name);
		return getCassDaoSupport(name).update(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object findOne(DomainController domain, Object entity)
			throws Exception {
		String name = getDomain(domain);
		Object obj = convert2Pojo(entity, name);
		Object object = getCassDaoSupport(name).findOne(obj);
		return object != null ? SerializerUtil.serialize(DomainDaoSupport.type, object) : null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object findAll(DomainController domain, Object entities)
			throws Exception {
		String name = getDomain(domain);
		List<?> list = convert2ListPojo(entities, name);
		List object = getCassDaoSupport(name).findAll(list);
		return (object != null && object.size() > 0) ? SerializerUtil.serializeArray(DomainDaoSupport.type, object) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object exists(DomainController domain, Object entity)
			throws Exception {
		String name = getDomain(domain);
		Object obj = convert2Pojo(entity, name);
		return getCassDaoSupport(name).exists(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object count(DomainController domain, Object entity)
			throws Exception {
		String name = getDomain(domain);
		if (entity == null) {
			return getCassDaoSupport(name).count();
		} else {
			Object obj = convert2Pojo(entity, name);
			return getCassDaoSupport(name).count(obj);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object delete(DomainController domains, Object entities)
			throws Exception {
		String name = getDomain(domains);
		Object obj = convert2Pojo(entities, name);
		return getCassDaoSupport(name).delete(obj);
	}

	@Override
	public Object deleteAll(DomainController domainContro, Object entities)
			throws Exception {
		String name = getDomain(domainContro);
		return getCassDaoSupport(name).deleteAll();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object batchDelete(DomainController dc, Object entities)
			throws Exception {
		String name = getDomain(dc);
		List<?> list = convert2ListPojo(entities, name);
		return getCassDaoSupport(name).delete(list);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object doBySQL(DomainController domain, String sql) throws Exception {
		List<HashMap> list = new ArrayList<>();
		ResultSet rs = session.execute(sql);
		for (Row row : rs.all()) {
			HashMap map = new HashMap();
			for (Definition info : row.getColumnDefinitions().asList()) {
				Object obj = getRowValue(row, info);
				map.put(info.getName(), obj);
			}
			list.add(map);
		}
		if (list.size() <= 0) {
			return null;
		}
		return SerializerUtil.serializeArray(DomainDaoSupport.type, list);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object findByExample(DomainController domain, Object entity)
			throws Exception {
		String name = getDomain(domain);
		Object object = convert2Pojo(entity, name);
		List result = getCassDaoSupport(name).findByExample(object);
		return (result != null && result.size() > 0) ? SerializerUtil.serializeArray(DomainDaoSupport.type, result) : null;
	}

	private Object convert2Pojo(Object entity, String name) {
		try {
			return SerializerUtil.deserialize(DomainDaoSupport.type, entity,
					DBPCassBean.getClass2Name(name));
		} catch (NullPointerException e) {
			throw new RuntimeException(String.format(
					"domain names is error.. [%s]", name), e);
		}
	}

	private List<?> convert2ListPojo(Object entities, String name) {
		try {
			return SerializerUtil.deserializeArray(DomainDaoSupport.type, entities,
					DBPCassBean.getClass2Name(name));
		} catch (NullPointerException e) {
			throw new RuntimeException(String.format(
					"domain names is error.. [%s]", name), e);
		} 
	}

	private String getDomain(DomainController domainContro) {
		return domainContro.getNames();
	}

	@SuppressWarnings("incomplete-switch")
	private Object getRowValue(Row row, Definition info) {
		DataType columnType = row.getColumnDefinitions()
				.getType(info.getName());
		String columnName = info.getName();
		if (columnType.isCollection()) {
			List<DataType> typeArguments = columnType.getTypeArguments();
			switch (columnType.getName()) {
			case SET:
				return row.getSet(columnName, typeArguments.get(0)
						.asJavaClass());
			case MAP:
				return row.getMap(columnName, typeArguments.get(0)
						.asJavaClass(), typeArguments.get(1).asJavaClass());
			case LIST:
				return row.getList(columnName, typeArguments.get(0)
						.asJavaClass());
			}
		}

		ByteBuffer bytes = row.getBytesUnsafe(columnName);
		return bytes == null ? null : columnType.deserialize(bytes);
	}
}
