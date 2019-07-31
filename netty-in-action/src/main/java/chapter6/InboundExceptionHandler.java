package chapter6;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 代码清单 6-12 基本的入站异常处理
 * 异常会按照入站方向流动 一般异常处理放在ChannelPipeline的最后
 */
public class InboundExceptionHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
        Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
