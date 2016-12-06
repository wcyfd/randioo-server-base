package com.randioo.randioo_server_base.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Lock;

import com.google.protobuf.GeneratedMessage;

/**
 * 事件队列
 * 
 * @author wcy 2016年12月5日
 *
 */
public class ActionQueue {
	private PriorityBlockingQueue<GameEvent> inner = new PriorityBlockingQueue<>();
	private Lock lock = null;

	public ActionQueue() {
		try {
			Field field = inner.getClass().getDeclaredField("lock");
			field.setAccessible(true);
			lock = (Lock) field.get(inner);
			field.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void add(GeneratedMessage pOrder, int currentFrameIndex, int executionWaitFrameCount) {
		GameEvent event = new GameEvent(pOrder, currentFrameIndex + executionWaitFrameCount);

		inner.put(event);
	}

	public void remove(GeneratedMessage pOrder) {

		inner.remove(pOrder);

	}

	public boolean ready(int currentFrameIndex) {

		GameEvent order = inner.peek();
		return order != null && order.getExecuteFrameIndex() <= currentFrameIndex;

	}

	public int size() {
		return inner.size();

	}

	public GeneratedMessage poll() {

		GameEvent orderWrapper = inner.poll();
		if (orderWrapper == null) {
			return null;
		}
		return orderWrapper.getAction();

	}

	public List<GameEvent> pollAll(long limitTime) {
		List<GameEvent> array = new ArrayList<>();
		try {
			lock.lock();

			Object[] objs = inner.toArray();
			for (Object obj : objs) {
				GameEvent event = (GameEvent) obj;
				if (event.getExecuteFrameIndex() < limitTime) {
					array.add(event);
					inner.remove(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

		return array;
	}

	public List<GameEvent> pollAllGameEvent() {
		List<GameEvent> array = new ArrayList<>();

		GameEvent orderWrapper = inner.poll();
		while (orderWrapper != null) {
			array.add(orderWrapper);
		}
		return array;

	}

}
