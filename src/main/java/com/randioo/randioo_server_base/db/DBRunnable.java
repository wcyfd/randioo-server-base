package com.randioo.randioo_server_base.db;

/**
 * 数据库线程
 * 
 * @author AIM
 *
 * @param <T>
 */
public abstract class DBRunnable<T> implements Runnable {

	private T entity;

	public DBRunnable(T entity) {
		this.entity = entity;
	}

	@Override
	public void run() {
		try {
			run(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity = null;
	}

	public abstract void run(T entity);

}
