package ${packageName}.dao;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cwl.iso.db.mysql.entity.${domainClazz};
import com.cwl.iso.db.mysql.entity.${domainClazz}Example;
import com.cwl.iso.db.mysql.entity.${domainClazz}Example.Criteria;
import com.cwl.iso.db.mysql.entity.${domainClazz}Key;

import com.cwl.iso.db.mysql.jpa.iface.DynamicTableDaoSupport;
import ${packageName}.mapper.${domainClazz}Mapper;
import com.cwl.iso.db.mysql.dao.CommonSqlMapper;
import nivance.dbpapi.exception.JPAException;

@Repository
public class ${domainClazz}Dao implements DynamicTableDaoSupport<${domainClazz}, ${domainClazz}Example, ${domainClazz}Key>{

	@Resource
	private ${domainClazz}Mapper mapper;
	
	
	@Override
	public int countByExample(${domainClazz}Example example, String table) {
		return mapper.countByExample(example, table);
	}

	@Override
	public int deleteByExample(${domainClazz}Example example, String table) {
		return mapper.deleteByExample(example, table);
	}

	@Override
	public int deleteByPrimaryKey(${domainClazz}Key key, String table) {
		return mapper.deleteByPrimaryKey(key, table);
	}

	@Override
	public int insert(${domainClazz} record, String table)  {
		return mapper.insert(record, table);
	}

	@Override
	public int insertSelective(${domainClazz} record, String table)  {
		return mapper.insertSelective(record, table);
	}

	@Override
	@Transactional
	public int batchInsert(List<${domainClazz}> records, String table)
			 {
		for(${domainClazz} record : records){
			mapper.insert(record, table);
		}
		return records.size();
	}

	@Override
	@Transactional
	public int batchUpdate(List<${domainClazz}> records, String table)
			 {
		for(${domainClazz} record : records){
			mapper.updateByPrimaryKeySelective(record, table);
		}
		return records.size();
	}

	@Override
	@Transactional
	public int batchDelete(List<${domainClazz}> records, String table)
			 {
		for(${domainClazz} record : records){
			mapper.deleteByPrimaryKey(record, table);
		}
		return records.size();
	}

	@Override
	public List<${domainClazz}> selectByExample(${domainClazz}Example example, String table)
			 {
		return mapper.selectByExample(example, table);
	}

	@Override
	public ${domainClazz} selectByPrimaryKey(${domainClazz}Key key, String table)
			 {
		return mapper.selectByPrimaryKey(key, table);
	}

	@Override
	public List<${domainClazz}> findAll(List<${domainClazz}> records, String table) {
		if(records==null||records.size()<=0){
			return mapper.selectByExample(new ${domainClazz}Example(), table);
		}
		List<${domainClazz}> list = new ArrayList<>();
		for(${domainClazz} record : records){
			${domainClazz} result = mapper.selectByPrimaryKey(record, table);
			if(result!=null){
				list.add(result);
			}
		}
		return list;
	}

	@Override
	public int updateByExampleSelective(${domainClazz} record, ${domainClazz}Example example,String table)  {
		return mapper.updateByExampleSelective(record, example, table);
	}

	@Override
	public int updateByExample(${domainClazz} record, ${domainClazz}Example example, String table) {
		return mapper.updateByExample(record, example, table);
	}

	@Override
	public int updateByPrimaryKeySelective(${domainClazz} record, String table) {
		return mapper.updateByPrimaryKeySelective(record, table);
	}

	@Override
	public int updateByPrimaryKey(${domainClazz} record, String table) {
		return mapper.updateByPrimaryKey(record, table);
	}

	@Override
	public int sumByExample(${domainClazz}Example example, String table) {
		return 0;
	}

	@Override
	public void deleteAll(String table)  {
		mapper.deleteByExample(new ${domainClazz}Example(), table);
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
