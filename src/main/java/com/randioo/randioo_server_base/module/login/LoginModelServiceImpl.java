package com.randioo.randioo_server_base.module.login;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;

import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.cache.SessionCache;
import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.module.BaseService;
import com.randioo.randioo_server_base.net.CacheLockUtil;
import com.randioo.randioo_server_base.utils.TimeUtils;
import com.randioo.randioo_server_base.utils.template.Ref;

@Service("loginModelService")
public class LoginModelServiceImpl extends BaseService implements LoginModelService {

	private LoginHandler loginHandler;

	@Override
	public void setLoginHandler(LoginHandler loginHandler) {
		this.loginHandler = loginHandler;
	}

	@Override
	public boolean login(LoginInfo loginInfo) {
		String account = loginInfo.getAccount();

		ReentrantLock reentrantLock = CacheLockUtil.getLock(String.class, account);
		reentrantLock.lock();

		boolean isNewAccount = false;
		try {
			isNewAccount = RoleCache.getRoleByAccount(loginInfo.getAccount()) == null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reentrantLock.unlock();
		}
		return isNewAccount;
	}

	@Override
	public boolean createRole(LoginCreateInfo loginCreateInfo, Ref<Integer> errorCode) {
		String account = loginCreateInfo.getAccount();
		// 双重检测帐号是否可以注册
		if (!this.canCreate(loginCreateInfo, errorCode)) {
			return false;
		}

		ReentrantLock reentrantLock = CacheLockUtil.getLock(String.class, account);
		reentrantLock.lock();
		try {
			// 双重检测帐号是否可以注册
			if (!this.canCreate(loginCreateInfo, errorCode)) {
				return false;
			}

			// 创建账号
			try {
				RoleInterface roleInterface = loginHandler.createRole(loginCreateInfo);
				roleInterface.setCreateTime(TimeUtils.getNowTime());

				RoleCache.putNewRole(roleInterface);

				return true;
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reentrantLock.unlock();
		}
		errorCode.set(LoginModelConstant.CREATE_ROLE_FAILED);
		return false;

	}

	/**
	 * 能否创建
	 * 
	 * @param info
	 * @param errorCode
	 * @return
	 * @author wcy 2017年2月17日
	 */
	private boolean canCreate(LoginCreateInfo info, Ref<Integer> errorCode) {
		RoleInterface roleInterface = RoleCache.getRoleByAccount(info.getAccount());
		if (roleInterface != null) {
			errorCode.set(LoginModelConstant.CREATE_ROLE_EXIST);
			return false;
		}
		if (!loginHandler.createRoleCheckAccount(info, errorCode)) {
			return false;
		}
		return true;
	}

	@Override
	public RoleInterface getRoleData(LoginInfo loginInfo, Ref<Integer> errorCode, IoSession ioSession) {
		String account = loginInfo.getAccount();
		int nowTime = TimeUtils.getNowTime();
		ReentrantLock reentrantLock = CacheLockUtil.getLock(String.class, account);
		reentrantLock.lock();

		try {
			// 获得玩家对象
			RoleInterface roleInterface = loginHandler.getRoleInterface(loginInfo);
			if (roleInterface == null) {
				errorCode.set(LoginModelConstant.GET_ROLE_DATA_NOT_EXIST);
				return null;
			}

			int roleId = roleInterface.getRoleId();
			IoSession oldSession = SessionCache.getSessionById(roleId);

			if (oldSession != null) {
				// 该账号已登录
				if (oldSession.isConnected()) {
					boolean connectingError = loginHandler.canSynLogin();
					errorCode.set(LoginModelConstant.GET_ROLE_DATA_IN_LOGIN);
					if (!connectingError) {
						return null;
					}
				} else {
					// 设置登陆时间
					roleInterface.setLoginTime(nowTime);
				}
				oldSession.setAttribute("roleId", null);
				oldSession.close(false);
			} else {
				// 设置登陆时间
				roleInterface.setLoginTime(nowTime);
			}

			// session绑定ID
			ioSession.setAttribute("roleId", roleId);
			// session放入缓存
			SessionCache.addSession(roleId, ioSession);

			// 将数据库中的数据放入缓存中
			RoleCache.putRoleCache(roleInterface);

			return roleInterface;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			reentrantLock.unlock();
		}
	}

}
