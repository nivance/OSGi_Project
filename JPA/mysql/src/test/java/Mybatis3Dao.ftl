package ${packageName}.dao;

import org.springframework.stereotype.Repository;

import com.cwl.iso.db.mysql.entity.${domainClazz};
import com.cwl.iso.db.mysql.entity.${domainClazz}Example;
import com.cwl.iso.db.mysql.entity.${domainClazz}Example.Criteria;
import com.cwl.iso.db.mysql.entity.${domainClazz}Key;

import com.cwl.iso.db.mysql.jpa.iface.StaticTableDaoSupport;
import com.cwl.iso.db.mysql.dao.AbstractStaticTableDaoSupport;
import ${packageName}.mapper.${domainClazz}Mapper;

@Repository
public class ${domainClazz}Dao extends AbstractStaticTableDaoSupport<${domainClazz}, ${domainClazz}Example, ${domainClazz}Key>{

	
	/* (non-Javadoc)
	 * @see com.cwl.iso.db.sql.dao.AbstractSqlDaoSupport#getSqlDaoSupport()
	 */
	@Override
	protected StaticTableDaoSupport getSqlDaoSupport() {
		return sqlSessionTemplate.getMapper(${domainClazz}Mapper.class);
	}

	/* (non-Javadoc)
	 * @see nivance.dbpapi.iface.SqlDaoSupport#getExample()
	 */
	@Override
	public ${domainClazz}Example getExample(${domainClazz} record) {
		${domainClazz}Example example = new ${domainClazz}Example();
		if(record!=null){
			Criteria criteria = example.createCriteria();
			${exampleBody}
		}
		return example;
	}
}
