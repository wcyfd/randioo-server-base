package com.randioo.randioo_server_base.config;

/**
 * 全局参数
 * 
 * @author wcy 2017年8月5日
 *
 */
public class GlobleParameter {

    protected String key;
    protected String value;
    protected GlobleTypeEnum type;

    /**
     * 设置键的名称
     * 
     * @param key
     * @author wcy 2017年8月5日
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 设置值的数据类型
     * 
     * @param type
     * @author wcy 2017年8月5日
     */
    public void setType(GlobleTypeEnum type) {
        this.type = type;
    }

    /**
     * 设置值
     * 
     * @param value
     * @author wcy 2017年8月5日
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GlobleParameter [key=");
        builder.append(key);
        builder.append(", value=");
        builder.append(value);
        builder.append(", type=");
        builder.append(type);
        builder.append("]");
        return builder.toString();
    }

}
