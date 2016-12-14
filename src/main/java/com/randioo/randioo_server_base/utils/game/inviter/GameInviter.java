package com.randioo.randioo_server_base.utils.game.inviter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class GameInviter<T> {
	// 邀请表
	private Map<T, Invitation<T>> requestMap = new ConcurrentHashMap<>();
	// 事件处理器
	private InviteHandler<T> handler;

	public void setHandler(InviteHandler<T> handler) {
		this.handler = handler;
	}

	/**
	 * 新建邀请
	 * 
	 * @param starter
	 * @param count
	 * @author wcy 2016年12月7日
	 */
	public void newInvitation(T starter, int count) {
		Invitation<T> invitation = new Invitation<>();
		invitation.setStarter(starter);
		invitation.setSize(count);
		invitation.getInvitationMap().put(starter, true);
		requestMap.put(starter, invitation);
	}

	/**
	 * 邀请
	 * 
	 * @param starter
	 * @param targeter
	 * @author wcy 2016年12月7日
	 */
	public void invite(T starter, T targeter) {
		Invitation<T> invitation = requestMap.get(starter);
		// 邀请是否存在
		if (invitation == null || invitation.isCancel()) {
			return;
		}

		try {
			invitation.getLock().lock();
			if (invitation.isCancel()) {
				return;
			}
			// 检查邀请是否超过数量
			int size = invitation.getInvitationMap().size();
			if (size >= invitation.getSize()) {
				handler.inviteOutOfIndex(invitation);
				return;
			}
			invitation.getInvitationMap().put(targeter, false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 应答
	 * 
	 * @param starter
	 * @param targeter
	 * @param invite
	 * @author wcy 2016年12月7日
	 */
	public void response(T starter, T targeter, boolean invite) {
		// 如果接受邀请
		Invitation<T> invitation = requestMap.get(starter);
		if (invitation == null || invitation.isCancel()) {
			handler.inviteCancel(invitation, targeter);
		}
		Lock lock = invitation.getLock();
		try {
			if (invitation.isCancel()) {
				handler.inviteCancel(invitation, targeter);
			}

			if (invite) {
				// 是否仍在邀请中
				if (!invitation.getInvitationMap().containsKey(targeter)) {
					handler.inviteCancel(invitation, targeter);
					return;
				}// 接受邀请
				invitation.getInvitationMap().put(targeter, true);
				handler.acceptInvite(invitation, targeter);
				Map<T, Boolean> invitationMap = invitation.getInvitationMap();
				// 检查邀请完毕
				if (this.checkInviteComplete(invitationMap)) {
					//邀请完毕删除该邀请
					requestMap.remove(starter);
					// 邀请完毕
					handler.inviteComplete(invitation);
				}

			} else {
				invitation.getInvitationMap().remove(targeter);
				handler.rejectInvite(invitation, targeter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	/**
	 * 取消邀请
	 * 
	 * @param starter
	 * @param targeter
	 * @author wcy 2016年12月7日
	 */
	public void cancelInvite(T starter, T targeter) {
		Invitation<T> invitation = requestMap.get(starter);
		if (invitation == null) {
			return;
		}
		Lock lock = invitation.getLock();
		lock.lock();
		try {
			if (invitation.isCancel()) {
				return;
			}
			if (invitation.getInvitationMap().containsKey(targeter)) {
				invitation.getInvitationMap().remove(targeter);
				handler.inviteCancel(invitation, targeter);
			}
			invitation.setCancel(true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	/**
	 * 取消所有邀请
	 * 
	 * @param starter
	 * @author wcy 2016年12月7日
	 */
	public void cancelAllInvite(T starter) {
		Invitation<T> invitation = requestMap.get(starter);
		if (invitation == null) {
			return;
		}
		Lock lock = invitation.getLock();
		lock.lock();
		try {
			if (invitation.isCancel()) {
				return;
			}
			Map<T, Boolean> invitationMap = invitation.getInvitationMap();
			for (T t : invitationMap.keySet()) {
				handler.inviteCancel(invitation, t);
			}
			invitation.setCancel(true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	/**
	 * 检查邀请完毕
	 * 
	 * @param targetMap
	 * @return
	 * @author wcy 2016年12月7日
	 */
	private boolean checkInviteComplete(Map<T, Boolean> invitationMap) {
		for (Boolean ready : invitationMap.values()) {
			if (!ready)
				return false;
		}
		return true;
	}
}
