package nivance.simplecass.cassandra.exception;


@SuppressWarnings("serial")
public class CQLGenException  extends Exception{

	public CQLGenException(String message, Throwable cause) {
		super(message, cause);
	}

	public CQLGenException(String message) {
		super(message);
	}

}
