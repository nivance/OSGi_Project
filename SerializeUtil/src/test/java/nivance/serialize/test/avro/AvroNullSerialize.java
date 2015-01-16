package nivance.serialize.test.avro;

import static org.junit.Assert.fail;
import nivance.serialize.SerializerFactory;
import nivance.serialize.SerializerUtil;
import nivance.serialize.SerializerFactory.Type;
import nivance.serialize.test.bean.UserLess_null;
import nivance.serialize.test.bean.UserMore_null;

import org.junit.Before;
import org.junit.Test;

public class AvroNullSerialize {

	private Type avro = SerializerFactory.Type.AVRO;
	
	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testNullSame() {
		UserLess_null less = new UserLess_null();
		less.setName("nameless");
		try {
			Object bs = SerializerUtil.serialize(avro, less);
			
			UserLess_null less2 = SerializerUtil.deserialize(avro, bs, UserLess_null.class);
			System.out.println(less2);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testNullDiff1() {
		UserLess_null less = new UserLess_null();
		less.setName("nameless");
		try {
			Object bs = SerializerUtil.serialize(avro, less);
			
			UserMore_null more = SerializerUtil.deserialize(avro, bs, UserMore_null.class);
			System.out.println(more);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testNullDiff2() {
		UserLess_null less = new UserLess_null();
		less.setName("nameless");
		less.setFavorite_number(1);
		try {
			Object bs = SerializerUtil.serialize(avro, less);
			
			UserMore_null more = SerializerUtil.deserialize(avro, bs, UserMore_null.class);
			System.out.println(more);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testNullDiff3() {
		UserMore_null more = new UserMore_null();
		more.setName("nameless");
		more.setFavorite_number(1);
		more.setFavorite_color("red");
		try {
			Object bs = SerializerUtil.serialize(avro, more);
			
			UserLess_null less = SerializerUtil.deserialize(avro, bs, UserLess_null.class);
			System.out.println(less);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
}
