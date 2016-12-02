package com.randioo.randioo_server_base.db.access;

/**
 * 数据库存取异常
 * 
 * @author xjd
 */
public class DataAccessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataAccessException(Throwable cause) {
		super(cause);
	}

}
