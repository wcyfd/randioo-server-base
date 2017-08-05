package com.randioo.randioo_server_base.handler;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage;
import com.randioo.randioo_server_base.error.ActionSupportFakeException;
import com.randioo.randioo_server_base.navigation.Navigation;
import com.randioo.randioo_server_base.net.IoHandlerAdapter;
import com.randioo.randioo_server_base.template.IActionSupport;

/**
 * 游戏服务器回调函数适配器,只能用于proto协议
 * 
 * @author wcy 2017年8月5日
 *
 */
public class GameServerHandlerAdapter extends IoHandlerAdapter {
    /**
     * 消息事件分发
     * 
     * @param message
     * @param session
     * @author wcy 2017年1月3日
     * @throws ActionSupportFakeException
     */
    protected void actionDispatcher(GeneratedMessage message, IoSession session) throws Exception {
        Map<FieldDescriptor, Object> allFields = message.getAllFields();
        for (Map.Entry<FieldDescriptor, Object> entrySet : allFields.entrySet()) {
            String name = entrySet.getKey().getName();
            IActionSupport action = Navigation.getAction(name);
            //如果action为空,说明协议异常
            if (action == null)
                throw new ActionSupportFakeException();

            try {
                action.execute(entrySet.getValue(), session);
            } catch (Exception e) {
                throw e;
            }
        }
    }
}
