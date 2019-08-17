package chapter10;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * 代码清单 CombinedChannelDuplexHandler<I,O>
 *
 * 通过该解码器和编码器实现参数化 CombinedByteCharCodec
 */
//
public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {
    public CombinedByteCharCodec() {
        //将委托实例传递给父类
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }
}
