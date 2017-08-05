package com.randioo.randioo_server_base.module.chat;

import com.randioo.randioo_server_base.service.BaseServiceInterface;

/**
 * 聊天服务,已经弃用，请使用独立的聊天服务器
 * 
 * @author wcy 2017年8月5日
 *
 */
@Deprecated
public interface ChatModelService extends BaseServiceInterface {

    /**
     * 设置聊天的回调
     * 
     * @param handler
     * @author wcy 2017年8月5日
     */
    public void setChatHandler(ChatHandler handler);

    /**
     * 发送私人聊天信息
     * 
     * @param context
     * @return
     * @author wcy 2016年12月19日
     */
    public void sendPrivateChat(Chatable chatable, int targetChatableKey, String context);

    /**
     * 
     * @param chatable
     * @param context
     * @author wcy 2017年8月5日
     */
    public void sendPublicChat(Chatable chatable, String context);

    void joinPublic(Chatable chatable);

    void exitPublic(Chatable chatable);
}
