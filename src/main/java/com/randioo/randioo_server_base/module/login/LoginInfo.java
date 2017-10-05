package com.randioo.randioo_server_base.module.login;

/**
 * 登录信息
 * 
 * @author wcy 2017年2月17日
 *
 */
public class LoginInfo {
	/** 帐号 */
	private String account;
	/** mac地址 */
	private String macAddress;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LoginInfo [account=").append(account).append(", macAddress=").append(macAddress).append("]");
        return builder.toString();
    }

}
