package com.randioo.randioo_server_base.net;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;


public class IoHandlerAdapter implements IoHandler {

	public void sessionCreated(IoSession session) throws Exception {
		// Empty handler
	}

	public void sessionOpened(IoSession session) throws Exception {
		// Empty handler
	}

	public void sessionClosed(IoSession session) throws Exception {
		// Empty handler
	}

	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		// Empty handler
	}

	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
	}

	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// Empty handler
	}

	public void messageSent(IoSession session, Object message) throws Exception {
		// Empty handler
	}
}
