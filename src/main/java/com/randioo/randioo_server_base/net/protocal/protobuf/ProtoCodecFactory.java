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
public class ProtoCodecFactory implements ProtocolCodecFactory{
	private final ProtoEncoder encoder;
	private final ProtoDecoder decoder;
	
	public ProtoCodecFactory() {
		this.encoder = new ProtoEncoder();
		this.decoder = new ProtoDecoder();
	}
	
	
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return encoder;
	}

}
