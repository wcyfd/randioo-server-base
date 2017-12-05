package com.randioo.randioo_server_base.processor;

import java.util.List;

/**
 * 命令回调函数
 * 
 * @author wcy 2017年10月28日
 *
 * @param <T>
 */
public interface ICommandCallback<T> {
    public static final String WAIT = "wait";

    /**
     * 
     * @param game
     * @param flowName
     * @param params
     * @return
     * @author wcy 2017年11月28日
     */
    public List<String> afterCommandExecute(T game, String flowName, String[] params);
}
