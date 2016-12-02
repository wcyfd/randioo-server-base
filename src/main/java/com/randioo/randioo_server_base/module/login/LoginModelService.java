package com.randioo.randioo_server_base.module.login;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.randioo_server_base.module.BaseServiceInterface;

public interface LoginModelService extends BaseServiceInterface{
	Object login(Object msg);
	Object creatRole(Object msg);
	Object getRoleData(Object requestMessage, IoSession ioSession);
}
