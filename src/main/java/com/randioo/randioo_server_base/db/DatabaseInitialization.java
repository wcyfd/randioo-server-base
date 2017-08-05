package com.randioo.randioo_server_base.db;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;

import com.randioo.randioo_server_base.GlobleConstant;
import com.randioo.randioo_server_base.config.GlobleMap;

/**
 * 数据库初始化工具
 * 
 * @author wcy 2017年8月5日
 *
 */
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

    /**
     * 初始化方法,数据库名称由数据库名和从全局参数列表中获取的端口拼接而成 <br>
     * 举例 databaseName="project" port=10003 最终数据库名project10003
     * 
     * @author wcy 2017年8月5日
     */
    public void initialize() {
        String databaseName = this.databaseName + GlobleMap.Int(GlobleConstant.ARGS_PORT);

        DatabaseCreator creator = new DatabaseCreator();
        creator.setDatabaseName(databaseName);
        creator.setDataSource(dataSource);
        creator.setSqls(sqls);

        // 数据库创建
        creator.initDatabase();

        BoneCPRedirector director = new BoneCPRedirector();
        director.setDataSource(dataSource);
        director.setDatabaseName(databaseName);
        // 重定向
        director.init();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialize();
    }
}