package com.randioo.randioo_server_base.net;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.ByteString;
import com.randioo.randioo_server_base.net.protocal.randioo.Message;

public class ActionSupport implements IActionSupport{

	@Override
	public void execute(Message message, IoSession session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(ByteString data, IoSession session) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void execute(Object data, IoSession session) {
		// TODO Auto-generated method stub
		
	}

}
