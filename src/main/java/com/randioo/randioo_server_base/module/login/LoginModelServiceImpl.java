package com.randioo.randioo_server_base.module.login;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.cache.SessionCache;
import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.lock.CacheLockUtil;
import com.randioo.randioo_server_base.service.BaseService;
import com.randioo.randioo_server_base.template.Ref;
import com.randioo.randioo_server_base.template.Session;
import com.randioo.randioo_server_base.utils.TimeUtils;

@Service("loginModelService")
public class LoginModelServiceImpl extends BaseService implements LoginModelService {

    /**
     * 
     */
    private static final long serialVersionUID = 4955539585779764566L;

    protected LoginModelServiceImpl() throws RemoteException {
        super();
    }

    private LoginHandler loginHandler;

    @Override
    public void setLoginHandler(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    @Override
    public RoleInterface getRoleData(LoginInfo loginInfo, Ref<Integer> errorCode, Object session) {
        String account = loginInfo.getAccount();
        String nowTime = TimeUtils.getDetailTimeStr();
        ReentrantLock reentrantLock = CacheLockUtil.getLock(String.class, account);
        reentrantLock.lock();

        try {
            // 获得玩家对象
            RoleInterface roleInterface = this.getRoleInterfaceByAccount(account);
            if (roleInterface == null) {
                // 账号不存在，检查帐号格式是否合法
                boolean checkAccount = loginHandler.createRoleCheckAccount(loginInfo, errorCode);
                // 账号格式不合法,返回错误码
                if (!checkAccount) {
                    return null;
                } else {
                    try {
                        // 帐号合法,创建用户
                        roleInterface = loginHandler.createRole(loginInfo);
                        roleInterface.setCreateTimeStr(TimeUtils.getDetailTimeStr());
                    } catch (Exception e) {
                        e.printStackTrace();
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
            Session.setAttribute(session, "roleId", roleId);

            // session放入缓存
            SessionCache.addSession(roleId, session);

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

    @Override
    public RoleInterface getRoleInterfaceById(int roleId) {
        RoleInterface role = RoleCache.getRoleById(roleId);
        if (role == null) {
            role = loginHandler.getRoleInterfaceFromDBById(roleId);
            if (role == null) {
                return null;
            }
            Lock lock = CacheLockUtil.getLock(String.class, role.getAccount());
            lock.lock();
            try {
                RoleInterface role2 = RoleCache.getRoleById(roleId);
                if (role2 != null) {
                    return role2;
                }

                loginHandler.loginRoleModuleDataInit(role);
                RoleCache.putRoleCache(role);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
        return role;
    }

    @Override
    public RoleInterface getRoleInterfaceByAccount(String account) {
        RoleInterface role = RoleCache.getRoleByAccount(account);
        if (role == null) {
            role = loginHandler.getRoleInterfaceFromDBByAccount(account);
            if (role == null) {
                return null;
            }
            Lock lock = CacheLockUtil.getLock(String.class, account);
            lock.lock();
            try {
                RoleInterface role2 = RoleCache.getRoleByAccount(account);
                if (role2 != null) {
                    return role2;
                }

                loginHandler.loginRoleModuleDataInit(role);
                RoleCache.putRoleCache(role);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
        return role;
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

}
