package com.randioo.randioo_server_base.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.session.IoSession;

import com.randioo.randioo_server_base.entity.RoleInterface;

/**
 * 玩家缓存池
 * 
 * @author wcy 2017年8月5日
 *
 */
public class RoleCache {
    /** 所有玩家缓存信息 */
    /** 玩家id与玩家对象的绑定 */
    private static ConcurrentMap<Integer, RoleInterface> roleMap = new ConcurrentHashMap<>();
    /** 玩家帐号与玩家对象的绑定 */
    private static ConcurrentMap<String, RoleInterface> roleAccountMap = new ConcurrentHashMap<>();
    /** 姓名集合 */
    private static Map<String, String> nameSet = new ConcurrentHashMap<>();
    /** 帐号集合 */
    private static Map<String, String> accountSet = new ConcurrentHashMap<>();

    /**
     * 根据id获取角色缓存
     * 
     * @param id
     * @return
     */
    public static RoleInterface getRoleById(int id) {
        return getRoleMap().get(id);
    }

    /**
     * 获得session绑定的玩家对象<br>
     * 如果没有走登录流程则session无法与role进行绑定<br>
     * 返回的玩家对象为空
     * 
     * @param ioSession
     * @return
     * @author wcy 2017年8月5日
     */
    public static RoleInterface getRoleBySession(IoSession ioSession) {
        try {
            // 从session中获得玩家id,此步骤在登录流程中已经绑定
            Integer roleId = (Integer) ioSession.getAttribute("roleId");
            if (roleId == null)
                return null;
            RoleInterface role = getRoleMap().get(roleId);
            if (role == null)
                return null;
            return role;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更具账号获取角色缓存
     * 
     * @param name
     * @return
     */
    public static RoleInterface getRoleByAccount(String account) {
        return roleAccountMap.get(account);
    }

    /**
     * 获得玩家昵称的集合
     * 
     * @return
     * @author wcy 2017年8月5日
     */
    public static Map<String, String> getNameSet() {
        return nameSet;
    }

    /**
     * 获得玩家帐号的集合
     * 
     * @return
     * @author wcy 2017年8月5日
     */
    public static Map<String, String> getAccountSet() {
        return accountSet;
    }

    /**
     * 获得所有玩家映射表
     * 
     * @return
     * @author wcy 2017年8月5日
     */
    public static ConcurrentMap<Integer, RoleInterface> getRoleMap() {
        return roleMap;
    }

    /**
     * 此方法会造成线程拥堵，使用putRoleCache<br>
     * 此方法已在登录流程中实现，请不要在游戏项目中手动调用<br>
     * 
     * @param role
     * @author wcy 2017年8月5日
     */
    @Deprecated
    public static synchronized void putNewRole(RoleInterface role) {
        roleMap.put(role.getRoleId(), role);
        roleAccountMap.put(role.getAccount(), role);
        accountSet.put(role.getAccount(), role.getAccount());
        nameSet.put(role.getName(), role.getName());
    }

    /**
     * 将玩家对象放入池中
     * 
     * @param role
     * @author wcy 2017年8月5日
     */
    public static void putRoleCache(RoleInterface role) {
        if (!roleMap.containsKey(role.getRoleId())) {
            roleMap.put(role.getRoleId(), role);
            roleAccountMap.put(role.getAccount(), role);
            accountSet.put(role.getAccount(), role.getAccount());
            nameSet.put(role.getName(), role.getName());
        }
    }

    /**
     * 获得账号映射表
     * 
     * @return
     * @author wcy 2017年8月5日
     */
    public static ConcurrentMap<String, RoleInterface> getRoleAccountMap() {
        return roleAccountMap;
    }

}
