package nivance.simplecass.cassandra.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Identifies a Keyspace to be persisted to Cassandra as a table.
 * 
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Keyspace {
	String name() default "";
	
	boolean ifNotExists() default true;
	
	int simpleReplica() default 1;
	
	
	
}
