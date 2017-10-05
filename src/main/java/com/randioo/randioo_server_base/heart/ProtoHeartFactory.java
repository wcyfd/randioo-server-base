package com.randioo.randioo_server_base.heart;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

import com.google.protobuf.GeneratedMessage;

/**
 * 心跳工厂
 * 
 * @author wcy 2017年8月17日
 *
 */
public class ProtoHeartFactory implements KeepAliveMessageFactory {

    private GeneratedMessage heartRequest;
    private GeneratedMessage heartResponse;
    private GeneratedMessage scHeart;
    private GeneratedMessage csHeart;

    public void setHeartRequest(GeneratedMessage heartRequest) {
        this.heartRequest = heartRequest;
    }

    public void setHeartResponse(GeneratedMessage heartResponse) {
        this.heartResponse = heartResponse;
    }

    public void setScHeart(GeneratedMessage scHeart) {
        this.scHeart = scHeart;
    }

    public void setCsHeart(GeneratedMessage csHeart) {
        this.csHeart = csHeart;
    }

    @Override
    public Object getRequest(IoSession arg0) {
        return scHeart;
    }

    @Override
    public Object getResponse(IoSession arg0, Object arg1) {
        return heartResponse;
    }

    @Override
    public boolean isRequest(IoSession arg0, Object arg1) {
        boolean isRequest = heartRequest.toString().equals(arg1.toString());
        return isRequest;
    }

    @Override
    public boolean isResponse(IoSession arg0, Object arg1) {
        boolean isResponse = csHeart.toString().equals(arg1.toString());
        return isResponse;
    }

}
