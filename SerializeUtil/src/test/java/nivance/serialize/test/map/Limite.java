/**
 * 
 */
package nivance.serialize.test.map;

import lombok.Data;
/**
 * 注：KeyPart.PARTITION 作为分区主键必须填写
 * @author JV
 *
 */
public @Data class Limite {
	private double doubletype;
	private byte bytetype;
	private Float floattype;
	private Boolean booleantype;
	private boolean btype;
}
