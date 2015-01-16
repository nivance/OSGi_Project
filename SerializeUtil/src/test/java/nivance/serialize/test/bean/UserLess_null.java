package nivance.serialize.test.bean;

import java.sql.Timestamp;
import java.util.Date;

import lombok.Data;

import org.apache.avro.reflect.Nullable;

@Data
public class UserLess_null {

	private String name;
	@Nullable
	private Integer favorite_number;
	@Nullable
	private Date date;
	@Nullable
	private Timestamp timestamp;
}
