package com.randioo.randioo_server_base.db;

import java.util.List;

public interface BaseDao<T> {
	T get(T entity);

	void insert(T entity);

	void update(T entity);

	List<T> selectList(int id);
}
