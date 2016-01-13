package pro.zhantss.lucene.data.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataHandler {
	
	public static byte[] streamToByteListAndClose(InputStream stream) throws IOException {
		if (stream == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int index = 0;
		while((index = stream.read(buffer)) != -1) {
			baos.write(buffer, 0, index);
		}
		byte[] res = baos.toByteArray();
		stream.close();
		baos.close();
		return res;
	}
	
}
