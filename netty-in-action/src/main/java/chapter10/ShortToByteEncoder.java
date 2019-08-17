package chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 代码清单 10-5 ShortToByteEncoder 类
 * 接受一个 Short 类型的实例作为消息，将它编码为 Short 的原子类型值，并将它写入 ByteBuf 中，
 * 其将随后被转发给 ChannelPipeline 中的下一个 ChannelOutboundHandler。
 * 每个传出的 Short 值都将会占用 ByteBuf 中的 2 字节
 * 扩展了MessageToByteEncoder
 */
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {
    @Override
    public void encode(ChannelHandlerContext ctx, Short msg, ByteBuf out) throws Exception {
        //将 Short 写入 ByteBuf 中
        out.writeShort(msg);
    }
}
