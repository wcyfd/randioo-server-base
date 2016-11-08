package com.randioo.randioo_server_base.error;

/**
 * 数量超出上下限
 * 
 */
public class NumberFakeException extends UserFakeMsgException {

	private static final long serialVersionUID = -5970819149126423040L;



	public NumberFakeException(String msg) {
		super(msg);
	}

}
