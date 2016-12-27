package com.randioo.randioo_server_base.module.invite;

/**
 * 可邀请的
 * 
 * @author wcy 2016年12月16日
 *
 */
public interface Invitable {
	public Invitation getInvitation();

	public void setInvitation(Invitation invitation);
}
