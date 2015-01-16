package nivance.nio.impl.filter;


import nivance.nio.impl.codec.Decoder;
import nivance.nio.impl.codec.Encoder;
import nivance.nio.impl.pack.PackBean;

import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.filterchain.AbstractCodecFilter;

public class StringTransFilter extends AbstractCodecFilter<Buffer, PackBean> {
	
	public StringTransFilter() {
		super(new Decoder(), new Encoder());
	}

}
