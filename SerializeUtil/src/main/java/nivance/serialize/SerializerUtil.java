package nivance.serialize;

import java.util.List;

/**
 * 序列化工具类
 * 
 */
public class SerializerUtil {

	public static <T> Object serialize(SerializerFactory.Type type, T data) {
		return SerializerFactory.getSerializer(type).serialize(data);
	}

	public static <T> T deserialize(SerializerFactory.Type type, Object data,
			Class<T> clazz) {
		return SerializerFactory.getSerializer(type).deserialize(data, clazz);
	}

	public static <T> Object serializeArray(SerializerFactory.Type type,
			List<T> list) {
		return SerializerFactory.getSerializer(type).serializeArray(list);
	}

	public static <T> List<T> deserializeArray(SerializerFactory.Type type,
			Object datas, Class<T> clazz) {
		return SerializerFactory.getSerializer(type).deserializeArray(datas,
				clazz);
	}

}
