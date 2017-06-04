package com.randioo.randioo_server_base.protocol.protobuf;

import java.io.ByteArrayOutputStream;
import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.google.protobuf.GeneratedMessage;

public class ProtoEncoder extends ProtocolEncoderAdapter {
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		GeneratedMessage sc = (GeneratedMessage) message;
//		byte[] bytes = sc.toByteArray();
//		IoBuffer buffer = IoBuffer.allocate(bytes.length + 4).setAutoExpand(true);
//		buffer.order(ByteOrder.LITTLE_ENDIAN);
//		buffer.putInt(bytes.length);
//		buffer.put(bytes);
//		buffer.flip();
//		out.write(buffer);		
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		sc.writeDelimitedTo(output);
		byte[] bytes = output.toByteArray();
		output.close();

		IoBuffer buffer = IoBuffer.allocate(bytes.length);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(bytes);
		buffer.flip();
		out.write(buffer);
		
	}
}