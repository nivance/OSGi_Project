package nivance.simplecass.cassandra.util;

import java.lang.annotation.Annotation;

import nivance.simplecass.cassandra.annotation.Counter;
import nivance.simplecass.cassandra.annotation.Id;
import nivance.simplecass.cassandra.annotation.Indexed;
import nivance.simplecass.cassandra.annotation.KeyColumn;

public class AnnotationUtils {
	
	public static  boolean hasKey(Annotation[] annotations){
		if(annotations==null||annotations.length<=0){
			return false;
		}
		for(Annotation at: annotations){
			if (at.annotationType() == KeyColumn.class||
				at.annotationType() == Id.class) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasKeyAndIndex(Annotation[] annotations){
		if(annotations==null||annotations.length<=0){
			return false;
		}
		for(Annotation at: annotations){
			if (at.annotationType() == KeyColumn.class||
				at.annotationType() == Indexed.class||
				at.annotationType() == Id.class) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasCounter(Annotation[] annotations){
		if(annotations==null||annotations.length<=0){
			return false;
		}
		for(Annotation at: annotations){
			if (at.annotationType() == Counter.class) {
				return true;
			}
		}
		return false;
	}
	
}
