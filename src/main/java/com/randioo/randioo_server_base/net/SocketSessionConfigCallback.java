package com.randioo.randioo_server_base.net;

import org.apache.mina.transport.socket.SocketSessionConfig;

public interface SocketSessionConfigCallback {
	void execute(SocketSessionConfig config);
}
