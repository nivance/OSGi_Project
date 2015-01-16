package nivance.serialize.test.bean;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class WrapperList {
	
	private List<Map<String, Object>> list;

}
