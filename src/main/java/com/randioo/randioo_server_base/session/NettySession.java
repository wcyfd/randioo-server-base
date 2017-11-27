package com.randioo.randioo_server_base.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import com.randioo.randioo_server_base.template.ISession;

public class NettySession implements ISession {

    @Override
    public Object getAttribute(Object session, Object key) {
        String name = key instanceof String ? (String) key : String.valueOf(key);
        AttributeKey<Object> attributeKey = AttributeKey.valueOf(name);

        Channel channel = ((ChannelHandlerContext) session).channel();

        Attribute<Object> attribute = channel.attr(attributeKey);
        Object obj = attribute.get();
        return obj;
    }

    @Override
    public void setAttribute(Object session, Object key, Object value) {
        Channel channel = ((ChannelHandlerContext) session).channel();
        String name = key instanceof String ? (String) key : String.valueOf(key);

        AttributeKey<Object> attributeKey = AttributeKey.valueOf(name);

        Attribute<Object> attribute = channel.attr(attributeKey);
        attribute.setIfAbsent(value);
    }

    @Override
    public void write(Object session, Object value) {
        ((ChannelHandlerContext) session).writeAndFlush(value);
    }

    @Override
    public void close(Object session) {
        ((ChannelHandlerContext) session).close();
    }

    @Override
    public boolean isInstanceof(Object obj) {
        return obj instanceof ChannelHandlerContext;
    }

    @Override
    public boolean isConnected(Object session) {
        return ((ChannelHandlerContext) session).channel().isOpen();
    }
}
