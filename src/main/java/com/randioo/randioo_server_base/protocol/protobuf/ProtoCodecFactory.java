package com.randioo.randioo_server_base.protocol.protobuf;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.MessageLite;

/**
 * 消息处理工厂
 *
 */
public class ProtoCodecFactory implements ProtocolCodecFactory {
    private ProtoEncoder encoder;
    private ProtoDecoder decoder;

    public ProtoCodecFactory(MessageLite messageLite) {
        this(messageLite, null);
    }

    public ProtoCodecFactory(MessageLite messageLite, ExtensionRegistryLite extensionRegistry) {
        this.encoder = new ProtoEncoder();
        this.decoder = new ProtoDecoder(messageLite, extensionRegistry);
    }

    public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
        return decoder;
    }

    public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
        return encoder;
    }

}
