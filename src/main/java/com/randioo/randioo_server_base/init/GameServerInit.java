package com.randioo.randioo_server_base.init;

import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.randioo.randioo_server_base.GlobleConstant;
import com.randioo.randioo_server_base.config.GlobleMap;
import com.randioo.randioo_server_base.handler.GameServerHandlerAdapter;
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

	@Autowired
	private GameServerHandlerAdapter gameServerHandlerAdapter;

	private KeepAliveFilter keepAliveFilter;

	public void setKeepAliveFilter(KeepAliveFilter keepAliveFilter) {
		this.keepAliveFilter = keepAliveFilter;
	}

	public void start() {
		serviceManager.initServices();
		logger.info("init Services");

		schedulerManager.start();
		logger.info("scheduler start");

		WanServer.startServer(new ProtocolCodecFilter(new ProtoCodecFactory()), keepAliveFilter,
				gameServerHandlerAdapter, new InetSocketAddress(GlobleMap.Int(GlobleConstant.ARGS_PORT)));
		logger.info("socket start");
	}
}
