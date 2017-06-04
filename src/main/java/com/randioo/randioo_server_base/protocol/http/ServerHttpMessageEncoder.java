package com.randioo.randioo_server_base.protocol.http;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.http.api.DefaultHttpResponse;
import org.apache.mina.http.api.HttpEndOfContent;
import org.apache.mina.http.api.HttpResponse;
import org.apache.mina.http.api.HttpStatus;
import org.apache.mina.http.api.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHttpMessageEncoder implements ProtocolEncoder {
	private static final Logger LOG = LoggerFactory.getLogger(ServerHttpMessageCodec.class);
	private static final CharsetEncoder ENCODER = Charset.forName("UTF-8").newEncoder();

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		LOG.debug("encode {}", message.getClass().getCanonicalName());

		if (message instanceof String) {
			String str = (String) message;
			byte[] resultBytes = str.getBytes(ENCODER.charset());
			int contentLength = resultBytes.length;
			
			Map<String, String> headers = new HashMap<>();
			headers.put("Content-Type", "text/html; charset=utf-8");
			headers.put("Content-Length", Integer.toString(contentLength));
			HttpResponse msg = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SUCCESS_OK, headers);

			StringBuilder sb = new StringBuilder(msg.getStatus().line());
			for (Iterator iterator = msg.getHeaders().entrySet().iterator(); iterator.hasNext(); sb.append("\r\n")) {
				java.util.Map.Entry header = (java.util.Map.Entry) iterator.next();
				sb.append((String) header.getKey());
				sb.append(": ");
				sb.append((String) header.getValue());
			}

			sb.append("\r\n");
			IoBuffer buf = IoBuffer.allocate(sb.length()).setAutoExpand(true);
			buf.putString(sb.toString(), ENCODER);
			buf.flip();
			out.write(buf);

			IoBuffer responseIoBuffer = IoBuffer.allocate(contentLength);
			responseIoBuffer.put(resultBytes);
			responseIoBuffer.flip();
			out.write(responseIoBuffer);
		} else if (message instanceof HttpEndOfContent)
			LOG.debug("End of Content");
	}

	public void dispose(IoSession iosession) throws Exception {
	}
}
