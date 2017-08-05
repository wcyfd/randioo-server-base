package com.randioo.randioo_server_base.db;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.randioo.randioo_server_base.template.Function;

@Component("gameDB")
/**
 * 游戏数据库<br>
 * 用于异步保存游戏数据
 * @author wcy 2017年8月5日
 *
 */
public class GameDB {
    private ExecutorService updatePool = Executors.newFixedThreadPool(4);
    private ExecutorService insertPool = Executors.newSingleThreadExecutor();
    private ExecutorService selectPool = Executors.newCachedThreadPool();
    private boolean close;

    /**
     * 获得更新线程池
     * 
     * @return
     * @author wcy 2017年8月5日
     */
    public ExecutorService getUpdatePool() {
        return updatePool;
    }

    /**
     * 获得插入线程池
     * 
     * @return
     * @author wcy 2017年8月5日
     */
    public ExecutorService getInsertPool() {
        return insertPool;
    }

    /**
     * 是否更新线程池开关关闭
     * 
     * @return
     * @author wcy 2017年8月5日
     */
    public boolean isUpdatePoolClose() {
        return close;
    }

    /**
     * 是否插入线程池开关关闭
     * 
     * @return
     * @author wcy 2017年8月5日
     */
    public ExecutorService getSelectPool() {
        return selectPool;
    }

    /**
     * 关闭所有线程,会等待所有线程关闭
     * 
     * @param timeout
     * @param unit
     * @param function
     * @throws InterruptedException
     * @author wcy 2017年8月5日
     */
    public void shutdown(long timeout, TimeUnit unit, Function function) throws InterruptedException {
        close = true;
        updatePool.shutdown();
        System.out.println("gameDB updatePool wait shutdown");
        insertPool.shutdown();
        System.out.println("gameDB insertPool wait shutdown");
        while (!updatePool.awaitTermination(timeout, unit)) {
        }
        System.out.println("gameDB updatePool success shutdown");
        while (!insertPool.awaitTermination(timeout, unit)) {
        }
        System.out.println("gameDB insertPool success shutdown");

        function.apply();

    }
}
