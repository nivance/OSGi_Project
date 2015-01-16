package nivance.simplecass.cassandra.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to identify fields by a specific tag. Tagged fields do not have dependency for the field name and it is a
 * refactoring friendly approach.
 * 
 * Example:
 * 
 * <code>
 * class Profile { @Tag(USER_NAME) String userName; String firstName; String lastName; }
 * cassandraTemplate.save(profile).taggedFields(USER_NAME).execute(); 
 * </code>
 * 
 * Will update only userName field.
 * 
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.ANNOTATION_TYPE })
public @interface Tag {
	int value();
}
