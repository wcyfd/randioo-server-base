package com.randioo.randioo_server_base.db;

/**
 * 可保存接口
 * 
 * @author wcy 2017年8月5日
 *
 */
public interface Saveable {
    /**
     * 设置改变
     * 
     * @param change
     * @author wcy 2016年12月14日
     */
    void setChange(boolean change);

    /**
     * 是否改变
     * 
     * @return
     * @author wcy 2016年12月14日
     */
    boolean isChange();

    /**
     * 字符串改变
     * 
     * @return
     * @author wcy 2016年12月14日
     */
    boolean checkChange();
}
