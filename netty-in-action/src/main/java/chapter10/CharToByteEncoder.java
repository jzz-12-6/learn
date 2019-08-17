package chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 代码清单 9 CharToByteEncoder 类
 *
 * 扩展了MessageToByteEncoder
 */
public class CharToByteEncoder extends MessageToByteEncoder<Character> {
    @Override
    public void encode(ChannelHandlerContext ctx, Character msg,
        ByteBuf out) throws Exception {
        //将 Character 解码为 char，并将其写入到出站 ByteBuf 中
        out.writeChar(msg);
    }
}

