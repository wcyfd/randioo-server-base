package com.randioo.randioo_server_base.protocol.protobuf;

import java.io.ByteArrayOutputStream;
import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.google.protobuf.GeneratedMessage;

/**
 * 
 * @author wcy 2017年8月17日
 *
 */
public class ProtoEncoder extends ProtocolEncoderAdapter {
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        GeneratedMessage sc = (GeneratedMessage) message;

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