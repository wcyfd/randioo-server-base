package com.randioo.randioo_server_base.net.protocal.http;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.http.HttpServerDecoder;
import org.apache.mina.http.HttpServerEncoder;

public class ServerHttpMessageCodec  extends ProtocolCodecFilter {
	private static final String DECODER_STATE_ATT = "http.ds";
	private static final String PARTIAL_HEAD_ATT = "http.ph";
	private static ProtocolEncoder encoder = new ServerHttpMessageEncoder();
	private static ProtocolDecoder decoder = new ServerHttpMessageDecoder();

	public ServerHttpMessageCodec() {
		super(encoder, decoder);
	}

	public void sessionClosed(org.apache.mina.core.filterchain.IoFilter.NextFilter nextFilter, IoSession session)
			throws Exception {
		super.sessionClosed(nextFilter, session);
		session.removeAttribute(DECODER_STATE_ATT);
		session.removeAttribute(PARTIAL_HEAD_ATT);
	}
}
