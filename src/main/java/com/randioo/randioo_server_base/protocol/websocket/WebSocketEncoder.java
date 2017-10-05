package com.randioo.randioo_server_base.protocol.websocket;

import com.randioo.randioo_server_base.protocol.json.GeneratedJsonMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof GeneratedJsonMessage) {
            ctx.writeAndFlush(new TextWebSocketFrame(((GeneratedJsonMessage) msg).serialize()));
        } else if (msg instanceof String) {
            ctx.writeAndFlush(new TextWebSocketFrame((String) msg));
        } else {
            ctx.writeAndFlush(msg);
        }
    }
}
