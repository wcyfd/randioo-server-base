package com.randioo.randioo_server_base.utils.db;

import java.lang.reflect.Field;

import javax.sql.DataSource;

import com.jolbox.bonecp.BoneCPDataSource;

public class BoneCPRedirector {
	private DataSource dataSource = null;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void redirect(String jdbcUrl) {
		BoneCPDataSource boneCPDataSource = (BoneCPDataSource) dataSource;
		try {
			// 关闭所有连接池
			boneCPDataSource.close();
			// 获得连接池对象
			Field field = BoneCPDataSource.class.getDeclaredField("pool");
			field.setAccessible(true);

			// 复制成空
			field.set(dataSource, null);

			field.setAccessible(false);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 赋予新的url
		boneCPDataSource.setJdbcUrl(jdbcUrl);
	}
}
