package com.randioo.randioo_server_base.db;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.randioo.randioo_server_base.utils.template.Function;

@Component("gameDB")
public class GameDB {
	private ExecutorService updatePool = Executors.newFixedThreadPool(4);
	private ExecutorService insertPool = Executors.newSingleThreadExecutor();
	private ExecutorService selectPool = Executors.newCachedThreadPool();
	private boolean close;

	public ExecutorService getUpdatePool() {
		return updatePool;
	}

	public ExecutorService getInsertPool() {
		return insertPool;
	}

	public boolean isUpdatePoolClose() {
		return close;
	}

	public ExecutorService getSelectPool() {
		return selectPool;
	}

	public void shutdown(long timeout, TimeUnit unit, Function function) throws InterruptedException {
		close = true;
		updatePool.shutdown();
		System.out.println("gameDB updatePool wait shutdown");
		insertPool.shutdown();
		System.out.println("gameDB insertPool wait shutdown");
		while (!updatePool.awaitTermination(timeout, unit)) {
		}
		System.out.println("gameDB updatePool success shutdown");
		while (!insertPool.awaitTermination(timeout, unit)) {
		}
		System.out.println("gameDB insertPool success shutdown");

		function.apply();

	}
}
