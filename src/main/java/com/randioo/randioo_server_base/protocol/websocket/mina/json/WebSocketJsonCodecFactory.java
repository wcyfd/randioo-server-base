/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.randioo.randioo_server_base.protocol.websocket.mina.json;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * Codec Factory used for creating websocket filter.
 * 
 * @author DHRUV CHOPRA
 */
public class WebSocketJsonCodecFactory implements ProtocolCodecFactory {
    private ProtocolEncoder encoder;
    private ProtocolDecoder decoder;

    public WebSocketJsonCodecFactory() {
        encoder = new WebSocketJsonEncoder();
        decoder = new WebSocketJsonDecoder();
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
