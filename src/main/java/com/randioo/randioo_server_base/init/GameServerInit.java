package com.randioo.randioo_server_base.init;

import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.randioo.randioo_server_base.config.GlobleConfig;
import com.randioo.randioo_server_base.config.GlobleConfig.GlobleEnum;
import com.randioo.randioo_server_base.net.IoHandlerAdapter;
import com.randioo.randioo_server_base.net.WanServer;
import com.randioo.randioo_server_base.protocol.protobuf.ProtoCodecFactory;
import com.randioo.randioo_server_base.scheduler.SchedulerManager;
import com.randioo.randioo_server_base.service.ServiceManager;

@Service
public class GameServerInit {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

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

		WanServer.startServer(new ProtocolCodecFilter(new ProtoCodecFactory()), handler,
				new InetSocketAddress(GlobleConfig.Int(GlobleEnum.PORT)));

		logger.info("socket port:" + GlobleConfig.Int(GlobleEnum.PORT));
	}

}
