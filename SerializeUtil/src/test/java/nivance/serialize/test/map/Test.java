package nivance.serialize.test.map;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nivance.serialize.SerializerFactory;
import nivance.serialize.SerializerUtil;
import nivance.serialize.SerializerFactory.Type;

public class Test {
	private static Type avro = SerializerFactory.Type.AVRO;

	public static void main(String[] args) {
		test1();
		test2();
		test5();
	}

	private static void test5() {
		Limite limite = new Limite();
		limite.setBooleantype(true);
		limite.setBtype(true);
		limite.setBytetype((byte)11);
		limite.setDoubletype(1233d);
		limite.setFloattype(123.45f);
		
		LimitedNum num = new LimitedNum();
		num.setLimite(limite);
		num.setLtype("slto");
		num.setPeriod("20140909");
		num.setDecimal(new BigDecimal(90));
		num.setDate(new Date());
		
		System.out.println("0:" + num);
		Object object = SerializerUtil.serialize(avro, num);
		System.out.println("a:" + object);
		LimitedNum num2 = SerializerUtil.deserialize(avro, object, LimitedNum.class);
		System.out.println("d:" + num2);
	}

	public static void test1() {
		BOKey boKey = new BOKey();
		boKey.setLtype("1222");
		boKey.setMerchantid("12312");
		boKey.setMessageid("fdafasf");
		boKey.setPeriod("19820909");


		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			list.add(i);
		}
		boKey.setList(list);

		List<LimitedNum> limitedNums = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			LimitedNum num1 = new LimitedNum();
			num1.setLtype("ltype:" + i);
			num1.setPeriod("period:" + i);
			limitedNums.add(num1);
		}
		boKey.setLimitedNums(limitedNums);

		System.out.println("boKey:" + boKey);
		Object object = SerializerUtil.serialize(avro, boKey);
		BOKey boKey2 = SerializerUtil.deserialize(avro, object, BOKey.class);
		System.out.println("boKey2:" + boKey2);

		System.out.println(limitedNums);
		Object objList = SerializerUtil.serializeArray(avro, limitedNums);
		System.out.println(objList);
		System.out.println(SerializerUtil.deserializeArray(avro, objList,
				LimitedNum.class));
	}
	
	public static void test2(){
		System.out.println(BOKey.class.getClass().isPrimitive());
		System.out.println(List.class.getClass().isPrimitive());
		System.out.println(boolean.class.isPrimitive());
		System.out.println(int.class.isPrimitive());
		System.out.println(char.class.isPrimitive());
		System.out.println(byte.class.isPrimitive());
		System.out.println(long.class.isPrimitive());
		System.out.println(float.class.isPrimitive());
		System.out.println(short.class.isPrimitive());
		System.out.println(double.class.isPrimitive());
		System.out.println(String.class.isPrimitive());
		System.out.println(Double.class.isPrimitive());
	}

}
