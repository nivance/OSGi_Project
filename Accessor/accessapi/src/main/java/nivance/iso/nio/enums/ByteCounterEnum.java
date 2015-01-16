package nivance.iso.nio.enums;

/**
 * 可识别长度的enumeration
 * 
 * @author brew
 * 
 */
public class ByteCounterEnum {
	public interface iface {
		public abstract int getByteCount();
	}

	/**
	 * 计算长度
	 * 
	 * @param faceClazz
	 * @return
	 */
	public static int calcLength(Class<?> faceClazz) {
		int count = 0;
		for (Object en : faceClazz.getEnumConstants()) {
			if (en instanceof iface) {
				iface face = (iface) en;
				if (face.getByteCount() > 0) {
					count += face.getByteCount() + 1;
				}
			}
		}
		return count - 1;
	}

}
