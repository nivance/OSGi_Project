package nivance.simplecass.cassandra.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cassandra.core.keyspace.TableOption;


/**
 * Identifies a domain object to be persisted to Cassandra as a table.
 * 
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Table {

	String name() default "";
	
	boolean ifNotExists() default true;

	TableOption [] options() default {}; 
	
	String compression() default ""; 
	
	boolean cacheAll() default false;
}
