package com.randioo.randioo_server_base.utils.game.inviter;


public interface InviteHandler {
	/**
	 * 接受邀请
	 * 
	 * @param starter 发起者邀请者
	 * @param targeter 邀请目标
	 * @author wcy 2016年12月7日
	 */
	public void acceptInvite(Invitation invitation,Invitable targeter);

	/**
	 * 拒绝邀请
	 * 
	 * @param starter 发起者邀请者
	 * @param targeter 邀请目标
	 * @author wcy 2016年12月7日
	 */
	public void rejectInvite(Invitation invitation,Invitable targeter);

	/**
	 * 邀请被取消
	 * 
	 * @param starter
	 * @param targeter
	 * @author wcy 2016年12月7日
	 */
	public void inviteCancel(Invitation invitation, Invitable targeter);

	/**
	 * 邀请完成
	 * 
	 * @param starter
	 * @author wcy 2016年12月7日
	 */
	public void inviteComplete(Invitation invitation);

	/**
	 * 邀请超过限制
	 * 
	 * @param invitation
	 * @author wcy 2016年12月7日
	 */
	public void inviteOutOfIndex(Invitation invitation);
	
	/**
	 * 收到邀请
	 * @param invitation
	 * @author wcy 2016年12月16日
	 */
	public void receiveInvite(Invitation invitation, Invitable targeter);
}
