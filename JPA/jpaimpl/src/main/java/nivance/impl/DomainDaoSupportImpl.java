package nivance.impl;

import java.io.IOException;

import javax.annotation.Resource;

import nivance.dbpapi.api.DomainDaoSupport;
import nivance.dbpapi.domain.DomainController;
import nivance.dbpapi.exception.JPAException;
import nivance.impl.service.AbstractDomainDaoSupport;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("domainDaoSupportImpl")
public class DomainDaoSupportImpl implements DomainDaoSupport {
	private static Logger log = LoggerFactory
			.getLogger(DomainDaoSupportImpl.class);

	@Resource
	private JpaImplFactory jpaSetFactory;
	
	@Override
	public Object insert(DomainController domain, Object entity)
			throws JPAException {
		long start = System.currentTimeMillis();
		try {
			domain = buildDomain(domain);
			log.debug("insert domain:["+domain.getTarget()+"] table["+domain.getNames()+"]");
			Object obj = this.getDataService(domain).insert(domain,
					buildEntity(entity));
			log.debug("insert cost " + (System.currentTimeMillis() - start)
					+ "ms. domian [{}] ", domain);
			return obj;
		} catch (Throwable e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object batchInsert(DomainController domains, Object entities)
			throws JPAException {
		long start = System.currentTimeMillis();
		try {
			domains = buildDomain(domains);
			Object obj = this.getDataService(domains).batchInsert(domains,
					buildEntity(entities));
			log.debug("batchInsert cost "
					+ (System.currentTimeMillis() - start) + "ms. domian [{}]",
					domains);
			return obj;
		} catch (Throwable e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object update(DomainController domain, Object entity)
			throws JPAException {
		long start = System.currentTimeMillis();
		try {
			domain = buildDomain(domain);
			Object obj = this.getDataService(domain).update(domain,
					buildEntity(entity));
			log.debug("update cost " + (System.currentTimeMillis() - start)
					+ "ms. domian [{}] ", domain);
			return obj;
		} catch (Throwable e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object batchUpdate(DomainController domains, Object entities)
			throws JPAException {
		long start = System.currentTimeMillis();
		try {
			domains = buildDomain(domains);
			Object obj = this.getDataService(domains).batchUpdate(domains,
					buildEntity(entities));
			log.debug("batchUpdate cost "
					+ (System.currentTimeMillis() - start) + "ms. domian [{}]",
					domains);
			return obj;
		} catch (Throwable e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object findOne(DomainController domain, Object entity)
			throws JPAException {
		long start = System.currentTimeMillis();
		try {
			domain = buildDomain(domain);
			Object object = this.getDataService(domain).findOne(domain,
					buildEntity(entity));
			log.debug("findOne cost " + (System.currentTimeMillis() - start)
					+ "ms. domian [{}]", domain);
			return object;
		} catch (Throwable e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object findAll(DomainController domain, Object ids)
			throws JPAException {
		long start = System.currentTimeMillis();
		try {
			domain = buildDomain(domain);
			Object object = this.getDataService(domain).findAll(domain,
					buildEntity(ids));
			log.debug("findAll cost " + (System.currentTimeMillis() - start)
					+ "ms. domian [{}]", domain);
			return object;
		} catch (Throwable e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object exists(DomainController domain, Object entity)
			throws JPAException {
		long start = System.currentTimeMillis();
		try {
			domain = buildDomain(domain);
			Object b = this.getDataService(domain).exists(domain,
					buildEntity(entity));
			log.debug("exists cost " + (System.currentTimeMillis() - start)
					+ "ms. domian [{}]", domain);
			return b;
		} catch (Throwable e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object count(DomainController domain, Object entity)
			throws JPAException {
		long start = System.currentTimeMillis();
		try {
			domain = buildDomain(domain);
			Object count = this.getDataService(domain).count(domain, entity);
			log.debug("insert cost " + (System.currentTimeMillis() - start)
					+ "domain [{}] ", domain);
			return count;
		} catch (Throwable e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object delete(DomainController domains, Object entities)
			throws JPAException {
		long start = System.currentTimeMillis();
		try {
			domains = buildDomain(domains);
			Object obj = this.getDataService(domains).delete(domains,
					buildEntity(entities));
			log.debug("delete cost " + (System.currentTimeMillis() - start)
					+ "ms. domains [{}]", domains);
			return obj;
		} catch (Throwable e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object deleteAll(DomainController domains,Object entiry) throws JPAException {
		long start = System.currentTimeMillis();
		try {
			domains = buildDomain(domains);
			Object obj = this.getDataService(domains).deleteAll(domains,entiry);
			log.debug("insert cost " + (System.currentTimeMillis() - start)
					+ "domains [{}]", domains);
			return obj;
		} catch (Throwable e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object doBySQL(DomainController domain, String sql)
			throws JPAException {
		long start = System.currentTimeMillis();
		try {
			domain = buildDomain(domain);
			Object obj = this.getDataService(domain).doBySQL(domain, sql);
			log.debug("doBySQL cost " + (System.currentTimeMillis() - start)
					+ " ms domain [{}] sql[{}]", domain, sql);
			return obj;
		} catch (Throwable e) {
			throw new JPAException(e);
		}
	}

	@Override
	public Object findByExample(DomainController domain, Object entity)
			throws JPAException {
		domain = buildDomain(domain);
		try {
			return getDataService(domain).findByExample(domain,
					buildEntity(entity));
		} catch (Throwable e) {
			throw new JPAException("", e);
		}
	}

	@Override
	public Object batchDelete(DomainController domain, Object entities)
			throws JPAException {
		domain = buildDomain(domain);
		try {
			return getDataService(domain).batchDelete(domain,
					buildEntity(entities));
		} catch (Throwable e) {
			throw new JPAException(e);
		}
	}
	
	private DomainController buildDomain(DomainController domainContro) {
		if (StringUtils.isBlank(domainContro.getTarget())) {
			domainContro.setTarget(DbDomain.cassandra.toString());// default
																	// cassandra
		}
		return domainContro;
	}

	private Object buildEntity(Object entity) throws IOException {
		return entity;
	}

	private AbstractDomainDaoSupport getDataService(DomainController domainContro) {
		AbstractDomainDaoSupport daoSupport = jpaSetFactory.getTargetDataServices().get(domainContro
				.getTarget());
		return daoSupport;
	}
}
