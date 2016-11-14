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
public class ServerMessageCodecFactory implements ProtocolCodecFactory{
	private final ServerMessageEncoder encoder;
	private final ServerMessageDecoder decoder;
	
	public ServerMessageCodecFactory(Charset charset) {
		this.encoder = new ServerMessageEncoder();
		this.decoder = new ServerMessageDecoder();
	}
	
	
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return encoder;
	}

}
