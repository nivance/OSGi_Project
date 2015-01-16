package nivance.serialize.test.bean;

import lombok.Data;

import org.apache.avro.reflect.AvroIgnore;

@Data
public class UserLess_ignore {

	private String name;
	@AvroIgnore
	private Integer favorite_number;
	
}
