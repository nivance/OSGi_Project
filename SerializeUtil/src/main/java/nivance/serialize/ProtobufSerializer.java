package nivance.serialize;

import java.lang.reflect.Method;
import java.util.List;

import com.google.protobuf.MessageLite;

class ProtobufSerializer implements ISerializer {

	private final static ProtobufSerializer instance = new ProtobufSerializer();

	private ProtobufSerializer() {
	}

	public static ProtobufSerializer getInstance() {
		return instance;
	}

	@Override
	public <T> Object serialize(T data) {
		if (data != null) {
			return ((MessageLite) data).toByteArray();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(Object data, Class<T> clazz) {
		if (data != null) {
			try {
				Method method = clazz.getMethod(METHOD, data.getClass());
				if (method != null) {
					return (T) method.invoke(clazz, data);
				} else {
					throw new RuntimeException("protocol pojo hasn't method: "
							+ METHOD);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	private static String METHOD = "parseFrom";

	@Override
	public <T> Object serializeArray(List<T> list) {
		return null;
	}

	@Override
	public <T> List<T> deserializeArray(Object datas, Class<T> clazz) {
		return null;
	}
}
