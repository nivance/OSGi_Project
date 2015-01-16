package nivance.simplecass.cassandra.mapping;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import nivance.simplecass.cassandra.annotation.Indexed;
import nivance.simplecass.cassandra.annotation.Table;

import org.springframework.cassandra.core.cql.generator.CreateIndexCqlGenerator;
import org.springframework.cassandra.core.keyspace.CreateIndexSpecification;

import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;

public class IndexStatement {

	public static List<Statement> createIndex(Class<?> clazz) {
		List<Statement> indexStatements = new ArrayList<>();
		Table table = clazz.getAnnotation(Table.class);
		for (Field field : clazz.getDeclaredFields()) {
			Indexed idxAnt = field.getAnnotation(Indexed.class);
			if(idxAnt==null) continue;
			CreateIndexSpecification indexed = new CreateIndexSpecification();
			indexed.columnName(field.getName());
			indexed.tableName(table.name());
			indexed.ifNotExists();
			indexStatements.add(new SimpleStatement(new CreateIndexCqlGenerator(indexed).toCql()));
		}
		return indexStatements;
	}
}
