package com.randioo.randioo_server_base.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import com.randioo.randioo_server_base.template.EntityRunnable;
import com.randioo.randioo_server_base.utils.HttpUtils;

public class HttpRoleAppender extends AppenderSkeleton {

	private String url;
	private String httpMethod;
	private long closeWaitSecond = 10;
	private String httpKey;
	private String httpKeyValue;
	private String httpRoleKey;
	private String httpGameKey;
	private String httpLogInfoKey;
	private String logRoleKey;
	private String logGameKey;
	private String logSplitPrefix;
	private Map<String, List<String>> map = new HashMap<>();

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
		map.put(httpKey, new ArrayList<String>(1));
		map.get(httpKey).add(httpKeyValue);

		map.put(httpRoleKey, new ArrayList<String>(1));
		map.put(httpGameKey, new ArrayList<String>(1));
		map.put(httpLogInfoKey, new ArrayList<String>(1));
	}

	@Override
	public boolean requiresLayout() {

		return true;
	}

	@Override
	protected void append(LoggingEvent loggingevent) {
		executorService.execute(new EntityRunnable<LoggingEvent>(loggingevent) {

			@Override
			public void run(LoggingEvent loggingevent) {
				String message = loggingevent.getMessage().toString();
				{
					String roleKey = substring(message, logRoleKey, logSplitPrefix);
					List<String> list = map.get(httpRoleKey);
					list.clear();
					list.add(roleKey);
				}

				{
					String gameKey = substring(message, logGameKey, logSplitPrefix);
					List<String> list = map.get(httpGameKey);
					list.clear();
					list.add(gameKey);
				}

				{
					List<String> list = map.get(httpLogInfoKey);
					list.clear();
					list.add(message);
				}

				try {
					HttpUtils.post(url, map, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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

	public String getHttpKey() {
		return httpKey;
	}

	public void setHttpKey(String httpKey) {
		this.httpKey = httpKey;
	}

	public String getHttpKeyValue() {
		return httpKeyValue;
	}

	public void setHttpKeyValue(String httpKeyValue) {
		this.httpKeyValue = httpKeyValue;
	}

	public String getHttpRoleKey() {
		return httpRoleKey;
	}

	public void setHttpRoleKey(String httpRoleKey) {
		this.httpRoleKey = httpRoleKey;
	}

	public String getHttpGameKey() {
		return httpGameKey;
	}

	public void setHttpGameKey(String httpGameKey) {
		this.httpGameKey = httpGameKey;
	}

	public String getHttpLogInfoKey() {
		return httpLogInfoKey;
	}

	public void setHttpLogInfoKey(String httpLogInfoKey) {
		this.httpLogInfoKey = httpLogInfoKey;
	}

	public String getLogRoleKey() {
		return logRoleKey;
	}

	public void setLogRoleKey(String logRoleKey) {
		this.logRoleKey = logRoleKey;
	}

	public String getLogGameKey() {
		return logGameKey;
	}

	public void setLogGameKey(String logGameKey) {
		this.logGameKey = logGameKey;
	}

	public String getLogSplitPrefix() {
		return logSplitPrefix;
	}

	public void setLogSplitPrefix(String logSplitPrefix) {
		this.logSplitPrefix = logSplitPrefix;
	}

	private static String substring(String message, String key, String prefix) {
		int index_1 = message.indexOf(key) + key.length() + 1;
		for (int i = index_1 - 1; i < message.length(); i++) {
			String str = message.substring(i, i + prefix.length());
			if (str.equals(prefix)) {
				return message.substring(index_1 - 1, i);
			}
		}

		return null;
	}

	public static void main(String[] args) {
		String m = "roleId:11,tiger:122,";
		System.out.println(substring(m, "roleId:", ","));
		System.out.println(substring(m, "tiger:", "2,"));
	}
}
