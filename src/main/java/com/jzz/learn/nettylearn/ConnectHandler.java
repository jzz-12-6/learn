package com.jzz.learn.nettylearn;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class ConnectHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client " +ctx.channel().remoteAddress() + " connected");
       // super.channelActive(ctx);

        Channel channel = null;
        //异步地连接到远程节点
        ChannelFuture channelFuture = channel.connect(new InetSocketAddress("127.0.0.1", 25));
        //注册一个ChannelFutureListener以便在操作完成时获得通知
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()){
                    ByteBuf buf = Unpooled.copiedBuffer("Hello", Charset.defaultCharset());
                    //将数据异步地发送到远程节点，返回一个ChannelFuture
                    ChannelFuture channelFuture1 = channelFuture.channel().writeAndFlush(buf);
                }else {
                    Throwable cause = channelFuture.cause();
                    cause.printStackTrace();
                }
            }
        });

    }
}
