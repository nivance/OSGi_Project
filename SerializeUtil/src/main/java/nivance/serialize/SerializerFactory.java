package nivance.serialize;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 序列化工厂 定义序列化类型，注册序列化对象，查找序列化对象 开发者可以使用其他序列化方式，使用时只要把自定义序列化对象注册到该工厂中
 * 
 */
public class SerializerFactory {
	
	public enum Type{
		AVRO,
		JSON,
		PROTOBUF
	}

	// AVRO序列化
	protected static final byte SERIALIZER_AVRO = (byte) 0x00;
	// JSON序列化
	protected static final byte SERIALIZER_JSON = (byte) 0x01;
	// protobuf序列化
	protected static final byte SERIALIZER_PROTOBUF = (byte) 0x02;

	private static Map<Type, ISerializer> serializerHandlerMap = new HashMap<Type, ISerializer>();

	static {
		SerializerFactory.registerSerializer(Type.AVRO,
				AvroSerializer.getInstance());
		SerializerFactory.registerSerializer(Type.JSON,
				JsonSerializer.getInstance());
		SerializerFactory.registerSerializer(Type.PROTOBUF,
				ProtobufSerializer.getInstance());
	}

	private static void registerSerializer(Type type, ISerializer serializer) {
		serializerHandlerMap.put(type, serializer);
	}

	public static ISerializer getSerializer(Type type) {
		return serializerHandlerMap.get(type);
	}

}
