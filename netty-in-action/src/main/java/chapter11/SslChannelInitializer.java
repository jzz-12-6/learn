package chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 代码清单 11-1 添加 SSL/TLS 支持
 * 使用ChannelInitializer来将SslHandler添加到ChannelPipeline
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean startTls;

    /**
     * 传入要使用的 SslContext
     * 如果设置为 true，第一个写入的消息将不会被加密（客户端应该设置为 true）
     * @param context 要使用的 SslContext
     * @param startTls 第一个消息是否加密
     */
    public SslChannelInitializer(SslContext context, boolean startTls) {
        this.context = context;
        this.startTls = startTls;
    }
    @Override
    protected void initChannel(Channel ch) throws Exception {
        //对于每个 SslHandler 实例，都使用 Channel 的 ByteBufAllocator 从 SslContext 获取一个新的 SSLEngine
        SSLEngine engine = context.newEngine(ch.alloc());
        //将 SslHandler 作为第一个 ChannelHandler 添加到 ChannelPipeline 中
        ch.pipeline().addFirst("ssl",
            new SslHandler(engine, startTls));
    }
}
