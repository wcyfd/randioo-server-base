package com.randioo.randioo_server_base.module.login;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.randioo_server_base.module.BaseServiceInterface;

public interface LoginModelService extends BaseServiceInterface{
	GeneratedMessage login(GeneratedMessage msg);
	GeneratedMessage creatRole(GeneratedMessage msg);
	GeneratedMessage getRoleData(GeneratedMessage requestMessage, IoSession ioSession);
}
