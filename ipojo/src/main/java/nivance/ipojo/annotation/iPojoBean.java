package nivance.ipojo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Stereotype;

@Component
@Instantiate
@Stereotype
@Target(ElementType.TYPE)
public @interface iPojoBean {
	
}
