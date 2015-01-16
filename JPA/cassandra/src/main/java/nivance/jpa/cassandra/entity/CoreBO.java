package nivance.jpa.cassandra.entity;

import lombok.Data;
import nivance.jpa.cassandra.prepare.mapping.Indexed;
import nivance.jpa.cassandra.prepare.mapping.KeyColumn;
import nivance.jpa.cassandra.prepare.mapping.Table;
import nivance.jpa.cassandra.prepare.schema.KeyPart;

import org.apache.avro.reflect.Nullable;

@Table(name="t_core_bo")
public @Data class CoreBO {

	@Nullable
	@KeyColumn(keyPart = KeyPart.PARTITION, ordinal = 1)
	private String ltype;
	@Nullable
	@KeyColumn(keyPart = KeyPart.PARTITION, ordinal = 2)
	private String period;
	@Nullable
	@KeyColumn(keyPart = KeyPart.PARTITION, ordinal = 3)
	private String merchantid;
	@Nullable
	@KeyColumn(keyPart = KeyPart.CLUSTERING, ordinal = 1)
	private String messageid;
	
	@Nullable
	private String command;
	@Nullable
	private String version;
	@Nullable
	private String encrypt;
	@Nullable
	private String compress;
	@Nullable
	private String requesttime;
	@Nullable
	@Indexed
	private Integer isvalid = 1;
	@Nullable
	private String region;
	@Nullable
	private String notifystatus;
	@Nullable
	private String wagerstatus;
	@Nullable
	private String allprintresult;
	@Nullable
	private String queriedstatus;
}
