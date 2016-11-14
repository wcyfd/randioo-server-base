package com.randioo.randioo_server_base.net.protocal.randioo;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 消息处理工厂
 *
 */
public class MessageCodecFactory implements ProtocolCodecFactory{
	private final MessageEncoder encoder;
	private final MessageDecoder decoder;
	
	public MessageCodecFactory(Charset charset) {
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
