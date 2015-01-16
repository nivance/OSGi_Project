package nivance.jpa.redis.osgi;

import java.util.Map;

import javax.annotation.Resource;

import nivance.dbpapi.domain.DomainController;
import nivance.dbpapi.exception.JPAException;
import nivance.impl.service.RedisDaoSupport;
import nivance.jpa.redis.dao.RedisDataInfoDao;

import org.springframework.stereotype.Component;

@Component("redisDaoSupportImpl")
public class RedisDaoSupportImpl implements RedisDaoSupport {

	@Override
	public Object insert(DomainController dc, Object entity) throws JPAException {
		try {
			String name = dc.getNames();
			byte[] enbs = (byte[])entity;
			return daos.get(name).batchInsert(enbs);
		} catch (Exception e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object batchInsert(DomainController dc, Object entities)
			throws JPAException {
		return this.insert(dc, entities);
	}

	@Override
	public Object update(DomainController dc, Object entity)
			throws JPAException {
		return this.insert(dc, entity);
	}

	@Override
	public Object batchUpdate(DomainController dc, Object entities)
			throws JPAException {
		return this.insert(dc, entities);
	}

	@Override
	public Object findOne(DomainController dc, Object entity)
			throws JPAException {
		try {
			String name = dc.getNames();
			byte[] enbs = (byte[])entity;
			return daos.get(name).findOne(enbs);
		} catch (Exception e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object findByExample(DomainController dc, Object entity)
			throws JPAException {
		throw new JPAException("not support this method.");
	}

	@Override
	public Object findAll(DomainController dc, Object entities)
			throws JPAException {
		try {
			String name = dc.getNames();
			byte[] enbs = (byte[])entities;
			return daos.get(name).findAll(enbs);
		} catch (Exception e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object exists(DomainController dc, Object entity)
			throws JPAException {
		throw new JPAException("not support this method.");
	}

	@Override
	public Object count(DomainController dc, Object entity) throws JPAException {
		try {
			String name = dc.getNames();
			byte[] enbs = (byte[])entity;
			return daos.get(name).count(enbs);
		} catch (Exception e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object delete(DomainController dc, Object entity)
			throws JPAException {
		try {
			String name = dc.getNames();
			byte[] enbs = (byte[])entity;
			return daos.get(name).deleteOne(enbs);
		} catch (Exception e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object batchDelete(DomainController dc, Object entities)
			throws JPAException {
		throw new JPAException("not support this method.");
	}

	@Override
	public Object deleteAll(DomainController dc, Object entity) throws JPAException {
		try {
			String name = dc.getNames();
			byte[] enbs = (byte[])entity;
			return daos.get(name).deleteAll(enbs);
		} catch (Exception e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object doBySQL(DomainController dc, String sql) throws JPAException {
		throw new JPAException("not support this method.");
	}

	private @Resource(name="daos") Map<String, RedisDataInfoDao> daos;
}
