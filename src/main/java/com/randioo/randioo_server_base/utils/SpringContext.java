package com.randioo.randioo_server_base.utils;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Spring 配置
 * 
 * @author xjd
 *
 */
public class SpringContext {
    private static AbstractXmlApplicationContext ctx;

    /**
     * 加载spring配置
     */
    public static void initSpringCtx(String filePath) {
        // ctx = new ClassPathXmlApplicationContext(filePath);
        ctx = new FileSystemXmlApplicationContext(filePath);

        ctx.registerShutdownHook();
    }

    /**
     * 根据beanId获取bean
     * 
     * @param beanId
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanId) {
        return (T) ctx.getBean(beanId);
    }

    /**
     * 通过类名获取对象
     * 
     * @param clazz
     * @return
     * @author wcy 2017年8月4日
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<?> clazz) {
        return (T) ctx.getBean(clazz);
    }
}
