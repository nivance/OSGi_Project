package nivance.serialize.test.bean;

import lombok.Data;

import org.apache.avro.reflect.AvroIgnore;

@Data
public class UserMore_ignore {

	private String name;
	@AvroIgnore
	private Integer favorite_number;
	@AvroIgnore
	private String favorite_color;
	

}
