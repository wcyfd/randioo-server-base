package com.randioo.randioo_server_base.utils.db;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.jolbox.bonecp.BoneCPDataSource;

public class DatabaseInitialization {
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
		DatabaseCreator creator = new DatabaseCreator();
		creator.setDatabaseName(databaseName);
		creator.setDataSource(dataSource);
		creator.setSqls(sqls);

		creator.initDatabase();

		BoneCPRedirector director = new BoneCPRedirector();
		director.setDataSource(dataSource);
		director.redirect(((BoneCPDataSource) dataSource).getJdbcUrl().replace("?", "/" + databaseName + "?"));
	}
}
