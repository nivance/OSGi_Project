package nivance.jpa.cassandra.prepare.schema;

import java.util.LinkedList;
import java.util.List;

import nivance.jpa.cassandra.prepare.convert.CassandraConverter;

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

/**   
 * @Company: 北京畅享互联有限公司
 * @Copyright: Copyright (c) 2012
 * @Author： 杨其桔
 * 创建时间：2014年2月19日 下午7:55:47   
 * 修改备注：   
 * @version    
 *    
 */
public class DefaultFindOneOperation<T> extends AbstractFindOneOperation<T>{

	protected CassandraConverter cassandraConverter;
	protected T entity;
	public DefaultFindOneOperation(Session session,
			CassandraConverter cassandraConverter, Class<T> entityClass,T entity) {
		super(session, cassandraConverter, entityClass);
		this.cassandraConverter = cassandraConverter;
		this.entity = entity;
	}

	@Override
	public RegularStatement createStatement() {
		Select select = QueryBuilder.select().all()
				.from(getTableName());
		Select.Where w = select.where();
//		CassandraPersistentEntity<?> cpe = cassandraConverter
//				.getMappingContext().getPersistentEntity(
//						entityClass);
//		List<Clause> list = cassandraConverter.getKeyPart(cpe, entity);
//		if(cpe.getIdProperty()==null){
//			list = cassandraConverter.getKeyPart(cpe, entity);
//		}else{
//			Method getMethod = cpe.getIdProperty().getGetter();
//			Object value = ReflectionUtils.invokeMethod(getMethod, entity);
//			list = cassandraConverter.getPrimaryKey(cpe, value);
//		}
		List<Clause> list = new LinkedList<Clause>();
		((CassandraConverter) entityReader).write(entity, list);
		for (Clause c : list) {
			w.and(c);
		}
		return select;
	}

}
