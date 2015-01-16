package nivance.nio.impl.pack;

import lombok.Data;
import nivance.iso.nio.enums.EResHeader;

import org.apache.commons.lang3.StringUtils;

@Data
public class PackBean {
	public String header[];
	String body;

	public PackBean(String[] header, String body) {
		super();
		this.header = header;
		this.body = body;
		toString();
	}

	String strcache;

	public String toString() {
		if (strcache != null)
			return strcache;
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < header.length; i++) {
			sb.append(',').append(header[i]);
		}
		if(StringUtils.isNotBlank(body)){
			sb.append(",").append(body);
		}
		String strlen = LengthUtil.lengthToString(sb.toString().getBytes().length);
		header[EResHeader.messageLength.ordinal()] = strlen;
		sb.insert(0, strlen);
		strcache = sb.toString();
		return strcache;
	}
}
