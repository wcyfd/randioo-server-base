package com.randioo.randioo_server_base.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局参数映射表
 * 
 * @author wcy 2017年8月5日
 *
 */
public class GlobleMap {
    protected static Map<String, Object> paramMap = new HashMap<>();

    /**
     * 根据字符串获得一个整形参数,如果键值不存在返回0
     * 
     * @param key
     * @return
     * @author wcy 2017年8月5日
     */
    public static int Int(String key) {
        Object result = paramMap.get(key);
        if (result == null)
            return 0;
        return (int) result;
    }

    /**
     * 根据字符串获得一个字符串,如果不存在返回null
     * 
     * @param key
     * @return
     * @author wcy 2017年8月5日
     */
    public static String String(String key) {
        Object result = paramMap.get(key);
        if (result == null)
            return null;
        return (String) result;
    }

    /**
     * 根据字符串获得一个boolean值,如果不存在返回false
     * 
     * @param key
     * @return
     * @author wcy 2017年8月5日
     */
    public static boolean Boolean(String key) {
        Object result = paramMap.get(key);
        if (result == null || !(Boolean) result)
            return false;
        return true;
    }

    /**
     * 放入一个全局参数
     * 
     * @param parameter
     * @author wcy 2017年8月5日
     */
    public static void putParam(GlobleParameter parameter) {
        String key = parameter.key;
        String value = parameter.value;

        switch (parameter.type) {
        case GLOBLE_TYPE_BOOLEAN:
            paramMap.put(key, Boolean.valueOf(value));
            break;
        case GLOBLE_TYPE_DOUBLE:
            paramMap.put(key, Double.parseDouble(value));
            break;
        case GLOBLE_TYPE_FLOAT:
            paramMap.put(key, Float.parseFloat(value));
            break;
        case GLOBLE_TYPE_INT:
            paramMap.put(key, Integer.parseInt(value));
            break;
        case GLOBLE_TYPE_STRING:
            paramMap.put(key, value);
            break;
        default:
            break;
        }

    }

    /**
     * 放入一个全局参数
     * 
     * @param key
     * @param value
     * @author wcy 2017年8月5日
     */
    public static void putParam(String key, Object value) {
        paramMap.put(key, value);
    }

    /**
     * 获得全局映射表的映射表信息
     * 
     * @return
     * @author wcy 2017年8月5日
     */
    public static String print() {
        return paramMap.toString();
    }

}
