package chapter6;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 代码清单 6-2 使用 SimpleChannelInboundHandler
 *
 */
@Sharable
public class SimpleDiscardHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        //不需要任何显式的资源释放
    }

}
