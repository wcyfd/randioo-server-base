package com.randioo.randioo_server_base.protocol.protobuf;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.MessageLite;

/**
 * 
 * @author wcy 2017年8月17日
 *
 */
public class ProtoDecoder extends CumulativeProtocolDecoder {

    private final MessageLite prototype;
    private final ExtensionRegistryLite extensionRegistry;

    public ProtoDecoder(MessageLite messageLite, ExtensionRegistryLite extensionRegistry) {
        this.prototype = messageLite.getDefaultInstanceForType();
        this.extensionRegistry = extensionRegistry;
    }

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        // 如果没有接收完Header部分（4字节），直接返回false
        in.order(ByteOrder.LITTLE_ENDIAN);

        in.mark();
        int preIndex = in.markValue();
        int length = readRawVarint32(in);
        if (preIndex == in.position()) {
            return false;
        }
        if (length < 0) {
            throw new RuntimeException("negative length: " + length);
        }

        if (in.remaining() < length) {
            in.reset();
            return false;
        }

        byte[] bodyBytes = new byte[length];
        in.get(bodyBytes); // 读取body部分
        if (extensionRegistry == null) {
            out.write(prototype.newBuilderForType().mergeFrom(bodyBytes, 0, length).build());
        } else {
            out.write(prototype.newBuilderForType().mergeFrom(bodyBytes, 0, length, extensionRegistry).build());
        }
        return true;
    }

    /**
     * Reads variable length 32bit int from buffer
     *
     * @return decoded int if buffers readerIndex has been forwarded else
     *         nonsense value
     */
    private static int readRawVarint32(IoBuffer buffer) {
        if (!buffer.hasRemaining()) {
            return 0;
        }
        buffer.mark();
        byte tmp = buffer.get();
        if (tmp >= 0) {
            return tmp;
        } else {
            int result = tmp & 127;
            if (!buffer.hasRemaining()) {
                buffer.reset();
                return 0;
            }
            if ((tmp = buffer.get()) >= 0) {
                result |= tmp << 7;
            } else {
                result |= (tmp & 127) << 7;
                if (!buffer.hasRemaining()) {
                    buffer.reset();
                    return 0;
                }
                if ((tmp = buffer.get()) >= 0) {
                    result |= tmp << 14;
                } else {
                    result |= (tmp & 127) << 14;
                    if (!buffer.hasRemaining()) {
                        buffer.reset();
                        return 0;
                    }
                    if ((tmp = buffer.get()) >= 0) {
                        result |= tmp << 21;
                    } else {
                        result |= (tmp & 127) << 21;
                        if (!buffer.hasRemaining()) {
                            buffer.reset();
                            return 0;
                        }
                        result |= (tmp = buffer.get()) << 28;
                        if (tmp < 0) {
                            throw new RuntimeException("malformed varint.");
                        }
                    }
                }
            }
            return result;
        }
    }

}
