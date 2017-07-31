package com.randioo.randioo_server_base.db;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;

import com.randioo.randioo_server_base.GlobleConstant;
import com.randioo.randioo_server_base.config.GlobleMap;

public class DatabaseInitialization implements InitializingBean {
	private DataSource dataSource;
	private String databaseName;
	private List<String> sqls = new ArrayList<>();

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public List<String> getSqls() {
		return sqls;
	}

	public void setSqls(List<String> sqls) {
		this.sqls = sqls;
	}

	public void initialize() {
		String databaseName = this.databaseName + GlobleMap.Int(GlobleConstant.ARGS_PORT);

		DatabaseCreator creator = new DatabaseCreator();
		creator.setDatabaseName(databaseName);
		creator.setDataSource(dataSource);
		creator.setSqls(sqls);

		creator.initDatabase();

		BoneCPRedirector director = new BoneCPRedirector();
		director.setDataSource(dataSource);
		director.setDatabaseName(databaseName);
		director.init();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initialize();
	}
}