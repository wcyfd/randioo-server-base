package com.randioo.randioo_server_base.log;

import java.net.ConnectException;
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
	private long closeWaitSecond = 10;
	private String httpKey = "key";
	private String httpKeyValue;
	private String httpRoleKey = "userId";
	private String httpGameKey = "gameName";
	private String httpLogInfoKey = "logInfo";
	private String logRoleKey = "roleId:";
	private String logSplitPrefix = ",";

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

		List<String> gameList = new ArrayList<>(1);
		gameList.add(HttpLogUtils.projectName);

		map.put(httpGameKey, gameList);
		map.put(httpRoleKey, new ArrayList<String>(1));
		map.put(httpLogInfoKey, new ArrayList<String>(1));
	}

	@Override
	public boolean requiresLayout() {

		return true;
	}

	@Override
	protected void append(LoggingEvent loggingevent) {
		String value = loggingevent.getMessage().toString();
		boolean roleOrSys = value.startsWith("<ROLE>") || value.startsWith("<SYS>");
		if (!roleOrSys)
			return;
		executorService.execute(new EntityRunnable<LoggingEvent>(loggingevent) {

			@Override
			public void run(LoggingEvent loggingevent) {
				String value = loggingevent.getMessage().toString();
				String message = value.replace("\n", "");
				{
					List<String> list = map.get(httpRoleKey);
					list.clear();
					String targetRole = null;
					if (value.startsWith("<ROLE>")) {
						targetRole = substring(message, logRoleKey, logSplitPrefix);
					} else if (value.startsWith("<SYS>")) {
						targetRole = loggingevent.getLoggerName();
					}
					list.add(targetRole);
				}

				{
					List<String> list = map.get(httpLogInfoKey);
					list.clear();
					list.add(message);
				}

				try {
					HttpUtils.post(url, map);
				} catch (ConnectException e) {
					// System.out.println("log server unavailable");
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

	public void setHttpKeyValue(String httpKeyValue) {
		this.httpKeyValue = httpKeyValue;
	}

	public String getHttpKeyValue() {
		return httpKeyValue;
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
		// String m = "roleId:11,tiger:122,";
		// System.out.println(substring(m, "roleId:", ","));
		// System.out.println(substring(m, "tiger:", "2,"));
		String s = "<ROLE>";
		System.out.println(s.startsWith("<ROLE>"));
	}
}
