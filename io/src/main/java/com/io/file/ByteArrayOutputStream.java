package com.io.file;



import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;


/**
 * 字节数组输出流
 */

public class ByteArrayOutputStream extends java.io.OutputStream {

    /**
     * 字节数组缓冲区
     */
    protected byte buf[];

    /**
     * 缓冲区中的有效字节数。
     */
    protected int count;

    /**
     * 空构造。缓冲区容量为32
     */
    public ByteArrayOutputStream() {
        this(32);
    }

    /**
     * 指定缓冲区容量大小的构造器
     */
    public ByteArrayOutputStream(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: "
                    + size);
        }
        buf = new byte[size];
    }

    /**
     * 增加minCapacity的容量
     */
    private void ensureCapacity(int minCapacity) {
        int oldCapacity = buf.length;
        int minGrowth = minCapacity - oldCapacity;
        if (minGrowth > 0) {
//            buf = Arrays.copyOf(buf, ArraysSupport.newLength(oldCapacity,
//                    minGrowth, oldCapacity /* preferred growth */));
        }
    }

    /**
     * 写一个字节
     */
    public synchronized void write(int b) {
        ensureCapacity(count + 1);
        buf[count] = (byte) b;
        count += 1;
    }

    /**
     * 从b[off]开始写入len个字节
     */
    public synchronized void write(byte b[], int off, int len) {
        Objects.checkFromIndexSize(off, len, b.length);
        ensureCapacity(count + len);
        System.arraycopy(b, off, buf, count, len);
        count += len;
    }

    /**
     * 写入一个字节数组
     */
    public void writeBytes(byte b[]) {
        write(b, 0, b.length);
    }

    /**
     * 将字节数组输出流转为其他输出流
     */
    public synchronized void writeTo(OutputStream out) throws IOException {
        out.write(buf, 0, count);
    }

    /**
     * 重置流
     */
    public synchronized void reset() {
        count = 0;
    }

    /**
     * 将流转数组
     */
    public synchronized byte[] toByteArray() {
        return Arrays.copyOf(buf, count);
    }

    /**
     * 返回缓冲区的当前大小
     */
    public synchronized int size() {
        return count;
    }

    /**
     * 流转字符串（平台默认字符集）
     */
    public synchronized String toString() {
        return new String(buf, 0, count);
    }

    /**
     * 指定字符集。流转字符串
     */
    public synchronized String toString(String charsetName)
            throws UnsupportedEncodingException
    {
        return new String(buf, 0, count, charsetName);
    }

    /**
     * 指定Charset转字符串
     */
    public synchronized String toString(Charset charset) {
        return new String(buf, 0, count, charset);
    }



    /**
     * 关闭流 数组流不用关闭
     */
    public void close() throws IOException {
    }

}

