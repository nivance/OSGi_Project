package nivance.jpaclient.test.demo;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.ReflectionUtils;

public class DataGenUtils {

	public static void setData(final Object obj) {
		Class<?> clazz = obj.getClass();
		for(Field field : getAllField(clazz)){
			ReflectionUtils.makeAccessible(field);
			ReflectionUtils.setField(field, obj, getData(field.getType()));
		}
	}
	
	public static List<Field> getAllField(Class<?> clazz){
		List<Field> list = new ArrayList<>();
		Class<?> targetClass = clazz;
		do {
			Field[] fields = targetClass.getDeclaredFields();
			for (Field field : fields) {
				list.add(field);
			}
			targetClass = targetClass.getSuperclass();
		}
		while (targetClass != null && targetClass != Object.class);
		return list;
	}
	
	private static Object getData(Class<?> clazz){
		String className = clazz.getSimpleName();
		Object result = null;
		switch (className) {
		case "String":
			result = "1";
			break;
		case "Integer":
			result = Integer.valueOf(1);
			break;	
		case "Short":
			result = Short.valueOf((short) 1);
			break;
		case "Long":
			result = 1L;
			break;
		case "BigDecimal":
			result = new BigDecimal("1");
			break;
		case "Date":
			result = new Date();
			break;
		default:
			break;
		}
		return result;
		
	}

}
