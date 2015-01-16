package nivance.serialize.test.bean;

import java.math.BigDecimal;

import lombok.Data;

import org.apache.avro.reflect.Nullable;

public @Data class AccAccountA {
	@Nullable
	private String merchantid;
	@Nullable
	private BigDecimal amount;
	@Nullable
	private Long creditamount;
	@Nullable
	private Long maxsaleamount;
	@Nullable
	private Long updatedate;
	
}