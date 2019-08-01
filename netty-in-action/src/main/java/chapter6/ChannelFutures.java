package chapter6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * Created by kerr.
 *
 * 代码清单 6-13 添加 ChannelFutureListener 到 ChannelFuture
 */
public class ChannelFutures {
    /**
     * 代码清单 6-13 添加 ChannelFutureListener 到 ChannelFuture
     * */
    public static void addingChannelFutureListener(){
        Channel channel = new NioSocketChannel();
        ByteBuf someMessage = Unpooled.buffer(1024);
        //获取出战操作的ChannelFuture
        ChannelFuture future = channel.write(someMessage);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) {
                if (!f.isSuccess()) {
                    f.cause().printStackTrace();
                    f.channel().close();
                }
            }
        });
    }
}
