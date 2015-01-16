package nivance.iso.exception;
/**
 * 省份接入异常
 * @author Administrator
 *
 */
public class HeaderTranslateException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3951562583119008718L;

	public HeaderTranslateException(String body) {
		super("Header2BeanError:"+":"+body);
	}


}
