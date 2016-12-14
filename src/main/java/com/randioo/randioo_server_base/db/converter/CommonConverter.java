package com.randioo.randioo_server_base.db.converter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.randioo.randioo_server_base.db.converter.ResultConverter;

public class CommonConverter implements ResultConverter<List> {

	private Map<String, Method> keyValueMap = new LinkedHashMap<>();

	public CommonConverter(String[] columnNames, Method[] types) {
		for (int i = 0; i < columnNames.length; i++) {
			keyValueMap.put(columnNames[i], types[i]);
		}
	}

	@Override
	public List convert(ResultSet rs) throws SQLException {
		List list = new ArrayList();
		for (Map.Entry<String, Method> entrySet : keyValueMap.entrySet()) {
			String columName = entrySet.getKey();
			Method method = entrySet.getValue();
			try {
				Object obj = method.invoke(rs, columName);
				list.add(obj);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
