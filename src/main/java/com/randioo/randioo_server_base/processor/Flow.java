package com.randioo.randioo_server_base.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 游戏监听器
 * 
 * @author wcy 2017年8月25日
 * 
 */
public interface Flow<T> {

    static final Logger logger = LoggerFactory.getLogger(Flow.class);

    public void execute(T game, String[] params);

}
