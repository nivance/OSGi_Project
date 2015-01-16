package nivance.iso.exception;

import org.apache.commons.lang3.StringUtils;

public class IndexBounderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8154850381660098017L;

	public IndexBounderException(String []load,int index) {
		super("MaxLength="+StringUtils.join(load,",")+",index="+index);
	}
	
}
