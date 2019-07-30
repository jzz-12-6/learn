package chapter6.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.local.LocalChannel;

/**
 * Created by kerr.
 */
public class DummyChannelHandlerContext {
    public static ChannelHandlerContext DUMMY_INSTANCE = new LocalChannel().pipeline().firstContext();
}
