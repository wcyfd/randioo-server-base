package com.randioo.randioo_server_base.net.protocal.protobuf;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.randioo.randioo_server_base.net.Protocal;
import com.randioo.randioo_server_base.net.Protocal.PT;

public class ClientMessageDecoder extends CumulativeProtocolDecoder {
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

		in.order(ByteOrder.LITTLE_ENDIAN);

		int remaining = in.remaining();
		if (remaining >= 4) {
			in.mark();

			int size = in.getInt();
			if (size > 2048) {
//				 throw new NumberFakeException("msg size too long");
			}

			if (size > in.remaining()) {
				in.reset();
				return false;
			}

			byte[] data = new byte[size];

			in.get(data);

			IoBuffer params = IoBuffer.wrap(data);
			params.order(ByteOrder.LITTLE_ENDIAN);
			
			PT request = PT.parseFrom(data);
			out.write(request);

			if (in.remaining() > 0) {
				return true;
			}

		}

		return false;
	}
}
