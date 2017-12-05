package com.randioo.randioo_server_base.service;

/**
 * 抽象校验管理器
 * 
 * @author wcy 2017年11月29日
 *
 * @param <T>
 */
public abstract class AbstractVerifyManager<T> {
    /**
     * 检查是否相同
     * 
     * @param t
     * @return
     * @author wcy 2017年11月29日
     */
    public boolean checkVerify(T t) {
        return getVerifyId(t) == getAccumlate(t);
    }

    /**
     * 重置
     * 
     * @param t
     * @author wcy 2017年11月29日
     */
    public void reset(T t) {
        int verifyId = getVerifyId(t);
        int accumlate = getAccumlate(t);
        int max = Math.max(verifyId, accumlate);
        max++;
        verifyId(t, max);
        accumlateToValue(t, max);
    }

    public abstract int getVerifyId(T t);

    protected abstract int getAccumlate(T t);

    protected abstract void verifyId(T t, int newVerifyId);

    protected abstract void accumlateToValue(T t, int accumlate);

    public abstract void accumlate(T t);
}
