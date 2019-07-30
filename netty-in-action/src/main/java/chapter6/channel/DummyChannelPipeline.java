package chapter6.channel;

import io.netty.channel.*;
import io.netty.channel.local.LocalChannel;
import org.apache.tomcat.util.net.NioChannel;

/**
 * Created by kerr.
 */
public class DummyChannelPipeline {
    public static final ChannelPipeline DUMMY_INSTANCE = new LocalChannel().pipeline();

}
