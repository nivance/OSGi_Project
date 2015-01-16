package nivance.nio.impl.codec;

import nivance.nio.impl.pack.PackBean;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.grizzly.AbstractTransformer;
import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.TransformationException;
import org.glassfish.grizzly.TransformationResult;
import org.glassfish.grizzly.attributes.AttributeStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Encoder extends AbstractTransformer<PackBean, Buffer> {

	Logger log = LoggerFactory.getLogger(Encoder.class);
	String charset = System.getProperty("ISO_CHARSET", "UTF-8");

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasInputRemaining(AttributeStorage storage, PackBean input) {
		return input != null;
	}

	@Override
	protected TransformationResult<PackBean, Buffer> transformImpl(
			AttributeStorage storage, PackBean pb)
			throws TransformationException {
		final Buffer output = obtainMemoryManager(storage).allocate(
				pb.toString().getBytes().length);
		try {
			for(int i=0; i<pb.header.length; i++){
				output.put(pb.header[i].getBytes());
				if(i + 1 != pb.header.length)
					output.put((byte) 44);
			}
			if(StringUtils.isNotBlank(pb.getBody())){
				output.put((byte) 44);
				byte[] byteBody = pb.getBody().getBytes(charset);
				output.put(byteBody);
			}
			output.flip();
			output.allowBufferDispose(true);
		} catch (Exception e) {
			log.error("传输信息有误[" + pb + "]", e);
			throw new TransformationException(e);
		}
		return TransformationResult.createCompletedResult(output, null);

	}

}
