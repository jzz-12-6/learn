package com.io.file;



import java.io.*;
import java.nio.CharBuffer;
import java.util.Objects;

/**
 * 读取字符流
 */

public abstract class Reader implements Readable, Closeable {
    /**
     * 转移缓冲区大小
     */
    private static final int TRANSFER_BUFFER_SIZE = 8192;

    /**
     * 创建一个新的读取流
     */
    public static Reader nullReader() {
        return new Reader() {
            private volatile boolean closed;

            private void ensureOpen() throws IOException {
                if (closed) {
                    throw new IOException("Stream closed");
                }
            }

            @Override
            public int read() throws IOException {
                ensureOpen();
                return -1;
            }

            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                Objects.checkFromIndexSize(off, len, cbuf.length);
                ensureOpen();
                if (len == 0) {
                    return 0;
                }
                return -1;
            }

            @Override
            public int read(CharBuffer target) throws IOException {
                Objects.requireNonNull(target);
                ensureOpen();
                if (target.hasRemaining()) {
                    return -1;
                }
                return 0;
            }

            @Override
            public boolean ready() throws IOException {
                ensureOpen();
                return false;
            }

            @Override
            public long skip(long n) throws IOException {
                ensureOpen();
                return 0L;
            }

            @Override
            public long transferTo(Writer out) throws IOException {
                Objects.requireNonNull(out);
                ensureOpen();
                return 0L;
            }

            @Override
            public void close() {
                closed = true;
            }
        };
    }

    /**
     * 用于同步此流上的操作的对象。
     */
    protected Object lock;

    /**
     * 空构造
     */
    protected Reader() {
        this.lock = this;
    }

    /**
     * 指定同步锁的构造器
     */
    protected Reader(Object lock) {
        if (lock == null) {
            throw new NullPointerException();
        }
        this.lock = lock;
    }

    /**
     * 读取字符到指定CharBuffer（一个char缓冲区）缓冲区
     * Attempts to read characters into the specified character buffer.
     * The buffer is used as a repository of characters as-is: the only
     * changes made are the results of a put operation. No flipping or
     * rewinding of the buffer is performed.
     *
     * @param target the buffer to read characters into
     * @return The number of characters added to the buffer, or
     *         -1 if this source of characters is at its end
     * @throws IOException if an I/O error occurs
     * @throws NullPointerException if target is null
     * @throws java.nio.ReadOnlyBufferException if target is a read only buffer
     * @since 1.5
     */
    public int read(java.nio.CharBuffer target) throws IOException {
        int len = target.remaining();
        char[] cbuf = new char[len];
        int n = read(cbuf, 0, len);
        if (n > 0)
            target.put(cbuf, 0, n);
        return n;
    }

    /**
     * 读取一个字符
     * 末尾返回-1
     */
    public int read() throws IOException {
        char cb[] = new char[1];
        if (read(cb, 0, 1) == -1)
            return -1;
        else
            return cb[0];
    }

    /**
     * 读取字符，缓冲到数组中
     * 返回实际读取的字符数。末尾返回-1
     */
    public int read(char cbuf[]) throws IOException {
        return read(cbuf, 0, cbuf.length);
    }

    /**
     * 读取指定len个字符，存入 cbuf[off]中
     */
    public abstract int read(char cbuf[], int off, int len) throws IOException;

    /**最大跳过缓冲区大小 */
    private static final int maxSkipBufferSize = 8192;


    private char skipBuffer[] = null;

    /**
     * 跳过n个字符
     */
    public long skip(long n) throws IOException {
        if (n < 0L)
            throw new IllegalArgumentException("skip value is negative");
        int nn = (int) Math.min(n, maxSkipBufferSize);
        synchronized (lock) {
            if ((skipBuffer == null) || (skipBuffer.length < nn))
                skipBuffer = new char[nn];
            long r = n;
            while (r > 0) {
                int nc = read(skipBuffer, 0, (int)Math.min(r, nn));
                if (nc == -1)
                    break;
                r -= nc;
            }
            return n - r;
        }
    }

    /**
     * 是否可读取
     */
    public boolean ready() throws IOException {
        return false;
    }

    /**
     * 判断此流是否支持mark（）操作
     */
    public boolean markSupported() {
        return false;
    }

    /**
     * 标记流中的当前位置。 对reset（）的后续调用将尝试将流重新定位到此点
     */
    public void mark(int readAheadLimit) throws IOException {
        throw new IOException("mark() not supported");
    }

    /**
     * 重置流。 如果已标记流，则尝试将其重新定位到标记处。
     */
    public void reset() throws IOException {
        throw new IOException("reset() not supported");
    }

    /**
     * 关闭流并释放与其关联的所有系统资源。
     */
    public abstract void close() throws IOException;

    /**
     * 读取所有字符写入到输出流
     */
    public long transferTo(Writer out) throws IOException {
        Objects.requireNonNull(out, "out");
        long transferred = 0;
        char[] buffer = new char[TRANSFER_BUFFER_SIZE];
        int nRead;
        while ((nRead = read(buffer, 0, TRANSFER_BUFFER_SIZE)) >= 0) {
            out.write(buffer, 0, nRead);
            transferred += nRead;
        }
        return transferred;
    }

}
