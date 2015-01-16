package nivance.simplecass.cassandra.entity;

import java.util.List;

import nivance.simplecass.cassandra.annotation.Id;
import nivance.simplecass.cassandra.annotation.Qualify;
import nivance.simplecass.cassandra.annotation.Table;
import lombok.Data;

import com.datastax.driver.core.DataType;

@Table(name="bo")
public @Data class CoreBO {
	@Id
	private String messageid;
	private String ltype;
	private String merchantid;
	private String period;
	private String command;
	private String version;
	private String compress;
	private String requesttime;
	private String region;
	private String allprintresult;
	private Integer encrypt;
	private Integer notifystatus;
	private Integer wagerstatus;
	private Integer queriedstatus;
	private Integer isvalid;
	@Qualify(type= DataType.Name.LIST,typeArguments=DataType.Name.TEXT)
	private List<String> ordernos;
	
}

