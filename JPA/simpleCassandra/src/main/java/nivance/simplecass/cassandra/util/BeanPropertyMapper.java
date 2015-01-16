package nivance.simplecass.cassandra.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.annotation.Transient;

/**
 * 将一个object映射成一个HashMap<String,Object>
 * 
 * @author brew
 * 
 */
public class BeanPropertyMapper {

	static Map<Class<?>, Map<String, Pair<Method, Method>>> class2Map = new HashMap<>();

	public static Map<String, Pair<Method, Method>> extractMethods(Class<?> clazz) {
		Map<String, Pair<Method, Method>> map = class2Map.get(clazz);
		if (map == null) {
			synchronized (class2Map) {
				map = class2Map.get(clazz);
				if (map == null) {
					map = new HashMap<>();
					class2Map.put(clazz, map);
					for (Field field : clazz.getDeclaredFields()) {
						Transient trans = (Transient) field.getAnnotation(Transient.class);
						if (trans != null) {
							continue;
						}
						Method get = MethodHelper.getMethod(clazz, field);
						Method set = MethodHelper.setMethod(clazz, field);
						if (get != null && set != null) {
							map.put(field.getName(), Pair.of(get, set));
						}
					}
				}
			}
		}
		return map;
	}

	public static  <T> T ObjectFrom(HashMap<String,Object> hb, Class<T> clazz) {
		T t;
		try {
			t = clazz.newInstance();
			Map<String, Pair<Method, Method>> getsetMethods = BeanPropertyMapper.extractMethods(clazz);
			for (Entry<String, Pair<Method, Method>> entry : getsetMethods.entrySet()) {
				entry.getValue().getRight().invoke(t, hb.get(entry.getKey()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return t;
	}

	public static HashMap<String,Object> hashbeanFrom(Object obj) {
		HashMap<String,Object> hb = new HashMap<String,Object>();
		Map<String, Pair<Method, Method>> getsetMethods = BeanPropertyMapper.extractMethods(obj.getClass());
		for (Entry<String, Pair<Method, Method>> entry : getsetMethods.entrySet()) {
			try {
				hb.put(entry.getKey(), entry.getValue().getLeft().invoke(obj));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hb;
	}
}
