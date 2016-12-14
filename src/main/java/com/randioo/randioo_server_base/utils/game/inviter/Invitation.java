package com.randioo.randioo_server_base.utils.game.inviter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 邀请
 * 
 * @author wcy 2016年12月7日
 *
 */
public class Invitation<T> {
	private int size;

	private T starter;

	private Map<T, Boolean> invitationMap = new HashMap<>();

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

	public Map<T, Boolean> getInvitationMap() {
		return invitationMap;
	}

	public T getStarter() {
		return starter;
	}

	public void setStarter(T starter) {
		this.starter = starter;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}
}
