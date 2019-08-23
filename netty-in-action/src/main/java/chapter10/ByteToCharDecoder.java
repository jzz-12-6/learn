package chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 代码清单 10-8 ByteToCharDecoder 类
 *
 */
public class ByteToCharDecoder extends ByteToMessageDecoder {
    /**
     *
     * @param ctx
     * @param in
     * @param out
     */
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)  {
        if (in.readableBytes() >= 2) {
            //将一个或者多个 Character 对象添加到传出的 List 中
            out.add(in.readChar());
        }
    }
}

