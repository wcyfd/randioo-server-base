package com.randioo.randioo_server_base.db;

/**
 * 数据库保存实体<br>
 * 用于检测实体内的数据是否发生变化<br>
 * 需要在实现类中调用setChange方法
 * 
 * @author wcy 2017年8月5日
 *
 */
public class DataEntity implements Saveable {

    private boolean change;

    @Override
    public void setChange(boolean change) {
        this.change = change;
    }

    @Override
    public boolean isChange() {
        if (!change) {
            change = checkChange();
        }
        return change;
    }

    @Override
    public boolean checkChange() {
        return false;
    }

}
