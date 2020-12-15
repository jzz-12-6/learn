package com.io.file;



import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;

/**
 * 合并流，
 * 从一个有序的输入流开始，从第一个一次开始读取
 */
public class SequenceInputStream extends InputStream {
    Enumeration<? extends InputStream> e;
    InputStream in;

    /**
     * 指定Vector<InputStream>的构造器
     */
    public SequenceInputStream(Enumeration<? extends InputStream> e) {
        this.e = e;
        peekNextStream();
    }

    /**
     * 指定两个InputStream
     */
    public SequenceInputStream(InputStream s1, InputStream s2) {
        Vector<InputStream> v = new Vector<>(2);
        v.addElement(s1);
        v.addElement(s2);
        e = v.elements();
        peekNextStream();
    }


    final void nextStream() throws IOException {
        if (in != null) {
            in.close();
        }
        peekNextStream();
    }

    private void peekNextStream() {
        if (e.hasMoreElements()) {
            in = (InputStream) e.nextElement();
            if (in == null)
                throw new NullPointerException();
        } else {
            in = null;
        }
    }

    /**
     * 返回当前流可读取或跳过的字节数
     */
    public int available() throws IOException {
        if (in == null) {
            return 0;
        }
        return in.available();
    }

    /**
     * 读取一个字节
     */
    public int read() throws IOException {
        while (in != null) {
            int c = in.read();
            if (c != -1) {
                return c;
            }
            nextStream();
        }
        return -1;
    }


    public int read(byte b[], int off, int len) throws IOException {
        if (in == null) {
            return -1;
        } else if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }
        do {
            int n = in.read(b, off, len);
            if (n > 0) {
                return n;
            }
            nextStream();
        } while (in != null);
        return -1;
    }


    public void close() throws IOException {
        do {
            nextStream();
        } while (in != null);
    }
}

