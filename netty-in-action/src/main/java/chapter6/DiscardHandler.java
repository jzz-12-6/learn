package chapter6;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 代码清单 6-1 释放消息资源
 * 扩展了 ChannelInboundHandlerAdapter
 */
//标示一个Channel-Handler 可以被多个Channel安全地共享
@Sharable
public class DiscardHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //丢弃已接收的消息
        ReferenceCountUtil.release(msg);
    }

}

