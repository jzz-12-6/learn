//package com.io.file;
//
//
//
//import java.io.*;
//import java.io.OutputStream;
//import java.util.Formatter;
//import java.util.Locale;
//import java.nio.charset.Charset;
//import java.nio.charset.IllegalCharsetNameException;
//import java.nio.charset.UnsupportedCharsetException;
//
///**
// * 打印流，可以打印到控制台，相当System.in
// * 或者同步刷新到文件中
// */
//
//public class PrintStream extends FilterOutputStream
//        implements Appendable, Closeable
//{
//
//    private final boolean autoFlush;
//    private boolean trouble = false;
//    private Formatter formatter;
//
//    /**
//     * 跟踪输出流，以便刷新缓冲区
//     */
//    private BufferedWriter textOut;
//    private OutputStreamWriter charOut;
//
//
//    private static <T> T requireNonNull(T obj, String message) {
//        if (obj == null)
//            throw new NullPointerException(message);
//        return obj;
//    }
//
//    /**
//     * 字符串转字符集编码
//     */
//    private static Charset toCharset(String csn)
//            throws UnsupportedEncodingException
//    {
//        requireNonNull(csn, "charsetName");
//        try {
//            return Charset.forName(csn);
//        } catch (IllegalCharsetNameException|UnsupportedCharsetException unused) {
//            throw new UnsupportedEncodingException(csn);
//        }
//    }
//
//
//    private PrintStream(boolean autoFlush, java.io.OutputStream out) {
//        super(out);
//        this.autoFlush = autoFlush;
//        this.charOut = new OutputStreamWriter(this);
//        this.textOut = new BufferedWriter(charOut);
//    }
//
//
//    private PrintStream(boolean autoFlush, Charset charset, java.io.OutputStream out) {
//        this(out, autoFlush, charset);
//    }
//
//    /**
//     * 指定OutputStream的构造器。自动刷新为false
//     */
//    public PrintStream(java.io.OutputStream out) {
//        this(out, false);
//    }
//
//    /**
//     * 指定OutputStream的构造器以及是否自动刷新
//     * 为true只要写入字节数组，就会刷新输出缓冲区
//     */
//    public PrintStream(java.io.OutputStream out, boolean autoFlush) {
//        this(autoFlush, requireNonNull(out, "Null output stream"));
//    }
//
//    /**
//     * 指定OutputStream的构造器以及是否自动刷新、字符集
//     * 为true只要写入字节数组，就会刷新输出缓冲区
//     */
//    public PrintStream(java.io.OutputStream out, boolean autoFlush, String encoding)
//            throws UnsupportedEncodingException
//    {
//        this(requireNonNull(out, "Null output stream"), autoFlush, toCharset(encoding));
//    }
//
//    /**
//     * 指定OutputStream的构造器以及是否自动刷新、字符集
//     * 为true只要写入字节数组，就会刷新输出缓冲区
//     */
//    public PrintStream(OutputStream out, boolean autoFlush, Charset charset) {
//        super(out);
//        this.autoFlush = autoFlush;
//        this.charOut = new OutputStreamWriter(this, charset);
//        this.textOut = new BufferedWriter(charOut);
//    }
//
//    /**
//     * 指定路径创建打印流，不会自动刷新
//     */
//    public PrintStream(String fileName) throws FileNotFoundException {
//        this(false, new FileOutputStream(fileName));
//    }
//
//    /**
//     * 指定路径和字符集创建打印流，不会自动刷新
//     */
//    public PrintStream(String fileName, String csn)
//            throws FileNotFoundException, UnsupportedEncodingException
//    {
//        this(false, toCharset(csn), new FileOutputStream(fileName));
//    }
//
//    public PrintStream(String fileName, Charset charset) throws IOException {
//        this(false, requireNonNull(charset, "charset"), new FileOutputStream(fileName));
//    }
//
//    /**
//     * 指定文件创建打印流
//     */
//    public PrintStream(File file) throws FileNotFoundException {
//        this(false, new FileOutputStream(file));
//    }
//
//    /**
//     * 指定文件以及字符集创建打印流
//     */
//    public PrintStream(File file, String csn)
//            throws FileNotFoundException, UnsupportedEncodingException
//    {
//        this(false, toCharset(csn), new FileOutputStream(file));
//    }
//
//
//    /**
//     * 指定文件以及字符集创建打印流
//     */
//    public PrintStream(File file, Charset charset) throws IOException {
//        this(false, requireNonNull(charset, "charset"), new FileOutputStream(file));
//    }
//
//
//    private void ensureOpen() throws IOException {
//        if (out == null)
//            throw new IOException("Stream closed");
//    }
//
//    /**
//     * 刷新流
//     */
//    public void flush() {
//        synchronized (this) {
//            try {
//                ensureOpen();
//                out.flush();
//            }
//            catch (IOException x) {
//                trouble = true;
//            }
//        }
//    }
//
//    private boolean closing = false;
//
//
//    public void close() {
//        synchronized (this) {
//            if (! closing) {
//                closing = true;
//                try {
//                    textOut.close();
//                    out.close();
//                }
//                catch (IOException x) {
//                    trouble = true;
//                }
//                textOut = null;
//                charOut = null;
//                out = null;
//            }
//        }
//    }
//
//    /**
//     * 刷新流并检查其错误状态
//     */
//    public boolean checkError() {
//        if (out != null)
//            flush();
//        if (out instanceof PrintStream) {
//            PrintStream ps = (PrintStream) out;
//            return ps.checkError();
//        }
//        return trouble;
//    }
//
//    /**
//     * 将流的错误状态设置为true 。
//     */
//    protected void setError() {
//        trouble = true;
//    }
//
//    /**
//     * 清除此流的内部错误状态。
//     */
//    protected void clearError() {
//        trouble = false;
//    }
//
//
//
//    /**
//     * 写入一个字节
//     */
//    public void write(int b) {
//        try {
//            synchronized (this) {
//                ensureOpen();
//                out.write(b);
//                if ((b == '\n') && autoFlush)
//                    out.flush();
//            }
//        }
//        catch (InterruptedIOException x) {
//            Thread.currentThread().interrupt();
//        }
//        catch (IOException x) {
//            trouble = true;
//        }
//    }
//
//    /**
//     * 从buf[off]写入len个长度字节
//     */
//    public void write(byte buf[], int off, int len) {
//        try {
//            synchronized (this) {
//                ensureOpen();
//                out.write(buf, off, len);
//                if (autoFlush)
//                    out.flush();
//            }
//        }
//        catch (InterruptedIOException x) {
//            Thread.currentThread().interrupt();
//        }
//        catch (IOException x) {
//            trouble = true;
//        }
//    }
//
//
//
//    private void write(char[] buf) {
//        try {
//            synchronized (this) {
//                ensureOpen();
//                textOut.write(buf);
//                textOut.flushBuffer();
//                charOut.flushBuffer();
//                if (autoFlush) {
//                    for (int i = 0; i < buf.length; i++)
//                        if (buf[i] == '\n') {
//                            out.flush();
//                            break;
//                        }
//                }
//            }
//        } catch (InterruptedIOException x) {
//            Thread.currentThread().interrupt();
//        } catch (IOException x) {
//            trouble = true;
//        }
//    }
//
//
//    private void writeln(char[] buf) {
//        try {
//            synchronized (this) {
//                ensureOpen();
//                textOut.write(buf);
//                textOut.newLine();
//                textOut.flushBuffer();
//                charOut.flushBuffer();
//                if (autoFlush)
//                    out.flush();
//            }
//        }
//        catch (InterruptedIOException x) {
//            Thread.currentThread().interrupt();
//        }
//        catch (IOException x) {
//            trouble = true;
//        }
//    }
//
//    private void write(String s) {
//        try {
//            synchronized (this) {
//                ensureOpen();
//                textOut.write(s);
//                textOut.flushBuffer();
//                charOut.flushBuffer();
//                if (autoFlush && (s.indexOf('\n') >= 0))
//                    out.flush();
//            }
//        }
//        catch (InterruptedIOException x) {
//            Thread.currentThread().interrupt();
//        }
//        catch (IOException x) {
//            trouble = true;
//        }
//    }
//
//
//    private void writeln(String s) {
//        try {
//            synchronized (this) {
//                ensureOpen();
//                textOut.write(s);
//                textOut.newLine();
//                textOut.flushBuffer();
//                charOut.flushBuffer();
//                if (autoFlush)
//                    out.flush();
//            }
//        }
//        catch (InterruptedIOException x) {
//            Thread.currentThread().interrupt();
//        }
//        catch (IOException x) {
//            trouble = true;
//        }
//    }
//
//    private void newLine() {
//        try {
//            synchronized (this) {
//                ensureOpen();
//                textOut.newLine();
//                textOut.flushBuffer();
//                charOut.flushBuffer();
//                if (autoFlush)
//                    out.flush();
//            }
//        }
//        catch (InterruptedIOException x) {
//            Thread.currentThread().interrupt();
//        }
//        catch (IOException x) {
//            trouble = true;
//        }
//    }
//
//
//
//    /**
//     * 打印一个boolean
//     */
//    public void print(boolean b) {
//        write(String.valueOf(b));
//    }
//
//    /**
//     * 打印一个char
//     */
//    public void print(char c) {
//        write(String.valueOf(c));
//    }
//
//    /**
//     * 打印一个int
//     */
//    public void print(int i) {
//        write(String.valueOf(i));
//    }
//
//    /**
//     * 打印一个long
//     */
//    public void print(long l) {
//        write(String.valueOf(l));
//    }
//
//    /**
//     * 打印一个float
//     */
//    public void print(float f) {
//        write(String.valueOf(f));
//    }
//
//    /**
//     * 打印一个double
//     */
//    public void print(double d) {
//        write(String.valueOf(d));
//    }
//
//    /**
//     * 打印一个char数组
//     */
//    public void print(char s[]) {
//        write(s);
//    }
//
//    /**
//     * 打印一个字符串
//     */
//    public void print(String s) {
//        write(String.valueOf(s));
//    }
//
//    /**
//     * 打印一个对象
//     */
//    public void print(Object obj) {
//        write(String.valueOf(obj));
//    }
//
//
//    /* Methods that do terminate lines */
//
//    /**
//     * 打印一个换行符
//     */
//    public void println() {
//        newLine();
//    }
//
//    /**
//     * 打印一个boolean和一个换行符
//     */
//    public void println(boolean x) {
//        if (getClass() == PrintStream.class) {
//            writeln(String.valueOf(x));
//        } else {
//            synchronized (this) {
//                print(x);
//                newLine();
//            }
//        }
//    }
//
//    /**
//     * 打印一个char和一个换行符
//     */
//    public void println(char x) {
//        if (getClass() == PrintStream.class) {
//            writeln(String.valueOf(x));
//        } else {
//            synchronized (this) {
//                print(x);
//                newLine();
//            }
//        }
//    }
//
//    /**
//     * 打印一个int和一个换行符
//     */
//    public void println(int x) {
//        if (getClass() == PrintStream.class) {
//            writeln(String.valueOf(x));
//        } else {
//            synchronized (this) {
//                print(x);
//                newLine();
//            }
//        }
//    }
//
//    /**
//     * 打印一个long和一个换行符
//     */
//    public void println(long x) {
//        if (getClass() == PrintStream.class) {
//            writeln(String.valueOf(x));
//        } else {
//            synchronized (this) {
//                print(x);
//                newLine();
//            }
//        }
//    }
//
//    /**
//     * 打印一个float和一个换行符
//     */
//    public void println(float x) {
//        if (getClass() == PrintStream.class) {
//            writeln(String.valueOf(x));
//        } else {
//            synchronized (this) {
//                print(x);
//                newLine();
//            }
//        }
//    }
//
//    /**
//     * 打印一个double和一个换行符
//     */
//    public void println(double x) {
//        if (getClass() == PrintStream.class) {
//            writeln(String.valueOf(x));
//        } else {
//            synchronized (this) {
//                print(x);
//                newLine();
//            }
//        }
//    }
//
//    /**
//     * 打印一个char[]和一个换行符
//     */
//    public void println(char[] x) {
//        if (getClass() == PrintStream.class) {
//            writeln(x);
//        } else {
//            synchronized (this) {
//                print(x);
//                newLine();
//            }
//        }
//    }
//
//    /**
//     * 打印一个String和一个换行符
//     */
//    public void println(String x) {
//        if (getClass() == PrintStream.class) {
//            writeln(String.valueOf(x));
//        } else {
//            synchronized (this) {
//                print(x);
//                newLine();
//            }
//        }
//    }
//
//    /**
//     * 打印一个对象和一个换行符
//     */
//    public void println(Object x) {
//        String s = String.valueOf(x);
//        if (getClass() == PrintStream.class) {
//            writeln(String.valueOf(s));
//        } else {
//            synchronized (this) {
//                print(s);
//                newLine();
//            }
//        }
//    }
//
//
//    /**
//     * 使用指定的格式字符串和参数将格式化字符串写入此输出流
//     */
//    public PrintStream printf(String format, Object ... args) {
//        return format(format, args);
//    }
//
//
//    public PrintStream printf(Locale l, String format, Object ... args) {
//        return format(l, format, args);
//    }
//
//
//    public PrintStream format(String format, Object ... args) {
//        try {
//            synchronized (this) {
//                ensureOpen();
//                if ((formatter == null)
//                        || (formatter.locale() !=
//                        Locale.getDefault(Locale.Category.FORMAT)))
//                    formatter = new Formatter((Appendable) this);
//                formatter.format(Locale.getDefault(Locale.Category.FORMAT),
//                        format, args);
//            }
//        } catch (InterruptedIOException x) {
//            Thread.currentThread().interrupt();
//        } catch (IOException x) {
//            trouble = true;
//        }
//        return this;
//    }
//
//
//    public PrintStream format(Locale l, String format, Object ... args) {
//        try {
//            synchronized (this) {
//                ensureOpen();
//                if ((formatter == null)
//                        || (formatter.locale() != l))
//                    formatter = new Formatter(this, l);
//                formatter.format(l, format, args);
//            }
//        } catch (InterruptedIOException x) {
//            Thread.currentThread().interrupt();
//        } catch (IOException x) {
//            trouble = true;
//        }
//        return this;
//    }
//
//    /**
//     * 追加字符
//     */
//    public PrintStream append(CharSequence csq) {
//        print(String.valueOf(csq));
//        return this;
//    }
//
//
//    public PrintStream append(CharSequence csq, int start, int end) {
//        if (csq == null) csq = "null";
//        return append(csq.subSequence(start, end));
//    }
//
//
//    public PrintStream append(char c) {
//        print(c);
//        return this;
//    }
//
//}
//
