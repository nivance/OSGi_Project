package nivance.nio.impl.pack;

public class LengthUtil {

	public static String lengthToString(int len){
		String result=Integer.toHexString(len);
		while(result.length()<4){
			result="0"+result;
		}
		return result;
	}
	
	public static int lengthFrom(String str){
		int startIndex=0;
		while(startIndex<str.length()&&str.indexOf(startIndex)=='0')startIndex++;
		if(startIndex>=0&&startIndex<str.length()){
			return Integer.parseInt(str.substring(startIndex),16);
		}
		return 0;
	}
	
}
