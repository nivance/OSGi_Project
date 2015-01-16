package nivance.nio.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UploadDataClient {
	String host = "127.0.0.1";
	int port=10036;
	Socket socket = null;
	OutputStream ous = null;
	InputStream ins = null;
	
	public UploadDataClient(){
		try {
			socket = new Socket(host , port);
			ous = socket.getOutputStream();
			ins = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 给合并通讯机发送数据
	 * 添加synchronized 避免一个终端同时做多件事情
	 */
	public String sendData(String command, String data)
			throws IOException, InterruptedException {
		/** 上传数据命令完成后返回来的明文数据 */
		String returnData = null;
		byte[] msg = chooseMessage(command, data);
		long start = System.currentTimeMillis();
		try {
			ous.write(msg);
			// 接收服务器的回应
			returnData = waitResponse(ins);
			long end = System.currentTimeMillis();
			log.info("思乐响应明文:"+returnData);
			log.info("SILE COST TIME[" + (end - start) + "]ms.");
			return returnData;
		} finally {
//			if (ins != null)
//				ins.close();
//			if (ous != null)
//				ous.close();
//			if (socket != null) {
//				socket.close();
//			}
		}
	}

	/**
	 * 处理上传数据命令后合并通讯机返回
	 * @throws IOException 
	 */
	public String waitResponse(InputStream ins) throws IOException {
		// 所有响应数据包大小固定为40字节
		byte[] bytes = new byte[1024];
		ins.read(bytes);
		return new String(bytes);
	}

	public byte[] chooseMessage(String command, String data)
			throws UnsupportedEncodingException {
		String msg = "";
		switch (command) {
		case "1000":
//			msg = "003a,1.0.0,1002,1399977068000,550e8400e29b41d4a7164466554400000000000";//bytes overflow
//			msg = "003a,1.0.0,1000,1399977068000,550e8400e29b41d4a7164466554400";//bytes less
//			msg = "003a,1.0.0,1002,139997706,8000550e84,00e29b41d4a71644665544000";//header eroor
			msg = "003a,1.0.0,1000,1399977068000,550e8400e29b41d4a716446655440000";
			break;
		case "1001":
			msg = "003a,1.0.0,1001,1399977068000,550e8400e29b41d4a716446655440000";
			break;
//		case 1002:
//			mingwen.append(Constants.LOTTERY_SERVICEENCASH).append(
//					Constants.TAB);
//			mingwen.append(randInt).append(Constants.TAB);
//			mingwen.append(data.length()).append(Constants.TAB);
//			mingwen.append(data).append(Constants.TAB);
//			break;
//		case 2000:
//			mingwen.append(Constants.LOTTERY_QUERYMONEY).append(Constants.TAB);
//			mingwen.append(randInt).append(Constants.TAB);
//			mingwen.append(data).append(Constants.TAB); // 逻辑机号
//			break;
		default:
			break;
		}
		log.info("发送明文：" + msg);
		return msg.toString().getBytes();
	}

	public static void main(String[] args) throws Exception {
		UploadDataClient u = new UploadDataClient();
		u.sendData("1001", null);
		u.sendData("1000", null);
	}
}
