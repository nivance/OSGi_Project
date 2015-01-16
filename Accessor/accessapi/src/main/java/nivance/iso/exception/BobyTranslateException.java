package nivance.iso.exception;
/**
 * 省份接入异常
 * @author Administrator
 *
 */
public class BobyTranslateException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3951562583119008718L;

	public BobyTranslateException(int beanid,String body) {
		super("String2BeanError:"+beanid+":"+body);
	}


}
