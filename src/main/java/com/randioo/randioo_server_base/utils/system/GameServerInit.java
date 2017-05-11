package com.randioo.randioo_server_base.utils.system;

import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.randioo.randioo_server_base.net.IoHandlerAdapter;
import com.randioo.randioo_server_base.net.WanServer;
import com.randioo.randioo_server_base.net.protocal.protobuf.ProtoCodecFactory;
import com.randioo.randioo_server_base.utils.scheduler.SchedulerManager;
import com.randioo.randioo_server_base.utils.service.ServiceManager;
import com.randioo.randioo_server_base.utils.system.GlobleConfig.GlobleEnum;

@Service
public class GameServerInit {
	@Autowired
	private ServiceManager serviceManager;

	@Autowired
	private SchedulerManager schedulerManager;

	private IoHandlerAdapter handler;

	public GameServerInit setHandler(IoHandlerAdapter handler) {
		this.handler = handler;
		return this;
	}

	public void start() {
		serviceManager.initServices();

		schedulerManager.start();

		WanServer.startServer(new ProtocolCodecFilter(new ProtoCodecFactory()), handler, new InetSocketAddress(
				GlobleConfig.Int(GlobleEnum.PORT)));
		System.out.printf("socket port:%d" + System.lineSeparator(), GlobleConfig.Int(GlobleEnum.PORT));

	}

}
