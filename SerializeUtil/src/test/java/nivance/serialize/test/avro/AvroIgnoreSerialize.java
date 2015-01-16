package nivance.serialize.test.avro;

import static org.junit.Assert.fail;
import nivance.serialize.SerializerFactory;
import nivance.serialize.SerializerUtil;
import nivance.serialize.SerializerFactory.Type;
import nivance.serialize.test.bean.UserLess_ignore;
import nivance.serialize.test.bean.UserMore_ignore;

import org.junit.Before;
import org.junit.Test;

public class AvroIgnoreSerialize {
	
	private Type avro = SerializerFactory.Type.AVRO;
	
	public static void main(String[] args) {
		AvroIgnoreSerialize serialize = new AvroIgnoreSerialize();
		serialize.testNullSame();
		serialize.testNullDiff1();
		serialize.testNullDiff2();
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNullSame() {
		UserLess_ignore less = new UserLess_ignore();
		less.setName("nameless");
		less.setFavorite_number(1);
		try {
			Object bs = SerializerUtil.serialize(avro, less);
			UserLess_ignore less2 = SerializerUtil.deserialize(avro, bs, UserLess_ignore.class);
			System.out.println(less2);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testNullDiff1() {
		UserLess_ignore less = new UserLess_ignore();
		less.setName("nameless");
		less.setFavorite_number(1);
		try {
			Object bs = SerializerUtil.serialize(avro, less);
			UserMore_ignore more = SerializerUtil.deserialize(avro, bs, UserMore_ignore.class);
			System.out.println(more);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testNullDiff2() {
		UserMore_ignore more = new UserMore_ignore();
		more.setName("nameless");
		more.setFavorite_number(1);
		more.setFavorite_color("red");
		try {
			Object bs = SerializerUtil.serialize(avro, more);
			UserLess_ignore less = SerializerUtil.deserialize(avro, bs, UserLess_ignore.class);
			System.out.println(less);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}


}
