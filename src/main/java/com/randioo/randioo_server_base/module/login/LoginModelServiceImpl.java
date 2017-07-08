package com.randioo.randioo_server_base.module.login;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
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

	private LoadingCache<String, RoleInterface> roleCache = null;

	private LoadingCache<Integer, String> roleIdAccountCache = null;

	@Override
	public void init() {
		CacheBuilder<Object, Object> roleCacheBuilder = CacheBuilder.newBuilder();
		loginHandler.buildRoleInterfaceCache(roleCacheBuilder);
		roleCache = roleCacheBuilder.removalListener(new RemovalListener<String, RoleInterface>() {

			@Override
			public void onRemoval(RemovalNotification<String, RoleInterface> notification) {
				String account = notification.getKey();
				RoleInterface roleInterface = notification.getValue();
				Lock lock = getRoleInterfaceLock(account);
				try {
					lock.lock();
					loginHandler.updateRole(roleInterface);
				} catch (Exception e) {
					loggererror("remove from cache error", e);
				} finally {
					lock.unlock();
				}
			}

		}).build(new CacheLoader<String, RoleInterface>() {

			@Override
			public RoleInterface load(String account) throws Exception {
				Lock lock = getRoleInterfaceLock(account);
				try {
					lock.lock();
					RoleInterface role = loginHandler.getRoleInterfaceFromDBByAccount(account);
					if (role == null)
						return null;

					loginHandler.loginRoleModuleDataInit(role);

					putRoleCache(role);
					return role;
				} catch (Exception e) {
					loggererror("getRoleData error", e);
				} finally {
					lock.unlock();
				}
				return null;
			}

		});

		CacheBuilder<Object, Object> roleIdAccountCacheBuilder = CacheBuilder.newBuilder();
		loginHandler.buildRoleIdAccountCache(roleIdAccountCacheBuilder);
		roleIdAccountCache = roleIdAccountCacheBuilder.build(new CacheLoader<Integer, String>() {

			@Override
			public String load(Integer key) throws Exception {
				int roleId = key;
				return loginHandler.getAccountFromDBById(roleId);
			}

		});
	}

	@Override
	public RoleInterface getRoleData(LoginInfo loginInfo, Ref<Integer> errorCode, IoSession session) {
		String account = loginInfo.getAccount();
		String nowTime = TimeUtils.getDetailTimeStr();
		Lock lock = getRoleInterfaceLock(account);

		try {
			lock.lock();
			// 获得玩家对象
			RoleInterface roleInterface = this.getRoleInterfaceByAccount(account);
			if (roleInterface == null) {
				// 账号不存在，检查帐号格式是否合法
				boolean checkAccount = loginHandler.createRoleCheckAccount(loginInfo, errorCode);
				// 账号格式不合法,返回错误码
				if (!checkAccount) {
					errorCode.set(LoginModelConstant.LOGIN_CREATE_ACCOUNT_FORMAT_ERROR);
					return null;
				} else {
					try {
						// 帐号合法,创建用户
						roleInterface = loginHandler.createRole(loginInfo);
						roleInterface.setCreateTimeStr(TimeUtils.getDetailTimeStr());

						this.putNewRole(roleInterface);
					} catch (Exception e) {
						loggererror("create role failed", e);
						errorCode.set(LoginModelConstant.LOGIN_CREATE_ROLE_FAILED);
						return null;
					}
				}
			}

			int roleId = roleInterface.getRoleId();

			// 从该玩家的设备表中获得该设备
			Facility newLoginFacility = this.getFacility(roleId, loginInfo);

			// 如果有旧的连接，则检查该链接的设备号，如果设备号相同，则直接重新连接，如果设备号不相同则提示异地登录
			Facility currentFacility = FacilityCache.getLoginFacilityMap().get(roleId);

			Facility resultFacility = null;
			if (currentFacility != null) {
				// 设备号相同直接重新连接
				if (currentFacility.getMacAddress().equals(newLoginFacility.getMacAddress())) {
					resultFacility = currentFacility;
				} else {
					// 设备号不同
					// 该次登陆的设备如果有旧的链接，则先关闭,然后再进行替换,新设备设置新的连接
					resultFacility = newLoginFacility;
					// 通知异地登陆并直接替换
					loginHandler.noticeOtherPlaceLogin(currentFacility);
				}
			} else {
				// 没有旧连接则直接加入
				resultFacility = newLoginFacility;
			}

			resultFacility.setSession(session);
			FacilityCache.getLoginFacilityMap().put(roleId, resultFacility);
			// 设置登陆时间
			roleInterface.setLoginTimeStr(nowTime);

			// session绑定ID
			sessionBindRole(session, roleId);
			// session放入缓存
			SessionCache.addSession(roleId, session);

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
		String account = null;
		try {
			account = roleIdAccountCache.get(roleId);
		} catch (ExecutionException e) {
			loggererror("function getRoleInterfaceById", e);
		}
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

	@Override
	public Facility getFacility(int roleId, LoginInfo loginInfo) {
		Map<Integer, RoleFacilities> map = FacilityCache.getRoleFacilitiesMap();
		String macAddress = loginInfo.getMacAddress();
		// 缓存里没有去数据库查，如果还是没有则新建一个
		if (!map.containsKey(roleId)) {
			RoleFacilities roleFacilities = new RoleFacilities();
			roleFacilities.setRoleId(roleId);
			map.put(roleId, roleFacilities);
		}

		RoleFacilities roleFacilities = map.get(roleId);

		Map<String, Facility> facilities = roleFacilities.getFacilities();
		if (!facilities.containsKey(macAddress)) {
			Facility facility = loginHandler.getFacilityFromDB(roleId, macAddress);
			if (facility == null) {
				facility = new Facility();
				facility.setMacAddress(loginInfo.getMacAddress());
				facility.setRoleId(roleId);
				facilities.put(macAddress, facility);
				loginHandler.saveFacility(facility);
			}
		}

		Facility facility = facilities.get(macAddress);

		return facility;
	}

	@Override
	public Lock getRoleInterfaceLock(String account) {
		return CacheLockUtil.getLock(RoleInterface.class, account);
	}

	@Override
	public RoleInterface getRoleInterfaceByAccount(String account) {
		try {
			RoleInterface role = roleCache.get(account);

			return role;
		} catch (ExecutionException e) {
			loggererror("function getRoleInterfaceByAccount error", e);
		}
		return null;
	}

	private void putRoleCache(RoleInterface roleInterface) {
		// 添加roleId和帐号的映射
		roleIdAccountCache.put(roleInterface.getRoleId(), roleInterface.getAccount());
	}

	private void putNewRole(RoleInterface roleInterface) {
		// 向缓存中加入该玩家
		roleCache.put(roleInterface.getAccount(), roleInterface);

		this.putRoleCache(roleInterface);
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
			loggererror("function getRoleBySession error", e);
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
		return session.getAttribute(this.getSessionKey());
	}

	/**
	 * 
	 * @return
	 */
	private Object getSessionKey() {
		return "roleId";
	}

}
