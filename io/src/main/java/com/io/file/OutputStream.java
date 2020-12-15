package com.io.file;


import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.Objects;

/**
 * This abstract class is the superclass of all classes representing
 * an output stream of bytes. An output stream accepts output bytes
 * and sends them to some sink.
 * <p>
 * Applications that need to define a subclass of
 * <code>OutputStream</code> must always provide at least a method
 * that writes one byte of output.
 *
 * @author  Arthur van Hoff
 * @see     java.io.BufferedOutputStream
 * @see     java.io.ByteArrayOutputStream
 * @see     java.io.DataOutputStream
 * @see     java.io.FilterOutputStream
 * @see     java.io.InputStream
 * @see     OutputStream#write(int)
 * @since   1.0
 */
public abstract class OutputStream implements Closeable, Flushable {
    /**
     * 创建一个新的输出流
     */
    public static OutputStream nullOutputStream() {
        return new OutputStream() {
            private volatile boolean closed;

            private void ensureOpen() throws IOException {
                if (closed) {
                    throw new IOException("Stream closed");
                }
            }

            @Override
            public void write(int b) throws IOException {
                ensureOpen();
            }

            @Override
            public void write(byte b[], int off, int len) throws IOException {
                Objects.checkFromIndexSize(off, len, b.length);
                ensureOpen();
            }

            @Override
            public void close() {
                closed = true;
            }
        };
    }

    /**
     * 将一个字节写入到输出流
     */
    public abstract void write(int b) throws IOException;

    /**
     * 将一个字节数组写入到输出流
     */
    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    /**
     * 从指定的字节数组写入 len字节，从偏移量 off开始输出到此输出流。
     */
    public void write(byte b[], int off, int len) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);
        // len == 0 condition implicitly handled by loop bounds
        for (int i = 0 ; i < len ; i++) {
            write(b[off + i]);
        }
    }

    /**
     * 刷新此输出流并强制任何缓冲的输出字节被写出。
     */
    public void flush() throws IOException {
    }

    public void close() throws IOException {
    }

}
