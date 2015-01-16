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

import com.cwl.iso.db.mysql.jpa.iface.StaticTableDaoSupport;
import ${packageName}.mapper.${domainClazz}Mapper;
import nivance.dbpapi.exception.JPAException;

@Repository
public class ${domainClazz}Dao implements StaticTableDaoSupport<${domainClazz}, ${domainClazz}Example, ${domainClazz}Key>{

	@Resource
	private ${domainClazz}Mapper mapper;
	
	
	@Override
	public int countByExample(${domainClazz}Example example) {
		return mapper.countByExample(example);
	}

	@Override
	public int deleteByExample(${domainClazz}Example example) {
		return mapper.deleteByExample(example);
	}

	@Override
	public int deleteByPrimaryKey(${domainClazz}Key key) {
		return mapper.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(${domainClazz} record)  {
		return mapper.insert(record);
	}

	@Override
	public int insertSelective(${domainClazz} record)  {
		return mapper.insertSelective(record);
	}

	@Override
	@Transactional
	public int batchInsert(List<${domainClazz}> records)
			 {
		for(${domainClazz} record : records){
			mapper.insert(record);
		}
		return records.size();
	}

	@Override
	@Transactional
	public int batchUpdate(List<${domainClazz}> records)
			 {
		for(${domainClazz} record : records){
			mapper.updateByPrimaryKeySelective(record);
		}
		return records.size();
	}

	@Override
	@Transactional
	public int batchDelete(List<${domainClazz}> records)
			 {
		for(${domainClazz} record : records){
			mapper.deleteByPrimaryKey(record);
		}
		return records.size();
	}

	@Override
	public List<${domainClazz}> selectByExample(${domainClazz}Example example)
			 {
		return mapper.selectByExample(example);
	}

	@Override
	public ${domainClazz} selectByPrimaryKey(${domainClazz}Key key)
			 {
		return mapper.selectByPrimaryKey(key);
	}

	@Override
	public List<${domainClazz}> findAll(List<${domainClazz}> records) {
		if(records==null||records.size()<=0){
			return mapper.selectByExample(new ${domainClazz}Example());
		}
		List<${domainClazz}> list = new ArrayList<>();
		for(${domainClazz} record : records){
			${domainClazz} result = mapper.selectByPrimaryKey(record);
			if(result!=null){
				list.add(result);
			}
		}
		return list;
	}

	@Override
	public int updateByExampleSelective(${domainClazz} record, ${domainClazz}Example example)  {
		return mapper.updateByExampleSelective(record, example);
	}

	@Override
	public int updateByExample(${domainClazz} record, ${domainClazz}Example example) {
		return mapper.updateByExample(record, example);
	}

	@Override
	public int updateByPrimaryKeySelective(${domainClazz} record) {
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(${domainClazz} record) {
		return mapper.updateByPrimaryKey(record);
	}

	@Override
	public int sumByExample(${domainClazz}Example example) {
		return 0;
	}

	@Override
	public void deleteAll()  {
		mapper.deleteByExample(new ${domainClazz}Example());
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
