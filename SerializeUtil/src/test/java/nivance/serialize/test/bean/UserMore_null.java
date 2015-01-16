package nivance.serialize.test.bean;

import java.util.Date;

import lombok.Data;

import org.apache.avro.reflect.Nullable;

@Data
public class UserMore_null {

	private String name;
	@Nullable
	private Integer favorite_number;
	@Nullable
	private String favorite_color;
	@Nullable
	private Date date;

}
