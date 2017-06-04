package com.randioo.randioo_server_base.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.randioo.randioo_server_base.db.access.DataAccess;

/**
 * 数据库生成器
 * 
 * @author wcy 2016年12月13日
 *
 */
public class DatabaseCreator extends DataAccess {
	private final String CREATE_DATABASE_SQL = "create database if not exists {0}";
	private final String USE_DATABASE_SQL = "use {0}";

	private DataSource dataSource;
	private String databaseName;
	private List<String> sqls = new ArrayList<>();

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName.toLowerCase();
	}

	public List<String> getSqls() {
		return sqls;
	}

	public void setSqls(List<String> sqls) {
		this.sqls = sqls;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void initDatabase() {
		try (Connection conn = dataSource.getConnection()) {
			// 如果没有这个数据库则进行新建
			createDatabase(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createDatabase(Connection conn) throws SQLException {
		// 创建数据库
		this.executeNotCloseConn(MessageFormat.format(CREATE_DATABASE_SQL, databaseName), conn);
		// 使用该数据库
		this.executeNotCloseConn(MessageFormat.format(USE_DATABASE_SQL, databaseName), conn);

		for (String sql : sqls) {
			sql = sql.replace("{database}", databaseName);
			// 新建表
			try {
				this.executeNotCloseConn(sql, conn);
			} catch (Exception e) {
				System.out.println("create database table error:" + sql + " [check char ` and' (it's different)]");
			}
		}
	}

}
