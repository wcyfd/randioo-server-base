package com.randioo.randioo_server_base.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.randioo.randioo_server_base.net.protocal.http.ServerHttpMessageCodec;

public class WanServer {
	public static void startHttpServer(IoHandlerAdapter handler, InetSocketAddress inetSocketAddress) {
		startServer(new ServerHttpMessageCodec(), handler, inetSocketAddress);
	}

	public static void startServer(IoFilter ioFilter, IoHandlerAdapter handler, InetSocketAddress inetSocketAddress) {
		NioSocketAcceptor ioAcceptor = new NioSocketAcceptor();

		ioAcceptor.getSessionConfig().setReadBufferSize(ServerConfig.getBufferSize());

		ioAcceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, ServerConfig.getIdleTime());

		ioAcceptor.getSessionConfig().setSendBufferSize(ServerConfig.getBufferSize());
		ioAcceptor.getSessionConfig().setTcpNoDelay(true);

		DefaultIoFilterChainBuilder chain = ioAcceptor.getFilterChain();

		chain.addLast("codec", ioFilter);

		chain.addLast("threadpool", new ExecutorFilter(Executors.newCachedThreadPool()));

		ioAcceptor.setHandler(handler);
		try {
			ioAcceptor.bind(inetSocketAddress);
			System.out.println("WANSERVER : START SERVER SUCCESS");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				// WanServer.this.dispose();
			}
		});
	}
}
