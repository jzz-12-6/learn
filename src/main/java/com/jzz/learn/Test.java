package com.jzz.learn;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author jzz
 * @date 2019/5/23
 */
public class Test {

    public static void main(String[] args) {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = Unpooled.buffer(1024);
        ByteBuf bodyBuf = Unpooled.buffer(1024);
        //将 ByteBuf 实例追加到 CompositeByteBuf
        messageBuf.addComponents(headerBuf, bodyBuf);
        messageBuf.removeComponent(0);
        int length = messageBuf.readableBytes();
        //分配一个具有可读字节数长度的新数组
        byte[] array = new byte[length];
        //将字节读到该数组中
        messageBuf.getBytes(messageBuf.readerIndex(), array);
        //使用偏移量和长度作为参数使用该数组

        byte[] array1 = messageBuf.array();
        System.out.println(array==array1);
    }
}
