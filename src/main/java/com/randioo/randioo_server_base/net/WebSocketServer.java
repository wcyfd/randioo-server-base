package com.randioo.randioo_server_base.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.randioo.randioo_server_base.protocol.websocket.WebSocketEncoder;

@Component
public class WebSocketServer {

    private Logger logger = LoggerFactory.getLogger(WebSocketServer.class.getSimpleName());

    private ServerBootstrap boot = null;
    private EventLoopGroup boss = null;
    private EventLoopGroup group = null;
    private int port;

    private ChannelHandler channelHandler;

    public void setChannelHandler(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    public WebSocketServer() {
        boss = new NioEventLoopGroup();// 通过nio方式来接收连接和处理连接
        group = new NioEventLoopGroup();// 通过nio方式来接收连接和处理连接
        boot = new ServerBootstrap();// 引导辅助程序
        boot.group(boss, group);
        boot.channel(NioServerSocketChannel.class);// 设置nio类型的channel
        boot.childHandler(new ChannelInitializer<SocketChannel>() {// 有连接到达时会创建一个channel

                    protected void initChannel(SocketChannel ch) throws Exception {
                        // pipeline管理channel中的Handler，在channel队列中添加一个handler来处理业务
                        ChannelPipeline pipeline = ch.pipeline();
                        // Http消息编码解码
                        pipeline.addLast("http-codec", new HttpServerCodec());
                        // Http消息组装
                        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                        // WebSocket通信支持
                        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                        pipeline.addLast("text-frame-encoder", new WebSocketEncoder());
                        // WebSocket服务端Handler
                        pipeline.addLast("handler", channelHandler);
                    }
                })

        .childOption(ChannelOption.TCP_NODELAY, true);

    }

    public void start(int p) {
        this.port = p;

        Thread netThread = new Thread(new Runnable() {

            @Override
            public void run() {
                fireStart();
            }

        });
        netThread.start();

    }

    private void fireStart() {
        try {
            Channel ch = boot.bind(port).sync().channel();
            logger.info("netty websocket server start at port:{}", port);
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            group.shutdownGracefully();
        }
    }
}
