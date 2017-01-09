package com.randioo.randioo_server_base.net;

import org.apache.mina.core.session.IoSession;


/**
 * 请求执行接口
 * @author xjd
 *
 */
public interface IActionSupport {
	void execute(Object data,IoSession session);
}