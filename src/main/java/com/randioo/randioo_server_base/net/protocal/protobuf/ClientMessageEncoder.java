package com.randioo.randioo_server_base.net.protocal.protobuf;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.randioo.randioo_server_base.net.Protocal;
import com.randioo.randioo_server_base.net.Protocal.PT;

public class ClientMessageEncoder extends ProtocolEncoderAdapter {
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		PT cs = (PT) message;
		byte[] bytes = cs.toByteArray();
		IoBuffer buffer = IoBuffer.allocate(bytes.length + 4).setAutoExpand(true);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		out.write(buffer);
	}
}