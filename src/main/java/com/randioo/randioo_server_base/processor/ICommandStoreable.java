package com.randioo.randioo_server_base.processor;

import java.util.Stack;

import org.slf4j.Logger;

/**
 * 命令存储接口
 * 
 * @author wcy 2017年10月28日
 *
 */
public interface ICommandStoreable {

    /**
     * 
     * @return
     * @author wcy 2017年8月25日
     */
    public Stack<String> getCmdStack();

    public Logger logger();
}
