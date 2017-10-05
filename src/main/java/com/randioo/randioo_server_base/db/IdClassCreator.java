package com.randioo.randioo_server_base.db;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

/**
 * 类id生成器
 * 
 * @author wcy 2017年2月13日
 *
 */
@Component
public class IdClassCreator {

    private Map<Class<?>, AtomicInteger> atomicIdMap = new ConcurrentHashMap<>();

    public int getId(Class<?> clazz) {
        AtomicInteger atomicInteger = atomicIdMap.get(clazz);
        int id = atomicInteger.incrementAndGet();
        return id;
    }

    /**
     * 初始化id值
     *
     * @param clazz
     * @param initId
     * @author wcy 2017年2月13日
     */
    public void initId(Class<?> clazz, int initId) {
        AtomicInteger atomicInteger = new AtomicInteger(initId);
        atomicIdMap.put(clazz, atomicInteger);
    }

    /**
     * 获得当前id
     *
     * @param clazz
     * @return
     * @author wcy 2017年3月21日
     */
    public int getCurrentId(Class<?> clazz) {
        AtomicInteger atomicInteger = atomicIdMap.get(clazz);
        int value = atomicInteger.get();
        return value;
    }
}
