package nivance.simplecass.cassandra.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

public class MethodHelper {

	public static Method getMethod(Class<?> clazz,Field field) {
		Method method = null;
		if (field.getType() == Boolean.class) {
			try {
				method = clazz.getMethod("is" + StringUtils.capitalize(field.getName()));
			} catch (Exception e) {
			}
			if (method == null) {
				try {
					method = clazz.getMethod(field.getName());
				} catch (Exception e) {
				}
			}
		}
		if (method == null) {
			try {
				method = clazz.getMethod("get" + StringUtils.capitalize(field.getName()));
			} catch (Exception e) {
			}
		}
		return method;
	}

	public static Method setMethod(Class<?> clazz,Field field) {
		Method method = null;
		if (field.getType() == Boolean.class && field.getName().startsWith("is")) {
			try {
				method = clazz.getMethod(field.getName().substring(2), Boolean.class);
			} catch (Exception e) {
			}
		}
		if (method == null) {
			try {
				method = clazz.getMethod("set" + StringUtils.capitalize(field.getName()), field.getType());
			} catch (Exception e) {
			}
		}
		return method;
	}
}
