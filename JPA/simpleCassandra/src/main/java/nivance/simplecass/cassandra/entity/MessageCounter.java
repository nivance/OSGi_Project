package nivance.simplecass.cassandra.entity;

import nivance.simplecass.cassandra.annotation.Counter;
import nivance.simplecass.cassandra.annotation.Id;
import nivance.simplecass.cassandra.annotation.Table;
import lombok.Data;

@Table(name = "messageCounter")
public @Data class MessageCounter {

	@Id
	private String key;
	@Counter
	private long successnum;
	@Counter
	private long faildnum;
	@Counter
	private long ordernos;
	@Counter
	private long totalnum;

}
