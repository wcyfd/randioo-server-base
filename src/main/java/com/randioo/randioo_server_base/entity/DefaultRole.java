package com.randioo.randioo_server_base.entity;

import com.randioo.randioo_server_base.db.DataEntity;

/**
 * 默认玩家
 * 
 * @author wcy 2017年8月5日
 *
 */
public class DefaultRole extends DataEntity implements RoleInterface {
    /** 玩家id */
    private int roleId;
    /** 玩家账号 */
    private String account;
    /** 玩家名 */
    private String name;
    /** 登陆时间 */
    private String loginTimeStr;
    /** 离线时间 */
    private String offlineTimeStr;
    /** 创建时间 */
    private String createTimeStr;
    /** 载入时间 */
    private String loadTimeStr;

    @Override
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public int getRoleId() {
        return roleId;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        setChange(true);
        this.name = name;
    }

    @Override
    public void setLoginTimeStr(String loginTimeStr) {
        setChange(true);
        this.loginTimeStr = loginTimeStr;
    }

    @Override
    public String getLoginTimeStr() {
        return loginTimeStr;
    }

    @Override
    public void setOfflineTimeStr(String offlineTimeStr) {
        setChange(true);
        this.offlineTimeStr = offlineTimeStr;
    }

    @Override
    public String getOfflineTimeStr() {
        return offlineTimeStr;
    }

    @Override
    public void setCreateTimeStr(String createTimeStr) {
        setChange(true);
        this.createTimeStr = createTimeStr;
    }

    @Override
    public String getCreateTimeStr() {
        return createTimeStr;
    }

    @Override
    public void setLoadTimeStr(String loadTimeStr) {
        setChange(true);
        this.loadTimeStr = loadTimeStr;
    }

    @Override
    public String getLoadTimeStr() {
        return loadTimeStr;
    }

}
