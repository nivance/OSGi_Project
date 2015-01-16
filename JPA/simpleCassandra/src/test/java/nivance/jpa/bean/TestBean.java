package nivance.jpa.bean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import nivance.simplecass.cassandra.annotation.Id;
import nivance.simplecass.cassandra.annotation.Qualify;
import nivance.simplecass.cassandra.annotation.Table;
import lombok.Data;

import com.datastax.driver.core.DataType;

@Table(name="testbean")
public @Data class TestBean {

	@Id
	private String key;
	
	@Qualify(type = DataType.Name.BIGINT)
	private Long printtime;
	
	private BigDecimal amount;
	
	@Qualify(type= DataType.Name.SET,typeArguments=DataType.Name.TEXT)
	private Set<String> nset;
	
	@Qualify(type= DataType.Name.LIST,typeArguments=DataType.Name.TEXT)
	private List<String> ors;
	
}
