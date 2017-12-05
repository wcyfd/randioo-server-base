package com.randioo.randioo_server_base.collections8;

import java.util.Map;

/**
 * 参照java8进行补充的Map工具类
 * 
 * @author wcy 2017年11月24日
 *
 */
public class Maps8 {
    /**
     * 如果缺少则加入
     * 
     * @param map
     * @param t
     * @param v
     * @author wcy 2017年11月24日
     */
    public static <T, V> void putIfAbsent(Map<T, V> map, T t, V v) {
        if (map.containsKey(t)) {
            return;
        }
        map.put(t, v);
    }
}
