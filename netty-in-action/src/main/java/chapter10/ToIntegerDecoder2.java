package chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 代码清单 10-2 ToIntegerDecoder2 类扩展了 ReplayingDecoder
 *
 * 扩展 ReplayingDecoder<Void> 以将字节解码为消息
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {
    /**
     *
     * @param ctx ChannelHandlerContext
     * @param in ByteBuf ReplayingDecoderByteBuf扩展了ByteBuf
     * @param out List<Object>
     * @throws Exception
     */
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //从入站 ByteBuf 中读取 一个 int，并将其添加到解码消息的 List 中
        //如果没有足够的字节可用，readInt()方法的实现将会抛出一个Error其将在基类中被捕获并处理
        out.add(in.readInt());
    }
}

