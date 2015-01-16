package nivance.jpa.mysql.admin.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import nivance.jpa.mysql.admin.mapper.AdminOperatorMapper;
import nivance.jpa.mysql.entity.AdminOperator;
import nivance.jpa.mysql.entity.AdminOperatorExample;
import nivance.jpa.mysql.entity.AdminOperatorKey;
import nivance.jpa.mysql.entity.AdminOperatorExample.Criteria;
import nivance.jpa.mysql.jpa.iface.StaticTableDaoSupport;

import org.springframework.stereotype.Repository;

@Repository
public class AdminOperatorDao implements StaticTableDaoSupport<AdminOperator, AdminOperatorExample, AdminOperatorKey>{

	@Resource
	private AdminOperatorMapper mapper;
	
	
	@Override
	public int countByExample(AdminOperatorExample example) {
		return mapper.countByExample(example);
	}

	@Override
	public int deleteByExample(AdminOperatorExample example) {
		return mapper.deleteByExample(example);
	}

	@Override
	public int deleteByPrimaryKey(AdminOperatorKey key) {
		return mapper.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(AdminOperator record)  {
		return mapper.insert(record);
	}

	@Override
	public int insertSelective(AdminOperator record)  {
		return mapper.insertSelective(record);
	}

	@Override
	public int batchInsert(List<AdminOperator> records)
			 {
		for(AdminOperator record : records){
			mapper.insert(record);
		}
		return records.size();
	}

	@Override
	public int batchUpdate(List<AdminOperator> records)
			 {
		for(AdminOperator record : records){
			mapper.updateByPrimaryKeySelective(record);
		}
		return records.size();
	}

	@Override
	public int batchDelete(List<AdminOperator> records)
			 {
		for(AdminOperator record : records){
			mapper.deleteByPrimaryKey(record);
		}
		return records.size();
	}

	@Override
	public List<AdminOperator> selectByExample(AdminOperatorExample example)
			 {
		return mapper.selectByExample(example);
	}

	@Override
	public AdminOperator selectByPrimaryKey(AdminOperatorKey key)
			 {
		return mapper.selectByPrimaryKey(key);
	}

	@Override
	public List<AdminOperator> findAll(List<AdminOperator> records) {
		if(records==null||records.size()<=0){
			return mapper.selectByExample(new AdminOperatorExample());
		}
		List<AdminOperator> list = new ArrayList<>();
		for(AdminOperator record : records){
			AdminOperator result = mapper.selectByPrimaryKey(record);
			if(result!=null){
				list.add(result);
			}
		}
		return list;
	}

	@Override
	public int updateByExampleSelective(AdminOperator record, AdminOperatorExample example)  {
		return mapper.updateByExampleSelective(record, example);
	}

	@Override
	public int updateByExample(AdminOperator record, AdminOperatorExample example) {
		return mapper.updateByExample(record, example);
	}

	@Override
	public int updateByPrimaryKeySelective(AdminOperator record) {
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(AdminOperator record) {
		return mapper.updateByPrimaryKey(record);
	}

	@Override
	public int sumByExample(AdminOperatorExample example) {
		return 0;
	}

	@Override
	public void deleteAll()  {
		mapper.deleteByExample(new AdminOperatorExample());
	}

	/* (non-Javadoc)
	 */
	@Override
	public AdminOperatorExample getExample(AdminOperator record) {
		AdminOperatorExample example = new AdminOperatorExample();
		if(record!=null){
			Criteria criteria = example.createCriteria();
							if(record.getUsername()!=null){
				criteria.andUsernameEqualTo(record.getUsername());
				}
				if(record.getPassword()!=null){
				criteria.andPasswordEqualTo(record.getPassword());
				}
				if(record.getRealname()!=null){
				criteria.andRealnameEqualTo(record.getRealname());
				}
				if(record.getRetry()!=null){
				criteria.andRetryEqualTo(record.getRetry());
				}
				if(record.getStatus()!=null){
				criteria.andStatusEqualTo(record.getStatus());
				}
				if(record.getCreatedate()!=null){
				criteria.andCreatedateEqualTo(record.getCreatedate());
				}
				if(record.getLastdate()!=null){
				criteria.andLastdateEqualTo(record.getLastdate());
				}

		}
		return example;
	}
}
