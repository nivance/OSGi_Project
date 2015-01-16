package nivance.serialize.test.map;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
public @Data class BOKey extends LimitedNum{
	private String merchantid;
	private String messageid;
	private Long longtype;
	private Short shorttype;
	private Integer inttype;
	private Double doubletype;
	private Byte bytetype;
	private Float floattype;
	private Boolean booleantype;
	private boolean btype;
	private Character chartype;
	private List<Integer> list;
	private List<LimitedNum> limitedNums;
}
