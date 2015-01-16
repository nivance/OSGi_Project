package nivance.simplecass.cassandra.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.datastax.driver.core.DataType;

/**
 * Qualifies data type as Cassandra type.
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Qualify {

	DataType.Name type();

	DataType.Name[] typeArguments() default {};

}
