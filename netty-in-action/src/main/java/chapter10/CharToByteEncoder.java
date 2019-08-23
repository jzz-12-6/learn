package chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 代码清单 9 CharToByteEncoder 类
 * 能将 Character 转换回字节,
 */
public class CharToByteEncoder extends MessageToByteEncoder<Character> {
    @Override
    public void encode(ChannelHandlerContext ctx, Character msg, ByteBuf out)  {
        //将 Character 解码为 char，并将其写入到出站 ByteBuf 中
        out.writeChar(msg);
    }
}

