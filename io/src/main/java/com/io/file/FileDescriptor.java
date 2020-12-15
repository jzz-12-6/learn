//package com.io.file;
//
//
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//
///**
// * 表示打开的文件或者套接字
// *  FileOutputStream out = new FileOutputStream(FileDescriptor.out);
// *  out.write('A');
// *  out.close();
// *  相当于System.out.print('A');
// */
//public final class FileDescriptor {
//    /**
//     * 表示0输入、1输出和2错误
//     */
//    private int fd;
//
//    private long handle;
//
//    private Closeable parent;
//    private List<Closeable> otherParents;
//    private boolean closed;
//
//    /**
//     * 文件是否被追加
//     */
//    private boolean append;
//
//    static {
//        initIDs();
//    }
//
//    // Set up JavaIOFileDescriptorAccess in SharedSecrets
//    static {
//        SharedSecrets.setJavaIOFileDescriptorAccess(
//                new JavaIOFileDescriptorAccess() {
//                    public void set(FileDescriptor fdo, int fd) {
//                        fdo.set(fd);
//                    }
//
//                    public int get(FileDescriptor fdo) {
//                        return fdo.fd;
//                    }
//
//                    public void setAppend(FileDescriptor fdo, boolean append) {
//                        fdo.append = append;
//                    }
//
//                    public boolean getAppend(FileDescriptor fdo) {
//                        return fdo.append;
//                    }
//
//                    public void close(FileDescriptor fdo) throws IOException {
//                        fdo.close();
//                    }
//
//                    /* Register for a normal FileCleanable fd/handle cleanup. */
//                    public void registerCleanup(FileDescriptor fdo) {
//                        FileCleanable.register(fdo);
//                    }
//
//                    /* Register a custom PhantomCleanup. */
//                    public void registerCleanup(FileDescriptor fdo,
//                                                PhantomCleanable<FileDescriptor> cleanup) {
//                        fdo.registerCleanup(cleanup);
//                    }
//
//                    public void unregisterCleanup(FileDescriptor fdo) {
//                        fdo.unregisterCleanup();
//                    }
//
//                    public void setHandle(FileDescriptor fdo, long handle) {
//                        fdo.setHandle(handle);
//                    }
//
//                    public long getHandle(FileDescriptor fdo) {
//                        return fdo.handle;
//                    }
//                }
//        );
//    }
//
//
//    private PhantomCleanable<FileDescriptor> cleanup;
//
//    /**
//     * 无效的构造器
//     * 未指定fd和handle
//     */
//    public FileDescriptor() {
//        fd = -1;
//        handle = -1;
//    }
//
//    /**
//     * 指定文件操作类型
//     */
//    private FileDescriptor(int fd) {
//        this.fd = fd;
//        this.handle = getHandle(fd);
//        this.append = getAppend(fd);
//    }
//
//    /**
//     * 标准输入流，不能直接使用
//     * 通过System.in
//     */
//    public static final FileDescriptor in = new FileDescriptor(0);
//
//    /**
//     * 标准输出流，不能直接使用
//     * 通过System.out
//     */
//    public static final FileDescriptor out = new FileDescriptor(1);
//
//    /**
//     * 标准错误，不能直接使用
//     * 通过System.err
//     */
//    public static final FileDescriptor err = new FileDescriptor(2);
//
//    /**
//     * 是否可用
//     */
//    public boolean valid() {
//        return (handle != -1) || (fd != -1);
//    }
//
//    /**
//     * 强制所有系统缓冲区与底层设备同步。将FileDescriptor的所有修改数据和属性都写入相关设备后返回
//     */
//    public native void sync() throws SyncFailedException;
//
//
//    private static native void initIDs();
//
//
//    private static native long getHandle(int d);
//
//
//    private static native boolean getAppend(int fd);
//
//    /**
//     * 设置fd
//     */
//    @SuppressWarnings("unchecked")
//    synchronized void set(int fd) {
//        if (fd == -1 && cleanup != null) {
//            //cleanup.clear();
//            cleanup = null;
//        }
//        this.fd = fd;
//    }
//
//    /**
//     * 设置handle.
//     */
//    @SuppressWarnings("unchecked")
//    void setHandle(long handle) {
//        if (handle == -1 && cleanup != null) {
//            //cleanup.clear();
//            cleanup = null;
//        }
//        this.handle = handle;
//    }
//
//    /**
//     * 注册当前handle
//     */
//    @SuppressWarnings("unchecked")
//    synchronized void registerCleanup(PhantomCleanable<FileDescriptor> cleanable) {
//        Objects.requireNonNull(cleanable, "cleanable");
//        if (cleanup != null) {
//            cleanup.clear();
//        }
//        cleanup = cleanable;
//    }
//
//    /**
//     * 取消注册当前原始FD或者处理的清除
//     */
//    synchronized void unregisterCleanup() {
//        if (cleanup != null) {
//            cleanup.clear();
//        }
//        cleanup = null;
//    }
//
//    /**
//     * 关闭文件描述符或句柄
//     */
//    @SuppressWarnings("unchecked")
//    synchronized void close() throws IOException {
//        unregisterCleanup();
//        close0();
//    }
//
//    /*
//     * Close the raw file descriptor or handle, if it has not already been closed
//     * and set the fd and handle to -1.
//     */
//    private native void close0() throws IOException;
//
//    /*
//     * Package private methods to track referents.
//     * If multiple streams point to the same FileDescriptor, we cycle
//     * through the list of all referents and call close()
//     */
//
//    /**
//     * Attach a Closeable to this FD for tracking.
//     * parent reference is added to otherParents when
//     * needed to make closeAll simpler.
//     */
//    synchronized void attach(Closeable c) {
//        if (parent == null) {
//            // first caller gets to do this
//            parent = c;
//        } else if (otherParents == null) {
//            otherParents = new ArrayList<>();
//            otherParents.add(parent);
//            otherParents.add(c);
//        } else {
//            otherParents.add(c);
//        }
//    }
//
//    /**
//     * Cycle through all Closeables sharing this FD and call
//     * close() on each one.
//     *
//     * The caller closeable gets to call close0().
//     */
//    @SuppressWarnings("try")
//    synchronized void closeAll(Closeable releaser) throws IOException {
//        if (!closed) {
//            closed = true;
//            IOException ioe = null;
//            try (releaser) {
//                if (otherParents != null) {
//                    for (Closeable referent : otherParents) {
//                        try {
//                            referent.close();
//                        } catch(IOException x) {
//                            if (ioe == null) {
//                                ioe = x;
//                            } else {
//                                ioe.addSuppressed(x);
//                            }
//                        }
//                    }
//                }
//            } catch(IOException ex) {
//                /*
//                 * If releaser close() throws IOException
//                 * add other exceptions as suppressed.
//                 */
//                if (ioe != null)
//                    ex.addSuppressed(ioe);
//                ioe = ex;
//            } finally {
//                if (ioe != null)
//                    throw ioe;
//            }
//        }
//    }
//}
//
