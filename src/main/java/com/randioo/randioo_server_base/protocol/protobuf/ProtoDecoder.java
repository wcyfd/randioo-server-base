package com.randioo.randioo_server_base.protocol.protobuf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.InvalidProtocolBufferException;
import com.randioo.randioo_server_base.error.UserFakeMsgException;

public class ProtoDecoder extends CumulativeProtocolDecoder {
	// protected boolean doDecode(IoSession session, IoBuffer in,
	// ProtocolDecoderOutput out) throws Exception {
	//
	// in.order(ByteOrder.LITTLE_ENDIAN);
	//
	// int size = this.readVarInt(in);
	// if (in.remaining() < size + 1) {
	// in.reset();
	// return false;
	// }
	//
	// byte[] data = new byte[size+1];
	//
	// in.get(data);
	//
	// ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
	//
	// out.write(inputStream);
	//
	// if (in.remaining() > 0) {
	// return true;
	// }
	//
	// return false;
	//
	//
	// }

	// protected int readVarInt(IoBuffer in) {
	// in.mark();
	// int result = 0;
	//
	// for (int i = 0;; i += 7) {
	// if (in.remaining() > 1) {
	// byte b = in.get();
	// if (i < 32) {
	// if (b >= 0x80) {
	// result |= ((b & 0x7f) << i);
	// } else {
	// result |= (b << i);
	// break;
	// }
	// } else {
	// result = 0;
	// break;
	// }
	// } else {
	// break;
	// }
	// }
	//
	// in.reset();
	// return result;
	// }

	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

		in.order(ByteOrder.LITTLE_ENDIAN);

		int size = 0;
		try {
			size = readRawVarint32(in);
			if (in.remaining() < size + computeRawVarint32Size(size)) {
				in.reset();
				return false;
			}
		} catch (UserFakeMsgException e) {
			in.reset();
			e.printStackTrace();

			return false;
		}

		byte[] data = new byte[size + computeRawVarint32Size(size)];

		in.get(data);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

		out.write(inputStream);

		if (in.remaining() > 0) {
			return true;
		}

		return false;

	}

	int readRawVarint32(IoBuffer input) throws IOException, UserFakeMsgException {
		input.mark();
		int firstByte = input.get();
		if (firstByte == -1)
			throw new UserFakeMsgException("truncatedMessage");

		int size = readRawVarint32(firstByte, input);
		input.reset();
		return size;
	}

	int readRawVarint32(int firstByte, IoBuffer input) throws IOException, UserFakeMsgException {
		if ((firstByte & 128) == 0)
			return firstByte;
		int result = firstByte & 127;
		int offset;
		for (offset = 7; offset < 32; offset += 7) {
			int b = input.get();
			if (b == -1)
				throw new UserFakeMsgException("truncatedMessage");
			result |= (b & 127) << offset;
			if ((b & 128) == 0)
				return result;
		}

		for (; offset < 64; offset += 7) {
			int b = input.get();
			if (b == -1)
				throw new UserFakeMsgException("truncatedMessage");
			if ((b & 128) == 0)
				return result;
		}
		throw new UserFakeMsgException("malformedVarint");
	}

	int computeRawVarint32Size(int value) {
		if ((value & -128) == 0)
			return 1;
		if ((value & -16384) == 0)
			return 2;
		if ((value & -2097152) == 0)
			return 3;
		return (value & -268435456) != 0 ? 5 : 4;
	}
}
