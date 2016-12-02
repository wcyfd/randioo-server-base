package com.randioo.randioo_server_base.db.converter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 整型映射工具
 * 
 * @author ming 创建时间：2013-11-19
 */
public class IntegerConverter implements ResultConverter<Integer> {
	public Integer convert(ResultSet rs) throws SQLException {
		return rs.getInt(1);
	}

}
