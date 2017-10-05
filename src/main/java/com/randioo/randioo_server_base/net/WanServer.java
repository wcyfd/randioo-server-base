package com.randioo.randioo_server_base.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.randioo.randioo_server_base.config.ServerConfig;
import com.randioo.randioo_server_base.protocol.http.ServerHttpMessageCodec;

public class WanServer {
	private static Logger logger = LoggerFactory.getLogger(WanServer.class.getSimpleName());

	public static void startHttpServer(IoHandlerAdapter handler, InetSocketAddress inetSocketAddress) {
		startServer(new ServerHttpMessageCodec(), null, handler, inetSocketAddress);
	}

	public static void startServer(IoFilter ioFilter, IoFilter heartFilter, IoHandlerAdapter handler,
			InetSocketAddress inetSocketAddress) {
		NioSocketAcceptor ioAcceptor = new NioSocketAcceptor();

		ioAcceptor.getSessionConfig().setReadBufferSize(ServerConfig.getBufferSize());

		ioAcceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, ServerConfig.getIdleTime());

		ioAcceptor.getSessionConfig().setSendBufferSize(ServerConfig.getBufferSize());
		ioAcceptor.getSessionConfig().setTcpNoDelay(true);

		DefaultIoFilterChainBuilder chain = ioAcceptor.getFilterChain();

		chain.addLast("codec", ioFilter);
		if (heartFilter != null)
			chain.addLast("keepalive", heartFilter);

		chain.addLast("threadpool", new ExecutorFilter(Executors.newCachedThreadPool()));

		ioAcceptor.setHandler(handler);
		try {
			ioAcceptor.setReuseAddress(true);
			ioAcceptor.bind(inetSocketAddress);
			logger.info("WANSERVER : START SERVER SUCCESS -> " + handler.getClass().getSimpleName()
					+ " -> socket port:" + inetSocketAddress.getPort());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
