package com.randioo.randioo_server_base.module.login;

import java.util.concurrent.locks.Lock;

import org.apache.mina.core.session.IoSession;

import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.service.BaseServiceInterface;
import com.randioo.randioo_server_base.template.Ref;

public interface LoginModelService extends BaseServiceInterface {
	void setLoginHandler(LoginHandler handler);

	RoleInterface getRoleData(LoginInfo loginInfo, Ref<Integer> errorCode, IoSession ioSession);

	RoleInterface getRoleInterfaceById(int roleId);

	RoleInterface getRoleInterfaceByAccount(String account);

	RoleInterface getRoleBySession(IoSession ioSession);

	Facility getFacility(int roleId, LoginInfo loginInfo);

	/**
	 * 
	 * @param account
	 * @return
	 */
	Lock getRoleInterfaceLock(String account);

	/**
	 * 清理
	 */
	void cleanUp();

	/**
	 * 删除所有缓存
	 */
	void invalidate();

}
