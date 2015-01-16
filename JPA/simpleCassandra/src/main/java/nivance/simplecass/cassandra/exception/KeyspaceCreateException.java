package nivance.simplecass.cassandra.exception;

public class KeyspaceCreateException extends RuntimeException {

	private static final long serialVersionUID = -6846303018139905240L;

	public KeyspaceCreateException() {
		super();
	}

	public KeyspaceCreateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public KeyspaceCreateException(String message, Throwable cause) {
		super(message, cause);
	}

	public KeyspaceCreateException(String message) {
		super(message);
	}

	public KeyspaceCreateException(Throwable cause) {
		super(cause);
	}

}
