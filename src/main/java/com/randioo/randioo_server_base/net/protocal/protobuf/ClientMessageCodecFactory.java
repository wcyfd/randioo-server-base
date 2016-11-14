package com.randioo.randioo_server_base.net.protocal.protobuf;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 消息处理工厂
 *
 */
public class ClientMessageCodecFactory implements ProtocolCodecFactory{
	private final ClientMessageEncoder encoder;
	private final ClientMessageDecoder decoder;
	
	public ClientMessageCodecFactory(Charset charset) {
		this.encoder = new ClientMessageEncoder();
		this.decoder = new ClientMessageDecoder();
	}
	
	
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return encoder;
	}

}
