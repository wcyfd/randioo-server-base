package com.randioo.randioo_server_base.db;

import java.util.List;

/**
 * 数据库接口基础类
 * 
 * @author wcy 2017年8月5日
 *
 * @param <T>
 */
public interface BaseDAO<T> {
    T get(T entity);

    void insert(T entity);

    void update(T entity);

    List<T> selectList(int id);
}
