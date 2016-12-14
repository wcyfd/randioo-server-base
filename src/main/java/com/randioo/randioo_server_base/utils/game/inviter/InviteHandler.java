package com.randioo.randioo_server_base.utils.game.inviter;

import java.util.Set;

public interface InviteHandler<T> {
	/**
	 * 接受邀请
	 * 
	 * @param starter 发起者邀请者
	 * @param targeter 邀请目标
	 * @author wcy 2016年12月7日
	 */
	public void acceptInvite(Invitation<T> invitation,T targeter);

	/**
	 * 拒绝邀请
	 * 
	 * @param starter 发起者邀请者
	 * @param targeter 邀请目标
	 * @author wcy 2016年12月7日
	 */
	public void rejectInvite(Invitation<T> invitation,T targeter);

	/**
	 * 邀请被取消
	 * 
	 * @param starter
	 * @param targeter
	 * @author wcy 2016年12月7日
	 */
	public void inviteCancel(Invitation<T> invitation, T targeter);

	/**
	 * 邀请完成
	 * 
	 * @param starter
	 * @author wcy 2016年12月7日
	 */
	public void inviteComplete(Invitation<T> invitation);

	/**
	 * 邀请超过限制
	 * 
	 * @param invitation
	 * @author wcy 2016年12月7日
	 */
	public void inviteOutOfIndex(Invitation<T> invitation);
}
