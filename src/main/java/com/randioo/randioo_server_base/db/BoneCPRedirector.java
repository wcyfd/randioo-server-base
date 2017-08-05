package com.randioo.randioo_server_base.db;

import java.lang.reflect.Field;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;

import com.jolbox.bonecp.BoneCPDataSource;

/**
 * BoneCP重定向类，由Spring自动初始化
 * 
 * @author wcy 2017年8月5日
 *
 */
public class BoneCPRedirector implements InitializingBean {
    private DataSource dataSource = null;
    private String databaseName = null;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    private void redirect(String jdbcUrl) {
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

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void init() {
        redirect(((BoneCPDataSource) dataSource).getJdbcUrl().replace("?", "/" + databaseName + "?"));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
