package chapter6;

import io.netty.channel.*;

/**
 * 代码清单 6-14 添加 ChannelFutureListener 到 ChannelPromise
 * 将 ChannelFutureListener 添加到即将作为参数传递给 ChannelOutboundHandler 的方法的 ChannelPromise
 */
public class OutboundExceptionHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {

        promise.addListener((ChannelFuture f)->{
            if (!f.isSuccess()) {
                f.cause().printStackTrace();
                f.channel().close();
            }
        });
    }
}
