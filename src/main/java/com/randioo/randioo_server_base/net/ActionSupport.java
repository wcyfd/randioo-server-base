package com.randioo.randioo_server_base.net;

import org.apache.mina.core.session.IoSession;

import com.randioo.randioo_server_base.net.Message;


/**
 * 请求执行接口
 * @author xjd
 *
 */
public interface ActionSupport {
	void execute(Message message, IoSession session);
}