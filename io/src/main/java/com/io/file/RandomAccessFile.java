//package com.io.file;
//
//
//
//import java.io.*;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.InputStream;
//import java.nio.channels.FileChannel;
//
//
///**
// * 读取和写入随机访问文件
// * 字节操作
// */
//
//public class RandomAccessFile implements DataOutput, DataInput, Closeable {
//
//    private FileDescriptor fd;
//    private volatile FileChannel channel;
//    /**
//     * 是否支持读写
//     */
//    private boolean rw;
//
//    /**
//     * 文件路径
//     */
//    private final String path;
//
//    private final Object closeLock = new Object();
//
//    private volatile boolean closed;
//
//    private static final int O_RDONLY = 1;
//    private static final int O_RDWR =   2;
//    private static final int O_SYNC =   4;
//    private static final int O_DSYNC =  8;
//    private static final int O_TEMPORARY =  16;
//
//    /**
//     * 指定文件路径和读写模式
//     * 模式
//     * 1.r 读
//     * 2.rw 读写 操作的是基础存储设备上的文件；那么，关闭文件时，会将“文件内容的修改”同步到基础存储设备上。至于，“更改文件内容”时，是否会立即同步，取决于系统底层实现。
//     * 3.rws 操作的是基础存储设备上的文件；那么，每次“更改文件内容[如write()写入数据]” 或 “修改文件元数据(如文件的mtime)”时，都会将这些改变同步到基础存储设备上。
//     * 4.rwd 操作的是基础存储设备上的文件；那么，每次“更改文件内容[如write()写入数据]”时，都会将这些改变同步到基础存储设备上。
//     */
//    public RandomAccessFile(String name, String mode)
//            throws FileNotFoundException
//    {
//        this(name != null ? new File(name) : null, mode);
//    }
//
//    /**
//     * 指定文件路径和读写模式
//     * 模式
//     * 1.r 读
//     * 2.rw 读写
//     * 3.rws
//     * 4.rwd
//     */
//    public RandomAccessFile(File file, String mode)
//            throws FileNotFoundException
//    {
//        this(file, mode, false);
//    }
//
//    private RandomAccessFile(File file, String mode, boolean openAndDelete)
//            throws FileNotFoundException
//    {
//        String name = (file != null ? file.getPath() : null);
//        int imode = -1;
//        if (mode.equals("r"))
//            imode = O_RDONLY;
//        else if (mode.startsWith("rw")) {
//            imode = O_RDWR;
//            rw = true;
//            if (mode.length() > 2) {
//                if (mode.equals("rws"))
//                    imode |= O_SYNC;
//                else if (mode.equals("rwd"))
//                    imode |= O_DSYNC;
//                else
//                    imode = -1;
//            }
//        }
//        if (openAndDelete)
//            imode |= O_TEMPORARY;
//        if (imode < 0)
//            throw new IllegalArgumentException("Illegal mode \"" + mode
//                    + "\" must be one of "
//                    + "\"r\", \"rw\", \"rws\","
//                    + " or \"rwd\"");
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkRead(name);
//            if (rw) {
//                security.checkWrite(name);
//            }
//        }
//        if (name == null) {
//            throw new NullPointerException();
//        }
//        if (file.isInvalid()) {
//            throw new FileNotFoundException("Invalid file path");
//        }
//        fd = new FileDescriptor();
//        fd.attach(this);
//        path = name;
//        open(name, imode);
//        FileCleanable.register(fd);
//    }
//
//    /**
//     * 返回FileDescriptor
//     */
//    public final FileDescriptor getFD() throws IOException {
//        if (fd != null) {
//            return fd;
//        }
//        throw new IOException();
//    }
//
//    /**
//     * 返回FileChannel
//     */
//    public final FileChannel getChannel() {
//        FileChannel fc = this.channel;
//        if (fc == null) {
//            synchronized (this) {
//                fc = this.channel;
//                if (fc == null) {
//                    this.channel = fc = FileChannelImpl.open(fd, path, true,
//                            rw, false, this);
//                    if (closed) {
//                        try {
//                            fc.close();
//                        } catch (IOException ioe) {
//                            throw new InternalError(ioe); // should not happen
//                        }
//                    }
//                }
//            }
//        }
//        return fc;
//    }
//
//
//    private native void open0(String name, int mode)
//            throws FileNotFoundException;
//
//
//
//    private void open(String name, int mode)
//            throws FileNotFoundException {
//        open0(name, mode);
//    }
//
//    // 'Read' primitives
//
//    /**
//     * 读取下一个字节
//     */
//    public int read() throws IOException {
//        return read0();
//    }
//
//    private native int read0() throws IOException;
//
//
//    private native int readBytes(byte b[], int off, int len) throws IOException;
//
//
//    public int read(byte b[], int off, int len) throws IOException {
//        return readBytes(b, off, len);
//    }
//
//
//    public int read(byte b[]) throws IOException {
//        return readBytes(b, 0, b.length);
//    }
//
//
//    public final void readFully(byte b[]) throws IOException {
//        readFully(b, 0, b.length);
//    }
//
//
//    public final void readFully(byte b[], int off, int len) throws IOException {
//        int n = 0;
//        do {
//            int count = this.read(b, off + n, len - n);
//            if (count < 0)
//                throw new EOFException();
//            n += count;
//        } while (n < len);
//    }
//
//    /**
//     * 跳过指定字节数
//     */
//    public int skipBytes(int n) throws IOException {
//        long pos;
//        long len;
//        long newpos;
//
//        if (n <= 0) {
//            return 0;
//        }
//        pos = getFilePointer();
//        len = length();
//        newpos = pos + n;
//        if (newpos > len) {
//            newpos = len;
//        }
//        seek(newpos);
//
//        return (int) (newpos - pos);
//    }
//
//    // 'Write' primitives
//
//    /**
//     * 写一个字节
//     */
//    public void write(int b) throws IOException {
//        write0(b);
//    }
//
//    private native void write0(int b) throws IOException;
//
//
//    private native void writeBytes(byte b[], int off, int len) throws IOException;
//
//
//    public void write(byte b[]) throws IOException {
//        writeBytes(b, 0, b.length);
//    }
//
//
//    public void write(byte b[], int off, int len) throws IOException {
//        writeBytes(b, off, len);
//    }
//
//    // 'Random access' stuff
//
//    /**
//     * 返回文件偏移量
//     */
//    public native long getFilePointer() throws IOException;
//
//    /**
//     * 设置文件从哪里开始读取或写入
//     */
//    public void seek(long pos) throws IOException {
//        if (pos < 0) {
//            throw new IOException("Negative seek offset");
//        } else {
//            seek0(pos);
//        }
//    }
//
//    private native void seek0(long pos) throws IOException;
//
//    /**
//     * 返回文件长度
//     */
//    public native long length() throws IOException;
//
//    /**
//     * 重设文件长度。若长度过小文件被截断
//     */
//    public native void setLength(long newLength) throws IOException;
//
//
//    public void close() throws IOException {
//        if (closed) {
//            return;
//        }
//        synchronized (closeLock) {
//            if (closed) {
//                return;
//            }
//            closed = true;
//        }
//
//        FileChannel fc = channel;
//        if (fc != null) {
//            fc.close();
//        }
//
//        fd.closeAll(new Closeable() {
//            public void close() throws IOException {
//                fd.close();
//            }
//        });
//    }
//
//
//
//    public final boolean readBoolean() throws IOException {
//        int ch = this.read();
//        if (ch < 0)
//            throw new EOFException();
//        return (ch != 0);
//    }
//
//
//    public final byte readByte() throws IOException {
//        int ch = this.read();
//        if (ch < 0)
//            throw new EOFException();
//        return (byte)(ch);
//    }
//
//
//    public final int readUnsignedByte() throws IOException {
//        int ch = this.read();
//        if (ch < 0)
//            throw new EOFException();
//        return ch;
//    }
//
//
//    public final short readShort() throws IOException {
//        int ch1 = this.read();
//        int ch2 = this.read();
//        if ((ch1 | ch2) < 0)
//            throw new EOFException();
//        return (short)((ch1 << 8) + (ch2 << 0));
//    }
//
//
//    public final int readUnsignedShort() throws IOException {
//        int ch1 = this.read();
//        int ch2 = this.read();
//        if ((ch1 | ch2) < 0)
//            throw new EOFException();
//        return (ch1 << 8) + (ch2 << 0);
//    }
//
//
//    public final char readChar() throws IOException {
//        int ch1 = this.read();
//        int ch2 = this.read();
//        if ((ch1 | ch2) < 0)
//            throw new EOFException();
//        return (char)((ch1 << 8) + (ch2 << 0));
//    }
//
//
//    public final int readInt() throws IOException {
//        int ch1 = this.read();
//        int ch2 = this.read();
//        int ch3 = this.read();
//        int ch4 = this.read();
//        if ((ch1 | ch2 | ch3 | ch4) < 0)
//            throw new EOFException();
//        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
//    }
//
//
//    public final long readLong() throws IOException {
//        return ((long)(readInt()) << 32) + (readInt() & 0xFFFFFFFFL);
//    }
//
//
//    public final float readFloat() throws IOException {
//        return Float.intBitsToFloat(readInt());
//    }
//
//
//    public final double readDouble() throws IOException {
//        return Double.longBitsToDouble(readLong());
//    }
//
//
//
//    public final String readLine() throws IOException {
//        StringBuilder input = new StringBuilder();
//        int c = -1;
//        boolean eol = false;
//
//        while (!eol) {
//            switch (c = read()) {
//                case -1:
//                case '\n':
//                    eol = true;
//                    break;
//                case '\r':
//                    eol = true;
//                    long cur = getFilePointer();
//                    if ((read()) != '\n') {
//                        seek(cur);
//                    }
//                    break;
//                default:
//                    input.append((char)c);
//                    break;
//            }
//        }
//
//        if ((c == -1) && (input.length() == 0)) {
//            return null;
//        }
//        return input.toString();
//    }
//
//
//    public final String readUTF() throws IOException {
//        return DataInputStream.readUTF(this);
//    }
//
//
//    public final void writeBoolean(boolean v) throws IOException {
//        write(v ? 1 : 0);
//    }
//
//
//    public final void writeByte(int v) throws IOException {
//        write(v);
//    }
//
//
//    public final void writeShort(int v) throws IOException {
//        write((v >>> 8) & 0xFF);
//        write((v >>> 0) & 0xFF);
//    }
//
//    public final void writeChar(int v) throws IOException {
//        write((v >>> 8) & 0xFF);
//        write((v >>> 0) & 0xFF);
//    }
//
//
//    public final void writeInt(int v) throws IOException {
//        write((v >>> 24) & 0xFF);
//        write((v >>> 16) & 0xFF);
//        write((v >>>  8) & 0xFF);
//        write((v >>>  0) & 0xFF);
//        //written += 4;
//    }
//
//
//    public final void writeLong(long v) throws IOException {
//        write((int)(v >>> 56) & 0xFF);
//        write((int)(v >>> 48) & 0xFF);
//        write((int)(v >>> 40) & 0xFF);
//        write((int)(v >>> 32) & 0xFF);
//        write((int)(v >>> 24) & 0xFF);
//        write((int)(v >>> 16) & 0xFF);
//        write((int)(v >>>  8) & 0xFF);
//        write((int)(v >>>  0) & 0xFF);
//        //written += 8;
//    }
//
//
//    public final void writeFloat(float v) throws IOException {
//        writeInt(Float.floatToIntBits(v));
//    }
//
//
//    public final void writeDouble(double v) throws IOException {
//        writeLong(Double.doubleToLongBits(v));
//    }
//
//
//    @SuppressWarnings("deprecation")
//    public final void writeBytes(String s) throws IOException {
//        int len = s.length();
//        byte[] b = new byte[len];
//        s.getBytes(0, len, b, 0);
//        writeBytes(b, 0, len);
//    }
//
//
//    public final void writeChars(String s) throws IOException {
//        int clen = s.length();
//        int blen = 2*clen;
//        byte[] b = new byte[blen];
//        char[] c = new char[clen];
//        s.getChars(0, clen, c, 0);
//        for (int i = 0, j = 0; i < clen; i++) {
//            b[j++] = (byte)(c[i] >>> 8);
//            b[j++] = (byte)(c[i] >>> 0);
//        }
//        writeBytes(b, 0, blen);
//    }
//
//
//    public final void writeUTF(String str) throws IOException {
//        DataOutputStream.writeUTF(str, this);
//    }
//
//    private static native void initIDs();
//
//    static {
//        initIDs();
//        SharedSecrets.setJavaIORandomAccessFileAccess(new JavaIORandomAccessFileAccess()
//        {
//            // This is for j.u.z.ZipFile.OPEN_DELETE. The O_TEMPORARY flag
//            // is only implemented/supported on windows.
//            public java.io.RandomAccessFile openAndDelete(File file, String mode)
//                    throws IOException
//            {
//                return new java.io.RandomAccessFile(file, mode, true);
//            }
//        });
//    }
//}
//
