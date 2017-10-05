/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.randioo.randioo_server_base.protocol.websocket.mina.protobuf;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.MessageLite;

/**
 * Codec Factory used for creating websocket filter.
 * 
 * @author DHRUV CHOPRA
 */
public class WebSocketProtobufCodecFactory implements ProtocolCodecFactory {
    private ProtocolEncoder encoder;
    private ProtocolDecoder decoder;

    public WebSocketProtobufCodecFactory(MessageLite messageLite) {
        this(messageLite, null);
    }

    public WebSocketProtobufCodecFactory(MessageLite messageLite, ExtensionRegistryLite extensionRegistry) {
        encoder = new WebSocketProtobufEncoder();
        decoder = new WebSocketProtobufDecoder(messageLite, extensionRegistry);
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return encoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return decoder;
    }
}
