package nivance.jpaclient.test.cass.entity;

import lombok.Data;

import org.apache.avro.reflect.Nullable;

public @Data class CoreBO {

	@Nullable
	private String ltype;
	@Nullable
	private String period;
	@Nullable
	private String merchantid;
	@Nullable
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
	private Integer isvalid = 1;
	@Nullable
	private String region;
	@Nullable
	private String notifystatus;
	@Nullable
	private String wagerstatus;
	@Nullable
	private String allprintresult;
}
