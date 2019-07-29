package chapter6;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 代码清单 6-3 消费并释放入站消息
 *
 */
@Sharable
public class DiscardInboundHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //通过调用 ReferenceCountUtil.release()方法释放资源
        //不会通过调用 ChannelHandlerContext.fireChannelRead()方法将入站消息转发给下一个 ChannelInboundHandler
        ReferenceCountUtil.release(msg);
    }

}
