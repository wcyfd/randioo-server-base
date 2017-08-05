package com.randioo.randioo_server_base.entity;

/**
 * 玩家接口
 * 
 * @author wcy 2017年8月5日
 *
 */
public interface RoleInterface {

    /**
     * 玩家id
     * 
     * @param roleId
     * @author wcy 2017年8月5日
     */
    public void setRoleId(int roleId);

    public int getRoleId();

    /**
     * 玩家帐号
     * 
     * @return
     * @author wcy 2017年8月5日
     */
    public String getAccount();

    public void setAccount(String account);

    /**
     * 玩家昵称
     * 
     * @return
     * @author wcy 2017年8月5日
     */
    public String getName();

    public void setName(String name);

    /**
     * 玩家登陆时间
     * 
     * @param loginTimeStr
     * @author wcy 2017年8月5日
     */
    public void setLoginTimeStr(String loginTimeStr);

    public String getLoginTimeStr();

    /**
     * 玩家离线时间
     * 
     * @param offlineTimeStr
     * @author wcy 2017年8月5日
     */
    public void setOfflineTimeStr(String offlineTimeStr);

    public String getOfflineTimeStr();

    /**
     * 玩家创建时间
     * 
     * @param createTimeStr
     * @author wcy 2017年8月5日
     */
    public void setCreateTimeStr(String createTimeStr);

    public String getCreateTimeStr();

    /**
     * 玩家载入时间
     * 
     * @param loadTimeStr
     * @author wcy 2017年8月5日
     */
    public void setLoadTimeStr(String loadTimeStr);

    public String getLoadTimeStr();
}
