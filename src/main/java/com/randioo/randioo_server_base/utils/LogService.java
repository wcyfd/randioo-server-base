package com.randioo.randioo_server_base.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.randioo.randioo_server_base.utils.template.Function;

public class LogService {
	private static LogService logService = new LogService();

	private LogService() {

	}

	private ExecutorService log = Executors.newSingleThreadScheduledExecutor();

	public static void print(final Function function) {
		logService.log.submit(new Runnable() {

			@Override
			public void run() {
				try {
					Object obj = function.apply();
					System.out.println(obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void print(final String message) {
		logService.log.submit(new Runnable() {

			@Override
			public void run() {
				System.out.println(message);
			}
		});
	}
}
