package nivance.serialize.test.avro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nivance.serialize.SerializerFactory;
import nivance.serialize.SerializerUtil;
import nivance.serialize.SerializerFactory.Type;
import nivance.serialize.test.bean.AccAccountA;
import nivance.serialize.test.bean.AccAccountB;
import nivance.serialize.test.bean.UserMore_ignore;

public class AvroObjectTest {
	private static Type avro = SerializerFactory.Type.AVRO;

	public static void main(String[] args) {
		objectSerDes();
		accSerDes();
		objectListSerDes();
		accListSerDes();
	}

	public static void objectSerDes() {
		UserMore_ignore more = new UserMore_ignore();
		more.setName("namemore");
		more.setFavorite_number(1);
		more.setFavorite_color("red");
		Object bs = SerializerUtil.serialize(avro, more);

		UserMore_ignore morei = SerializerUtil.deserialize(avro, bs,
				UserMore_ignore.class);
		System.out.println(morei);
	}

	public static void objectListSerDes() {
		List<UserMore_ignore> list = new ArrayList<UserMore_ignore>();
		for (int i = 0; i < 3; i++) {
			UserMore_ignore more = new UserMore_ignore();
			more.setName("namemore-" + i);
			more.setFavorite_number(i);
			more.setFavorite_color("red-" + i);
			list.add(more);
		}
		Object bs = SerializerUtil.serializeArray(avro, list);

		List<UserMore_ignore> list2 = SerializerUtil.deserializeArray(avro, bs,
				UserMore_ignore.class);
		System.out.println(list2);
	}

	public static void accSerDes() {
		AccAccountB b = new AccAccountB();
		b.setMerchantid("12345678");
		b.setAmount(BigDecimal.valueOf(1000l));
		b.setCreditamount(1232l);
		b.setExtrafield1("extrafield1");
		b.setUpdatedate(System.currentTimeMillis());
		b.setMaxsaleamount(100000000l);
		Object bs = SerializerUtil.serialize(avro, b);
		AccAccountA a = SerializerUtil.deserialize(avro, bs, AccAccountA.class);
		System.out.println(a.toString());
	}

	public static void accListSerDes() {
		List<AccAccountB> blist = new ArrayList<>();
		for (int i = 1; i < 4; i++) {
			AccAccountB b = new AccAccountB();
			b.setMerchantid(i + "1234567");
			b.setAmount(BigDecimal.valueOf(1000l * i));
			b.setCreditamount(1232l);
			b.setExtrafield1("extrafield1");
			b.setUpdatedate(System.currentTimeMillis());
			b.setMaxsaleamount(1000l * i);
			blist.add(b);
		}
		Object bs = SerializerUtil.serializeArray(avro, blist);
		List<AccAccountA> alist = SerializerUtil.deserializeArray(avro, bs,
				AccAccountA.class);
		System.out.println(alist.toString());
	}

}
