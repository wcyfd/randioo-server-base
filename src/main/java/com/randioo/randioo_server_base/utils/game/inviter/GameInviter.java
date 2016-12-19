package com.randioo.randioo_server_base.utils.game.inviter;

import java.util.concurrent.locks.Lock;

public class GameInviter {
	private InviteHandler handler;

	public void setHandler(InviteHandler handler) {
		this.handler = handler;
	}
	
	/**
	 * 新建邀请
	 * 
	 * @param starter
	 * @param count
	 * @author wcy 2016年12月7日
	 */
	public synchronized void newInvitation(Invitable starter, int count) {
		if(starter == null){
			return;
		}
		Invitation invitation = new Invitation();
		invitation.setSize(count);
		invitation.getInviteSuccessList().add(starter);
		starter.setInvitation(invitation);
	}

	/**
	 * 邀请
	 * 
	 * @param starter
	 * @param targeter
	 * @author wcy 2016年12月7日
	 */
	public void invite(Invitable starter, Invitable targeter) {
		Invitation invitation = starter.getInvitation();
		// 邀请是否存在
		if (invitation == null || invitation.isCancel()) {
			return;
		}

		Lock lock = invitation.getLock();
		try {
			lock.lock();
			if (invitation.isCancel()) {
				return;
			}
			// 检查邀请是否超过数量
			if (invitation.getInviteSuccessList().size() >= invitation.getSize()) {
				handler.inviteOutOfIndex(invitation);
				return;
			}
			handler.receiveInvite(invitation, targeter);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			invitation.getLock().unlock();
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
	public void response(Invitable starter, Invitable targeter, boolean invite) {
		// 如果接受邀请
		Invitation invitation = starter.getInvitation();
		if (invitation == null ||invitation.isCancel()) {
			return;
		}
	
		Lock lock = invitation.getLock();
		try {
			lock.lock();
			if (invitation.isCancel()) {
				handler.inviteCancel(invitation, targeter);
				return;
			}

			if (invite) {
				// 是否仍在邀请中,或者参与的人数已经满了
				if (invitation.getInviteSuccessList().size() >= invitation.getSize()) {
					handler.inviteCancel(invitation, targeter);
					return;
				}// 接受邀请
				invitation.getInviteSuccessList().add(targeter);
				targeter.setInvitation(invitation);
				handler.acceptInvite(invitation, targeter);
				// 检查邀请完毕
				if (this.checkInviteComplete(invitation)) {
					// 邀请完毕
					handler.inviteComplete(invitation);
				}

			} else {
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
	public void cancelInvite(Invitable targeter) {
		Invitation invitation = targeter.getInvitation();
		if (invitation == null||invitation.isCancel()) {
			return;
		}
		Lock lock = invitation.getLock();
		lock.lock();
		try {
			if (invitation.isCancel()) {
				return;
			}
			if (invitation.getInviteSuccessList().contains(targeter)) {
				invitation.getInviteSuccessList().remove(targeter);
				handler.inviteCancel(invitation, targeter);
				targeter.setInvitation(null);
			}
			//如果邀请到的人都退出了，则取消该邀请
			if(invitation.getInviteSuccessList().size() == 0){
				invitation.setCancel(true);				
			}
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
	public void cancelAllInvite(Invitable starter) {
		Invitation invitation = starter.getInvitation();
		if (invitation == null||invitation.isCancel()) {
			return;
		}
		Lock lock = invitation.getLock();
		lock.lock();
		try {
			if (invitation.isCancel()) {
				return;
			}
			for (Invitable invitable : invitation.getInviteSuccessList()) {
				handler.inviteCancel(invitation, invitable);
				invitable.setInvitation(null);
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
	private boolean checkInviteComplete(Invitation invitation) {
		if(invitation.getInviteSuccessList().size() <invitation.getSize()){
			return false;
		}
		return true;
	}
}
