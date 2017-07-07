package com.randioo.randioo_server_base.log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import com.randioo.randioo_server_base.utils.HttpUtils;

public class HttpAppender extends AppenderSkeleton {

	private String url;
	private String httpMethod;
	private long closeWaitSecond = 10;
	private String key;
	private ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	@Override
	public void close() {
		executorService.shutdown();
		LogLog.warn("HttpAppender start shutdown");
		try {
			while (!executorService.awaitTermination(closeWaitSecond, TimeUnit.SECONDS)) {
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LogLog.warn("HttpAppender shutdown success");
	}

	@Override
	public void activateOptions() {

	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

	@Override
	protected void append(LoggingEvent loggingevent) {
		String message = loggingevent.getMessage().toString();
		Map<String, List<String>> map = new HashMap<>();

		try {
			HttpUtils.get(url, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("randioo print : " + message);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
