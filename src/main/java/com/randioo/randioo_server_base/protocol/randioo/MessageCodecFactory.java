package com.randioo.randioo_server_base.protocol.randioo;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MessageCodecFactory implements ProtocolCodecFactory {
	private final MessageEncoder encoder;
	private final MessageDecoder decoder;

	public MessageCodecFactory() {
		this.encoder = new MessageEncoder();
		this.decoder = new MessageDecoder();
	}

	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return encoder;
	}

}
