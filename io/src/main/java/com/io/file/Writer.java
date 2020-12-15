package com.io.file;




import java.io.*;
import java.io.Reader;
import java.util.Objects;

/**
 * 字符写入流
 */

public abstract class Writer implements Appendable, Closeable, Flushable {

    /**
     * 临时缓冲区
     */
    private char[] writeBuffer;

    /**
     * 缓冲区大小
     */
    private static final int WRITE_BUFFER_SIZE = 1024;

    /**
     * 创建一个新的Writer
     */
    public static Writer nullWriter() {
        return new Writer() {
            private volatile boolean closed;

            private void ensureOpen() throws IOException {
                if (closed) {
                    throw new IOException("Stream closed");
                }
            }

            @Override
            public Writer append(char c) throws IOException {
                ensureOpen();
                return this;
            }

            @Override
            public Writer append(CharSequence csq) throws IOException {
                ensureOpen();
                return this;
            }

            @Override
            public Writer append(CharSequence csq, int start, int end) throws IOException {
                ensureOpen();
                if (csq != null) {
                    Objects.checkFromToIndex(start, end, csq.length());
                }
                return this;
            }

            @Override
            public void write(int c) throws IOException {
                ensureOpen();
            }

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                Objects.checkFromIndexSize(off, len, cbuf.length);
                ensureOpen();
            }

            @Override
            public void write(String str) throws IOException {
                Objects.requireNonNull(str);
                ensureOpen();
            }

            @Override
            public void write(String str, int off, int len) throws IOException {
                Objects.checkFromIndexSize(off, len, str.length());
                ensureOpen();
            }

            @Override
            public void flush() throws IOException {
                ensureOpen();
            }

            @Override
            public void close() throws IOException {
                closed = true;
            }
        };
    }

    /**
     * 对象锁
     */
    protected Object lock;

    /**
     * 空构造
     */
    protected Writer() {
        this.lock = this;
    }

    /**
     * 指定对象锁
     */
    protected Writer(Object lock) {
        if (lock == null) {
            throw new NullPointerException();
        }
        this.lock = lock;
    }

    /**
     * 写入一个字符
     */
    public void write(int c) throws IOException {
        synchronized (lock) {
            if (writeBuffer == null){
                writeBuffer = new char[WRITE_BUFFER_SIZE];
            }
            writeBuffer[0] = (char) c;
            write(writeBuffer, 0, 1);
        }
    }

    /**
     * 写入一个数组
     */
    public void write(char cbuf[]) throws IOException {
        write(cbuf, 0, cbuf.length);
    }

    /**
     * 从cbuf[off]开始写入len长度的字符
     */
    public abstract void write(char cbuf[], int off, int len) throws IOException;

    /**
     * 写入一个字符串
     */
    public void write(String str) throws IOException {
        write(str, 0, str.length());
    }

    /**
     * 从字符串off开始写入len个长度
     */
    public void write(String str, int off, int len) throws IOException {
        synchronized (lock) {
            char cbuf[];
            if (len <= WRITE_BUFFER_SIZE) {
                if (writeBuffer == null) {
                    writeBuffer = new char[WRITE_BUFFER_SIZE];
                }
                cbuf = writeBuffer;
            } else {    // Don't permanently allocate very large buffers.
                cbuf = new char[len];
            }
            str.getChars(off, (off + len), cbuf, 0);
            write(cbuf, 0, len);
        }
    }

    /**
     * 追加字符
     */
    public Writer append(CharSequence csq) throws IOException {
        write(String.valueOf(csq));
        return this;
    }

    /**
     * 追加指定子序列，下标start到end
     */
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        if (csq == null) csq = "null";
        return append(csq.subSequence(start, end));
    }

    /**
     * 追加一个字符
     */
    public Writer append(char c) throws IOException {
        write(c);
        return this;
    }

    /**
     * 刷新流。从缓存区写入目标文件
     */
    public abstract void flush() throws IOException;

    /**
     * 关闭流
     */
    public abstract void close() throws IOException;

}

