package com.randioo.randioo_server_base.net;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.ByteString;
import com.randioo.randioo_server_base.net.protocal.randioo.Message;


/**
 * 请求执行接口
 * @author xjd
 *
 */
public interface IActionSupport {
	void execute(Message message, IoSession session);
	void execute(ByteString data,IoSession session);
}