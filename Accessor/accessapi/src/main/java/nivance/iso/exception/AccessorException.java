package nivance.iso.exception;
/**
 * 省份接入异常
 * @author Administrator
 *
 */
public class AccessorException extends Exception {

	private static final long serialVersionUID = 1971444519374477593L;

	public AccessorException() {
		super();
	}

	public AccessorException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccessorException(String message) {
		super(message);
	}

	public AccessorException(Throwable cause) {
		super(cause);
	}

}
