package com.randioo.randioo_server_base.module.login;

import java.util.concurrent.locks.Lock;

import org.apache.mina.core.session.IoSession;

import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.service.BaseServiceInterface;
import com.randioo.randioo_server_base.template.Ref;

public interface LoginModelService extends BaseServiceInterface {
	void setLoginHandler(LoginHandler handler);

	boolean login(LoginInfo loginInfo);

	boolean createRole(LoginCreateInfo loginInfo, Ref<Integer> errorCode);

	RoleInterface getRoleData(LoginInfo loginInfo, Ref<Integer> errorCode, IoSession ioSession);

	RoleInterface getRoleInterfaceById(int roleId);

	RoleInterface getRoleInterfaceByAccount(String account);

	RoleInterface getRoleBySession(IoSession ioSession);

	/**
	 * 
	 * @param account
	 * @return
	 */
	Lock getRoleLock(String account);

}
