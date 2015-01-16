package nivance.serialize.test.map;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

import org.apache.avro.reflect.Nullable;

public @Data
class LimitedNum {

	@Nullable
	private String ltype;
	@Nullable
	private String period;
	private BigDecimal decimal;
	private Limite limite;
	private Date date;
}
