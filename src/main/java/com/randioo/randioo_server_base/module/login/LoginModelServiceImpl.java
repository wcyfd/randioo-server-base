package com.randioo.randioo_server_base.module.login;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger logger = LoggerFactory.getLogger(LoginModelService.class);
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

        try {
            reentrantLock.lock();
            // 获得玩家对象
            RoleInterface roleInterface = this.getRoleInterfaceByAccount(account);
            if (roleInterface == null) {
                // 账号不存在，检查帐号格式是否合法
                boolean checkAccount = loginHandler.createRoleCheckAccount(loginInfo, errorCode);
                // 账号格式不合法,返回错误码
                if (!checkAccount) {
                    errorCode.set(LoginModelConstant.GET_ROLE_DATA_NOT_EXIST);
                    return null;
                } else {
                    try {
                        // 帐号合法,创建用户
                        roleInterface = loginHandler.createRole(loginInfo);
                        roleInterface.setCreateTimeStr(TimeUtils.getDetailTimeStr());
                    } catch (Exception e) {
                        logger.error("", e);
                        errorCode.set(LoginModelConstant.CREATE_ROLE_FAILED);
                        return null;
                    }
                }

            }

            int roleId = roleInterface.getRoleId();

            Object lastSession = SessionCache.getSessionById(roleId);

            if (lastSession != null) {
                String lastMac = Session.getAttribute(session, "mac");
                if (lastMac == null || !lastMac.equals(loginInfo.getMacAddress())) {
                    // 通知异地登录
                    loginHandler.noticeOtherPlaceLogin(lastSession);
                    Session.close(lastSession);
                }
            }

            // 设置登陆时间
            roleInterface.setLoginTimeStr(nowTime);

            // session绑定ID
            Session.setAttribute(session, "roleId", roleId);

            Session.setAttribute(session, "mac", loginInfo.getMacAddress());

            // session放入缓存
            SessionCache.addSession(roleId, session);

            // 将数据库中的数据放入缓存中
            RoleCache.putRoleCache(roleInterface);

            return roleInterface;
        } catch (Exception e) {
            logger.error("", e);
            errorCode.set(LoginModelConstant.GET_ROLE_DATA_FAILED);
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
            try {
                lock.lock();
                RoleInterface role2 = RoleCache.getRoleById(roleId);
                if (role2 != null) {
                    return role2;
                }

                loginHandler.loginRoleModuleDataInit(role);
                RoleCache.putRoleCache(role);
            } catch (Exception e) {
                logger.error("", e);
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
            try {
                lock.lock();
                RoleInterface role2 = RoleCache.getRoleByAccount(account);
                if (role2 != null) {
                    return role2;
                }

                loginHandler.loginRoleModuleDataInit(role);
                RoleCache.putRoleCache(role);

            } catch (Exception e) {
                logger.error("", e);
            } finally {
                lock.unlock();
            }

        }
        return role;
    }

    @Override
    public void offline(Object session) {
        Integer roleId = Session.getAttribute(session, "roleId");
        if (roleId == null) {
            return;
        }

        RoleInterface roleInterface = RoleCache.getRoleById(roleId);
        if (roleInterface == null || roleInterface.getAccount() == null) {
            return;
        }

        ReentrantLock reentrantLock = CacheLockUtil.getLock(String.class, roleInterface.getAccount());
        try {
            reentrantLock.lock();

            loginHandler.closeCallback(session);

            // 如果要关闭的session与当前的不同,则不从SessionCache中移除
            Object currentSession = SessionCache.getSessionById(roleId);
            if (currentSession == session) {
                SessionCache.removeSessionById(roleId);
            }
        } catch (Exception e) {
            logger.error("offline error{}", e);
        } finally {
            reentrantLock.unlock();
        }
    }
}
