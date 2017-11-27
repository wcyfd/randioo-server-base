package com.randioo.randioo_server_base.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseService implements BaseServiceInterface {

    /**
     * 获得这个类的第一个接口
     */
    public Logger logger = LoggerFactory.getLogger(getSysName());

    private String getSysName() {
        return this.getClass().getInterfaces()[0].getSimpleName();
    }

    @Override
    public void init() {

    }

    @Override
    public void initService() {

    }

}
