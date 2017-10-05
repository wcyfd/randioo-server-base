package com.randioo.randioo_server_base.template;

/**
 * 请求执行接口
 * 
 * @author xjd
 *
 */
public interface IActionSupport {
    void execute(Object data, Object session);
}