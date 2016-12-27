package com.randioo.randioo_server_base.module.invite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 邀请
 * 
 * @author wcy 2016年12月7日
 *
 */
public class Invitation {
	private int size;

	/** 成功邀请的列表 */
	private List<Invitable> inviteSuccessList = new ArrayList<>();

	private Lock lock = new ReentrantLock();

	private boolean cancel;

	public Lock getLock() {
		return lock;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public List<Invitable> getInviteSuccessList() {
		return inviteSuccessList;
	}

}
