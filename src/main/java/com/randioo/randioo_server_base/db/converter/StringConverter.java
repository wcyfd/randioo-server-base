package com.randioo.randioo_server_base.db.converter;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.randioo.randioo_server_base.db.converter.ResultConverter;

public class StringConverter implements ResultConverter<String>{

	@Override
	public String convert(ResultSet rs) throws SQLException {		
		return rs.getString(1);
	}

}
