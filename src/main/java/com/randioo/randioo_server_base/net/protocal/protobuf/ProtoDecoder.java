package com.randioo.randioo_server_base.net.protocal.protobuf;

import java.io.ByteArrayInputStream;
import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class ProtoDecoder extends CumulativeProtocolDecoder {
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		
		in.order(ByteOrder.LITTLE_ENDIAN);

		int size = this.readVarInt(in);
		if (in.remaining() < size + 1) {
			in.reset();
			return false;
		}

		byte[] data = new byte[size+1];

		in.get(data);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

		out.write(inputStream);

		if (in.remaining() > 0) {
			return true;
		}
		
		return false;		

		
	}
	
	protected int readVarInt(IoBuffer in) {
		in.mark();
		int result = 0;

		for (int i = 0;; i += 7) {
			if (in.remaining() > 1) {
				byte b = in.get();
				if (i < 32) {
					if (b >= 0x80) {
						result |= ((b & 0x7f) << i);
					} else {
						result |= (b << i);
						break;
					}
				} else {
					result = 0;
					break;
				}
			} else {
				break;
			}
		}

		in.reset();
		return result;
	}

}
