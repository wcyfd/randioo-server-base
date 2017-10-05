package com.randioo.randioo_server_base.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {
    public static String post(String urlStr, Map<String, List<String>> map) throws Exception {
        URL url = null;
        HttpURLConnection httpConn = null;
        try {
            url = new URL(urlStr);
            httpConn = setHeader(url, "POST");
            // 连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
            httpConn.connect();
            // 建立输入流，向指向的URL传入参数

            try (DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream())) {
                if (map != null) {
                    String param = getParam(map);
                    dos.writeBytes(param);
                }
                dos.flush();
            }

            // 获得响应状态
            return getResponse(httpConn);
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }

    }

    /**
     * 
     * @param urlStr 网址
     * @param param 数据字符串
     * @return
     * @throws Exception
     * @author wcy 2017年9月9日
     */
    public static String post(String urlStr, String param) throws Exception {
        URL url = null;
        HttpURLConnection httpConn = null;
        try {
            url = new URL(urlStr);
            httpConn = setHeader(url, "POST");
            // 连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
            httpConn.connect();
            // 建立输入流，向指向的URL传入参数

            try (DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream())) {
                if (param != null) {
                    dos.writeBytes(param);
                }
                dos.flush();
            }

            // 获得响应状态
            return getResponse(httpConn);
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }

    }

    public static String get(String urlStr, Map<String, List<String>> params) throws Exception {
        URL url = null;
        HttpURLConnection httpConn = null;
        try {
            String param = getParam(params);
            if (!StringUtils.isNullOrEmpty(param))
                urlStr += "?" + param;

            System.out.println(urlStr);
            url = new URL(urlStr);
            httpConn = setHeader(url, "GET");
            httpConn.connect();
            // 获得响应状态
            return getResponse(httpConn);
        } finally {
            if (httpConn != null)
                httpConn.disconnect();
        }
    }

    private static HttpURLConnection setHeader(URL url, String requestMethod) throws IOException, ProtocolException {
        HttpURLConnection httpConn;
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setDoOutput(true); // 需要输出
        httpConn.setDoInput(true); // 需要输入
        httpConn.setUseCaches(false); // 不允许缓存
        httpConn.setRequestMethod(requestMethod); // 设置GET方式连接
        // 设置请求属性
        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpConn.setRequestProperty("Charset", "UTF-8");
        return httpConn;
    }

    private static String getResponse(HttpURLConnection httpConn) throws IOException, UnsupportedEncodingException {
        int resultCode = httpConn.getResponseCode();

        if (HttpURLConnection.HTTP_OK == resultCode) {
            try (BufferedReader responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(),
                    "UTF-8"))) {
                String readLine = null;
                StringBuilder sb = new StringBuilder();
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine).append(StringUtils.lineSplit);
                }
                return sb.toString();
            }
        }
        return null;
    }

    /**
     * 参数整理
     * 
     * @param map
     * @return
     * @author wcy 2017年6月27日
     */
    private static String getParam(Map<String, List<String>> map) {
        if (map == null)
            return null;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, List<String>> entrySet : map.entrySet()) {
            String key = entrySet.getKey();
            List<String> list = entrySet.getValue();
            for (String value : list) {
                if (i != 0)
                    sb.append("&");
                sb.append(key).append("=").append(value);
                i++;
            }
        }
        return sb.toString();
    }

    public static void getParams(Map<String, List<String>> map, String paramStr) {
        String[] keyValues = paramStr.split("&");
        for (String keyValue : keyValues) {
            String[] keyValueArray = keyValue.split("\\=");
            String key = keyValueArray[0];
            String value = keyValueArray[1];
            List<String> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
                map.put(key, list);
            }
            list.add(value);
        }
    }

}
