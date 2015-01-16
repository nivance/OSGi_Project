package ${packageName}.ipojodao;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Validate;

import nivance.dbpapi.iface.SqlDaoSupport;
import nivance.dbpimpl.SpringContextUtil;
import nivance.dbpimpl.sql.dao.AbstractSqlDaoSupport;
import ${packageName}.domain.${domainClazz};
import ${packageName}.domain.${domainClazz}Example;

@Component(name = "${domainObject}IpojoDao")
@Provides
@Instantiate
public class ${domainClazz}IpojoDao extends AbstractSqlDaoSupport<${domainClazz},${domainClazz}Example>{

	@Validate
	@SuppressWarnings("unchecked")
	public void start() {
		daoSupport = (SqlDaoSupport<${domainClazz},${domainClazz}Example>) SpringContextUtil
				.getBean("${domainObject}DAO");
	}

	@Invalidate
	public void stop() {
		if (daoSupport != null) {
			daoSupport = null;
		}
	}
}
