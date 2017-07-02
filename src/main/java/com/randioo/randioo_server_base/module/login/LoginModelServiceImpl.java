package com.randioo.randioo_server_base.module.login;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.cache.SessionCache;
import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.lock.CacheLockUtil;
import com.randioo.randioo_server_base.service.BaseService;
import com.randioo.randioo_server_base.template.Ref;
import com.randioo.randioo_server_base.utils.TimeUtils;

@Service("loginModelService")
public class LoginModelServiceImpl extends BaseService implements LoginModelService {

	private LoginHandler loginHandler;

	@Override
	public void setLoginHandler(LoginHandler loginHandler) {
		this.loginHandler = loginHandler;
	}

	private LoadingCache<String, RoleInterface> roleCache = CacheBuilder.newBuilder()
			.expireAfterAccess(10, TimeUnit.MINUTES).removalListener(new RemovalListener<String, RoleInterface>() {

				@Override
				public void onRemoval(RemovalNotification<String, RoleInterface> notification) {
					String account = notification.getKey();
					RoleInterface roleInterface = notification.getValue();
					Lock lock = getRoleLock(account);
					try {
						lock.lock();
						loginHandler.saveRole(roleInterface);
					} catch (Exception e) {
						logger.error("remove from cache error", e);
					} finally {
						lock.unlock();
					}
				}
			}).build(new CacheLoader<String, RoleInterface>() {

				@Override
				public RoleInterface load(String account) throws Exception {
					Lock lock = getRoleLock(account);
					try {
						lock.lock();
						RoleInterface role = loginHandler.getRoleInterfaceFromDBByAccount(account);
						if (role == null)
							return null;

						loginHandler.loginRoleModuleDataInit(role);

						putStaticRoleData(role);
						return role;
					} catch (Exception e) {
						logger.error("getRoleData error", e);
					} finally {
						lock.unlock();
					}
					return null;
				}
			});

	@Override
	public boolean login(LoginInfo loginInfo) {
		String account = loginInfo.getAccount();

		Lock lock = getRoleLock(account);
		boolean isNewAccount = false;
		try {
			lock.lock();
			isNewAccount = !RoleCache.getAccountSet().containsKey(account);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			lock.unlock();
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

		Lock lock = getRoleLock(account);
		try {
			lock.lock();
			// 双重检测帐号是否可以注册
			if (!this.canCreate(loginCreateInfo, errorCode)) {
				return false;
			}

			// 创建账号
			try {
				RoleInterface roleInterface = loginHandler.createRole(loginCreateInfo);
				roleInterface.setCreateTimeStr(TimeUtils.getDetailTimeStr());

				this.putNewRole(roleInterface);

				return true;
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
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
		if (RoleCache.getAccountSet().containsKey(info.getAccount())) {
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
		String nowTime = TimeUtils.getDetailTimeStr();
		Lock lock = getRoleLock(account);

		try {
			lock.lock();
			// 获得玩家对象
			RoleInterface roleInterface = this.getRoleInterfaceByAccount(account);
			if (roleInterface == null) {
				errorCode.set(LoginModelConstant.GET_ROLE_DATA_NOT_EXIST);
				return null;
			}

			int roleId = roleInterface.getRoleId();
			IoSession oldSession = SessionCache.getSessionById(roleId);

			if (oldSession != null) {
				// 该账号已登录
				if (oldSession.isConnected()) {
					boolean canSynLogin = loginHandler.canSynLogin();
					if (!canSynLogin) {
						errorCode.set(LoginModelConstant.GET_ROLE_DATA_IN_LOGIN);
						return null;
					}
				}
				sessionBindRole(oldSession, null);
				oldSession.close(true);
			}
			// 设置登陆时间
			roleInterface.setLoginTimeStr(nowTime);

			// session绑定ID
			sessionBindRole(ioSession, roleId);
			// session放入缓存
			SessionCache.addSession(roleId, ioSession);

			return roleInterface;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public RoleInterface getRoleInterfaceById(int roleId) {
		// roleId映射到account
		String account = roleId2Account(roleId);
		if (account == null)
			return null;

		try {
			RoleInterface role = roleCache.get(account);
			return role;
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String roleId2Account(int roleId) {
		String account = RoleCache.getRoleIdAccountMap().get(roleId);
		if (account == null) {
			account = loginHandler.getAccountFromDBById(roleId);
		}
		return account;
	}

	@Override
	public Lock getRoleLock(String account) {
		return CacheLockUtil.getLock(RoleInterface.class, account);
	}

	@Override
	public RoleInterface getRoleInterfaceByAccount(String account) {
		try {
			RoleInterface role = roleCache.get(account);
			return role;
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	private void putStaticRoleData(RoleInterface roleInterface) {
		// 添加roleId和帐号的映射
		RoleCache.getRoleIdAccountMap().put(roleInterface.getRoleId(), roleInterface.getAccount());
		// 添加帐号集
		RoleCache.getAccountSet().put(roleInterface.getAccount(), false);
		RoleCache.getNameSet().put(roleInterface.getName(), false);
	}

	private void putNewRole(RoleInterface roleInterface) {
		roleCache.put(roleInterface.getAccount(), roleInterface);

		this.putStaticRoleData(roleInterface);
	}

	@Override
	public RoleInterface getRoleBySession(IoSession ioSession) {
		try {
			Integer roleId = (Integer) getSessionBindValue(ioSession);
			if (roleId == null)
				return null;
			RoleInterface role = this.getRoleInterfaceById(roleId);
			if (role == null)
				return null;
			return role;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * session绑定玩家
	 * 
	 * @param session
	 * @param value
	 */
	private void sessionBindRole(IoSession session, Object value) {
		session.setAttribute(getSessionKey(), value);
	}

	private Object getSessionBindValue(IoSession session) {
		return session.getAttribute(getSessionKey());
	}

	/**
	 * 
	 * @return
	 */
	private Object getSessionKey() {
		return "roleId";
	}

}
