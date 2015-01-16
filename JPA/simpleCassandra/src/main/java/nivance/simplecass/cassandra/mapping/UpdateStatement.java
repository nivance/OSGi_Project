package nivance.simplecass.cassandra.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

import nivance.simplecass.cassandra.annotation.Table;
import nivance.simplecass.cassandra.exception.CQLGenException;
import nivance.simplecass.cassandra.util.AnnotationUtils;

import org.springframework.data.annotation.Transient;

import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Update;

/**
 * 根据bean生成一个preparedstatement, updatebyselective 最好新建一个bean
 * 
 * @author brew
 * 
 * @param <T>
 */
public class UpdateStatement extends CQLStatement {

	private Update generateUpdate(Class<?> clazz) throws CQLGenException {
		Table tb = (Table) clazz.getAnnotation(Table.class);
		if (tb == null) {
			throw new CQLGenException("TableName not found");
		}
		Update update = QueryBuilder.update(tb.name());
		for (Field field : clazz.getDeclaredFields()) {
			Transient trans = (Transient) field.getAnnotation(Transient.class);
			if (trans != null) {
				continue;
			}
			Annotation[] annotations = field.getAnnotations();
			if(AnnotationUtils.hasKey(annotations)) {
				update.where().and(QueryBuilder.eq(field.getName(), "?"));
				postFieldnames.add(field.getName());
			} else {
				update.with(QueryBuilder.set(field.getName(), "?"));
				fieldnames.add(field.getName());
			}
		}
		return update;
	}

	public CQLStatement update(Class<?> clazz) throws CQLGenException {
		cachedCQL=generateUpdate(clazz).getQueryString();
		return this;
	}

	public CQLStatement updateIfClause(Class<?> clazz, Clause ifClause) throws CQLGenException {
		Update update = generateUpdate(clazz);
		update.onlyIf(ifClause);
		cachedCQL=update.getQueryString();
		return this;
	}

//	public CQLStatement updateIfValid(Class<?> clazz) throws CQLGenException {
//		Update update = generateUpdate(clazz);
//		update.onlyIf(QueryBuilder.eq("isvalid", 1));
//		cachedCQL=update.getQueryString();
//		return this;
//	}
	
	public CQLStatement update(Class<?> clazz,HashMap<String,Object> mb){
		cachedCQL = generateUpdate(clazz,mb).getQueryString();
		return this;
	}
	
	private Update generateUpdate(Class<?> clazz,HashMap<String,Object> mb) {
		Table tb = (Table) clazz.getAnnotation(Table.class);
		Update update = QueryBuilder.update(tb.name());
		for (Field field : clazz.getDeclaredFields()) {
			Transient trans = (Transient) field.getAnnotation(Transient.class);
			if (trans != null) {
				continue;
			}
			Annotation[] annotations = field.getAnnotations();
			if(AnnotationUtils.hasKey(annotations)) {
				update.where().and(QueryBuilder.eq(field.getName(), "?"));
				postFieldnames.add(field.getName());
			} else if(mb.get(field.getName())!=null) {
				update.with(QueryBuilder.set(field.getName(), "?"));
				fieldnames.add(field.getName());
			}
		}
		return update;
	}

}
