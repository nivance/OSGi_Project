package nivance.iso.nio.api;

import nivance.iso.exception.IndexBounderException;

public class MessageBean {

	private final static String NONELOAD[] = new String[] {};
	String[] Header = NONELOAD;
	String[] Body = NONELOAD;

	public MessageBean(String []header,String []body){
		Header=header;
		Body=body;
	}
	
	public String header(int eindex) throws IndexBounderException {
		return getFrom(Header, eindex);
	}

	public String header(Enum e) throws IndexBounderException {
		return getFrom(Header, e.ordinal());
	}

	public String body(int eindex) throws IndexBounderException {
		return getFrom(Body, eindex);
	}

	public String body(Enum e) throws IndexBounderException {
		return getFrom(Body, e.ordinal());
	}

	public static String getFrom(String[] load, int index)
			throws IndexBounderException {
		if (load.length >= index) {
			throw new IndexBounderException(load, index);
		}
		return load[index];
	}
}
