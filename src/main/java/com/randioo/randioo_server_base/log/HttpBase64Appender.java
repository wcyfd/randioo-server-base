package com.randioo.randioo_server_base.log;

import java.net.ConnectException;
import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import sun.misc.BASE64Encoder;

import com.randioo.randioo_server_base.template.EntityRunnable;
import com.randioo.randioo_server_base.utils.HttpUtils;

public class HttpBase64Appender extends AppenderSkeleton {

    private String url = "http://10.0.51.18/APPadmin/gateway/PhpServices/Log/insertGameLog.php";
    private long closeWaitSecond = 10;
    private String httpKey = "key";
    private String httpKeyValue = "f4f3f65d6d804d138043fbbd1843d510";
    private String httpRoleKey = "userId";
    private String httpGameKey = "gameName";
    private String httpLogInfoKey = "logInfo";

    private String pattern = null;

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
        StringBuilder sb = new StringBuilder();
        sb.append(httpGameKey).append("=").append(LogSystem.loggerRootName).append("&").append(httpKey).append("=")
                .append(httpKeyValue).append("&").append(httpRoleKey).append("={0}&").append(httpLogInfoKey)
                .append("={1}");

        pattern = sb.toString();

        url = LogSystem.loggerUrl == null ? url : LogSystem.loggerUrl;
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    @Override
    protected void append(LoggingEvent loggingevent) {
        String value = loggingevent.getMessage().toString();

        String userId = value.startsWith("[") ? substring(value, "[", "]") : loggingevent.getLoggerName();
        value = layout.format(loggingevent);
        @SuppressWarnings("restriction")
        String base64Str = new BASE64Encoder().encode(value.getBytes());
        String messageStr = MessageFormat.format(pattern, userId, base64Str);

        executorService.execute(new EntityRunnable<String>(messageStr) {

            @Override
            public void run(String message) {

                try {
                    HttpUtils.post(url, message);
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
}
