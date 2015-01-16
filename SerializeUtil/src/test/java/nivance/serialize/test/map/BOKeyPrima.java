/**
 * 
 */
package nivance.serialize.test.map;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
public @Data
class BOKeyPrima extends LimitedNum {

	private String merchantid;
	private String messageid;
	private long longtype;
	private short shorttype;
	private int inttype;
	private double doubletype;
	private byte bytetype;
	private float floattype;
	private Boolean booleantype;
	private boolean btype;
	private char chartype;
	private List<Integer> list;
	private List<LimitedNum> limitedNums;

}
