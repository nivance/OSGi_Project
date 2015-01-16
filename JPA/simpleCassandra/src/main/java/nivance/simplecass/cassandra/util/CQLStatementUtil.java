package nivance.simplecass.cassandra.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

public class CQLStatementUtil {

	public static String getFindKey(Class<?> clazz,HashMap<String,Object> mb){
		StringBuffer key = new StringBuffer();
		for (Field field : clazz.getDeclaredFields()) {
			Annotation[] annotations = field.getAnnotations();
			if(AnnotationUtils.hasKeyAndIndex(annotations)&&mb.get(field.getName())!=null) {
				key.append(field.getName());
			}
		}
		return key.toString();
	}
	
	public static String getUpdateKey(Class<?> clazz,HashMap<String,Object> mb){
		StringBuffer key = new StringBuffer();
		for (Field field : clazz.getDeclaredFields()) {
			if(mb.get(field.getName())!=null) {
				key.append(field.getName());
			}
		}
		return key.toString();
	}
	
}
