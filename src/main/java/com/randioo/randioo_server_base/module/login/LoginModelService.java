package com.randioo.randioo_server_base.module.login;

import org.apache.mina.core.session.IoSession;

import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.module.BaseServiceInterface;
import com.randioo.randioo_server_base.utils.template.Ref;

public interface LoginModelService extends BaseServiceInterface{
	void setLoginHandler(LoginHandler handler);
	boolean login(LoginInfo loginInfo);
	boolean createRole(LoginCreateInfo loginInfo,Ref<Integer> errorCode);
	RoleInterface getRoleData(LoginInfo loginInfo,Ref<Integer> errorCode, IoSession ioSession);
}
