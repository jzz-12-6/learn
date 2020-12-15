package com.io.file;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;

/**
 * 包含内部缓冲区的字符输入流
 */
public class ByteArrayInputStream extends InputStream {

    /**
     * 字节数组缓冲区
     */
    protected byte buf[];

    /**
     * 读取下一个字符的索引
     */
    protected int pos;

    /**
     * 流中当前标记的索引
     */
    protected int mark = 0;

    /**
     * 字节流长度
     */
    protected int count;

    /**
     * 指定缓冲区的构造器
     */
    public ByteArrayInputStream(byte buf[]) {
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length;
    }

    /**
     * 指定缓冲区以及读取索引、读取长度的构造器，
     */
    public ByteArrayInputStream(byte buf[], int offset, int length) {
        this.buf = buf;
        this.pos = offset;
        this.count = Math.min(offset + length, buf.length);
        this.mark = offset;
    }

    /**
     * 读取下一个字节，范围0-255.末尾返回-1
     */
    public synchronized int read() {
        return (pos < count) ? (buf[pos++] & 0xff) : -1;
    }

    /**
     * 读取到b[off]，长度为len
     */
    public synchronized int read(byte b[], int off, int len) {
        Objects.checkFromIndexSize(off, len, b.length);

        if (pos >= count) {
            return -1;
        }

        int avail = count - pos;
        if (len > avail) {
            len = avail;
        }
        if (len <= 0) {
            return 0;
        }
        System.arraycopy(buf, pos, b, off, len);
        pos += len;
        return len;
    }

    /**
     * 读取所有字节
     * @return
     */
    public synchronized byte[] readAllBytes() {
        byte[] result = Arrays.copyOfRange(buf, pos, count);
        pos = count;
        return result;
    }

    public int readNBytes(byte[] b, int off, int len) {
        int n = read(b, off, len);
        return n == -1 ? 0 : n;
    }

    /**
     * 转为输出流
     */
    public synchronized long transferTo(OutputStream out) throws IOException {
        int len = count - pos;
        out.write(buf, pos, len);
        pos = count;
        return len;
    }

    /**
     * 跳过指定字节数
     */
    public synchronized long skip(long n) {
        long k = count - pos;
        if (n < k) {
            k = n < 0 ? 0 : n;
        }

        pos += k;
        return k;
    }

    /**
     * 是否可读
     */
    public synchronized int available() {
        return count - pos;
    }

    /**
     * 是否可标记
     */
    public boolean markSupported() {
        return true;
    }

    /**
     * 设置流中当前标记的位置
     */
    public void mark(int readAheadLimit) {
        mark = pos;
    }

    /**
     * 将缓冲区重置为标记位置。
     */
    public synchronized void reset() {
        pos = mark;
    }

    /**
     * 关闭流
     */
    public void close() throws IOException {
    }

}

