package nivance.nio.impl.codec;

import java.nio.charset.Charset;

import lombok.extern.slf4j.Slf4j;
import nivance.iso.exception.HeaderTranslateException;
import nivance.iso.nio.enums.EReqHeader;
import nivance.nio.impl.pack.BeanHelper;
import nivance.nio.impl.pack.PackBean;

import org.glassfish.grizzly.AbstractTransformer;
import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.TransformationException;
import org.glassfish.grizzly.TransformationResult;
import org.glassfish.grizzly.attributes.Attribute;
import org.glassfish.grizzly.attributes.AttributeStorage;

@Slf4j
public class Decoder extends AbstractTransformer<Buffer, PackBean> {

	protected final Attribute<String[]> headerAttribute;
	protected final Attribute<byte[]> bufferAttribute;

	public Decoder() {
		headerAttribute = attributeBuilder.createAttribute("Decoder.Header");
		bufferAttribute = attributeBuilder.createAttribute("Decoder.buffer");
	}

	protected Charset charset = Charset.forName(System.getProperty("ISO_CHARSET", "UTF-8"));

	@Override
	public String getName() {
		return null;
	}

	@Override
	protected TransformationResult<Buffer, PackBean> transformImpl(AttributeStorage storage, Buffer input) throws TransformationException {
		Integer headsize = EReqHeader.BytesLength;
		if (input.remaining() < headsize) {
			int len = input.remaining();
			byte[] byteBuff = new byte[len];
			input.get(byteBuff, 0, len);
			String msg = "接受到的数据为[" + new String(byteBuff, charset) + "],只有[" + len + "]个字节,但头部至少需要[" + headsize + "]个字节";
			log.error(msg);
			input.dispose();
			throw new TransformationException(msg);
		}
		byte[] byteBuff = bufferAttribute.get(storage);
		if (byteBuff == null) {
			byteBuff = new byte[EReqHeader.BytesLength + 0x6F];// 每次通讯最多传输62+111个字节
			bufferAttribute.set(storage, byteBuff);
		}
		input.get(byteBuff, 0, headsize);
		String headStr = new String(byteBuff, 0, headsize, charset);
		String headerArr[];
		try {
			headerArr = BeanHelper.buildHeader(headStr);
		} catch (HeaderTranslateException e) {
			log.error("Header Error:,close", e);
			throw new TransformationException("Header Error::", e);
		}
		headerAttribute.set(storage, headerArr);
		Integer bodySize = (int) (Long.parseLong( // 如果bodysize=-1,说明head后没有逗号
				headerArr[EReqHeader.messageLength.ordinal()], 16) - EReqHeader.BytesLength) + 4;
		String stringMessage = null;
		if (bodySize > 0) {
			if (input.remaining() < bodySize) {
				int len = input.remaining();
				input.get(byteBuff, headsize, len);
				String body = new String(byteBuff, headsize, len, charset);
				String msg = "接受到的数据为[" + headStr + body + "],body需要[" + bodySize + "]个字节，但只有[" + len + "]个字节";
				log.error(msg);
				throw new TransformationException(msg);
			} else {
				input.get(byteBuff, headsize, bodySize);
				stringMessage = new String(byteBuff, headsize, bodySize, charset).substring(1);
			}
		}
		log.debug("recv:" + headStr + "," + stringMessage);
		return TransformationResult.createCompletedResult(new PackBean(headerAttribute.get(storage), stringMessage), input);
	}

	@Override
	public void release(AttributeStorage storage) {
		headerAttribute.remove(storage);
		bufferAttribute.remove(storage);
		super.release(storage);
	}

	@Override
	public boolean hasInputRemaining(AttributeStorage storage, Buffer input) {
		return input != null && input.hasRemaining();
	}

}
