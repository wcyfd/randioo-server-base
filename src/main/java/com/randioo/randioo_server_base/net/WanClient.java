package com.randioo.randioo_server_base.net;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.randioo.randioo_server_base.net.protocal.randioo.Message;
import com.randioo.randioo_server_base.net.protocal.randioo.MessageCodecFactory;

public class WanClient {
	public enum WanClientType {
		TCP;
	}

	private IoSession session = null;
	private IoConnector connector = null;
	private InetSocketAddress inetSocketAddress = null;

	public void startClient(IoHandlerAdapter handler, InetSocketAddress inetSocketAddress, WanClientType type) {
		this.startClient(new ProtocolCodecFilter(new MessageCodecFactory(Charset.forName("UTF-8"))), handler,
				inetSocketAddress, type);
	}

	public void startClient(IoFilter ioFilter, IoHandlerAdapter handler, InetSocketAddress inetSocketAddress,
			WanClientType type) {
		switch (type) {
		case TCP:
			tcp(ioFilter, handler, inetSocketAddress);
			break;
		}

	}

	public void tcp(IoFilter ioFilter, IoHandlerAdapter handler, InetSocketAddress inetSocketAddress) {
		connector = new NioSocketConnector();
		connector.getFilterChain().addLast("codec", ioFilter);
		// connector.getFilterChain().addLast("threadpool", new
		// ExecutorFilter(Executors.newCachedThreadPool()));
		connector.setHandler(handler);
		this.inetSocketAddress = inetSocketAddress;
	}

	private IoSession getSession() {
		if (session == null || !session.isConnected()) {
			try {
				ConnectFuture future = connector.connect(inetSocketAddress);
				future.awaitUninterruptibly();
				session = future.getSession();
			} catch (RuntimeIoException e) {
				e.printStackTrace();
			}
		}
		return session;
	}

	public void send(Message message) {
		getSession().write(message);
	}

}
