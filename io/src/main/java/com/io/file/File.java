//package com.io.file;
//
//
//import java.io.*;
//import java.net.URI;
//import java.net.URL;
//import java.net.MalformedURLException;
//import java.net.URISyntaxException;
//import java.util.List;
//import java.util.ArrayList;
//import java.security.SecureRandom;
//import java.nio.file.Path;
//import java.nio.file.FileSystems;
////import sun.security.action.GetPropertyAction;
//
//
///**
// * 表示文件或者目录路径
// */
//public class File
//        implements Serializable, Comparable<File>
//{
//
//    /**
//     * 获取系统的文件对象
//     */
//    private static final FileSystem fs = DefaultFileSystem.getFileSystem();
//
//    /**
//     * 路径
//     */
//    private final String path;
//
//    /**
//     * 文件路径状态的枚举类型
//     */
//    private static enum PathStatus { INVALID, CHECKED };
//
//    /**
//     * 文件路径是否有效
//     */
//    private transient PathStatus status = null;
//
//
//    /**
//     * 文件路径是否无效
//     */
//    final boolean isInvalid() {
//        if (status == null) {
//            status = (this.path.indexOf('\u0000') < 0) ? PathStatus.CHECKED
//                    : PathStatus.INVALID;
//        }
//        return status == PathStatus.INVALID;
//    }
//
//    /**
//     * 路径名前缀长度
//     */
//    private final transient int prefixLength;
//
//
//    int getPrefixLength() {
//        return prefixLength;
//    }
//
//    /**
//     * 系统默认名称分割符
//     * UNIX /
//     * Windows \\
//     */
//    public static final char separatorChar = fs.getSeparator();
//
//    /**
//     * 系统分割符
//     */
//    public static final String separator = "" + separatorChar;
//
//    /**
//     * 系统默认路径分割符
//     * UNIX :
//     * Windows ;
//     */
//    public static final char pathSeparatorChar = fs.getPathSeparator();
//
//    /**
//     * 系统默认路径分割符
//     */
//    public static final String pathSeparator = "" + pathSeparatorChar;
//
//
//    /* -- Constructors -- */
//
//    /**
//     * 文件路径和长度的构造器
//     */
//    private File(String pathname, int prefixLength) {
//        this.path = pathname;
//        this.prefixLength = prefixLength;
//    }
//
//    /**
//     * 文件路径和文件夹的构造器
//     */
//    private File(String child, File parent) {
//        assert parent.path != null;
//        assert (!parent.path.equals(""));
//        this.path = fs.resolve(parent.path, child);
//        this.prefixLength = parent.prefixLength;
//    }
//
//    /**
//     * 文件路径的构造器
//     */
//    public File(String pathname) {
//        if (pathname == null) {
//            throw new NullPointerException();
//        }
//        this.path = fs.normalize(pathname);
//        this.prefixLength = fs.prefixLength(this.path);
//    }
//    /**
//     * 空参数会将/或者\\作为父级
//     */
//
//
//    /**
//     * 父级路径和子路径的构造器
//     */
//    public File(String parent, String child) {
//        if (child == null) {
//            throw new NullPointerException();
//        }
//        if (parent != null) {
//            if (parent.equals("")) {
//                this.path = fs.resolve(fs.getDefaultParent(),
//                        fs.normalize(child));
//            } else {
//                this.path = fs.resolve(fs.normalize(parent),
//                        fs.normalize(child));
//            }
//        } else {
//            this.path = fs.normalize(child);
//        }
//        this.prefixLength = fs.prefixLength(this.path);
//    }
//
//    /**
//     * 父对象和子路径的构造器
//     */
//    public File(File parent, String child) {
//        if (child == null) {
//            throw new NullPointerException();
//        }
//        if (parent != null) {
//            if (parent.path.equals("")) {
//                this.path = fs.resolve(fs.getDefaultParent(),
//                        fs.normalize(child));
//            } else {
//                this.path = fs.resolve(parent.path,
//                        fs.normalize(child));
//            }
//        } else {
//            this.path = fs.normalize(child);
//        }
//        this.prefixLength = fs.prefixLength(this.path);
//    }
//
//    /**
//     * 将网络地址的构造器
//     */
//    public File(URI uri) {
//
//        if (!uri.isAbsolute())
//            throw new IllegalArgumentException("URI is not absolute");
//        if (uri.isOpaque())
//            throw new IllegalArgumentException("URI is not hierarchical");
//        String scheme = uri.getScheme();
//        if ((scheme == null) || !scheme.equalsIgnoreCase("file"))
//            throw new IllegalArgumentException("URI scheme is not \"file\"");
//        if (uri.getRawAuthority() != null)
//            throw new IllegalArgumentException("URI has an authority component");
//        if (uri.getRawFragment() != null)
//            throw new IllegalArgumentException("URI has a fragment component");
//        if (uri.getRawQuery() != null)
//            throw new IllegalArgumentException("URI has a query component");
//        String p = uri.getPath();
//        if (p.equals(""))
//            throw new IllegalArgumentException("URI path component is empty");
//
//        p = fs.fromURIPath(p);
//        if (File.separatorChar != '/')
//            p = p.replace('/', File.separatorChar);
//        this.path = fs.normalize(p);
//        this.prefixLength = fs.prefixLength(this.path);
//    }
//
//
//    /* -- Path-component accessors -- */
//
//    /**
//     * 返回文件名称
//     */
//    public String getName() {
//        int index = path.lastIndexOf(separatorChar);
//        if (index < prefixLength) return path.substring(prefixLength);
//        return path.substring(index + 1);
//    }
//
//    /**
//     * 返回文件父级路径名称
//     * 父级为空返回null
//     */
//    public String getParent() {
//        int index = path.lastIndexOf(separatorChar);
//        if (index < prefixLength) {
//            if ((prefixLength > 0) && (path.length() > prefixLength))
//                return path.substring(0, prefixLength);
//            return null;
//        }
//        return path.substring(0, index);
//    }
//
//    /**
//     * 返回父级文件
//     */
//    public File getParentFile() {
//        String p = this.getParent();
//        if (p == null) return null;
//        return new File(p, this.prefixLength);
//    }
//
//    /**
//     * 返回文件全路径
//     */
//    public String getPath() {
//        return path;
//    }
//
//
//    /* -- 路径操作 -- */
//
//    /**
//     * 文件路径是否是绝对路径
//     * 文件路径为/或者\\是绝对的
//     */
//    public boolean isAbsolute() {
//        return fs.isAbsolute(this);
//    }
//
//    /**
//     * 返回文件的绝对路径
//     */
//    public String getAbsolutePath() {
//        return fs.resolve(this);
//    }
//
//    /**
//     * 根据绝对路径返回文件
//     */
//    public File getAbsoluteFile() {
//        String absPath = getAbsolutePath();
//        return File(absPath, fs.prefixLength(absPath));
//    }
//
//    /**
//     * 返回规范路径
//     * 相当于绝对路径，且删除冗余比如。..
//     */
//    public String getCanonicalPath() throws IOException {
//        if (isInvalid()) {
//            throw new IOException("Invalid file path");
//        }
//        return fs.canonicalize(fs.resolve(this));
//    }
//
//    /**
//     * 返回规范路径的文件
//     */
//    public File getCanonicalFile() throws IOException {
//        String canonPath = getCanonicalPath();
//        return new File(canonPath, fs.prefixLength(canonPath));
//    }
//
//    private static String slashify(String path, boolean isDirectory) {
//        String p = path;
//        if (File.separatorChar != '/')
//            p = p.replace(File.separatorChar, '/');
//        if (!p.startsWith("/"))
//            p = "/" + p;
//        if (!p.endsWith("/") && isDirectory)
//            p = p + "/";
//        return p;
//    }
//
//    /**
//     * Converts this abstract pathname into a <code>file:</code> URL.  The
//     * exact form of the URL is system-dependent.  If it can be determined that
//     * the file denoted by this abstract pathname is a directory, then the
//     * resulting URL will end with a slash.
//     *
//     * @return  A URL object representing the equivalent file URL
//     *
//     * @throws  MalformedURLException
//     *          If the path cannot be parsed as a URL
//     *
//     * @see     #toURI()
//     * @see     java.net.URI
//     * @see     java.net.URI#toURL()
//     * @see     java.net.URL
//     * @since   1.2
//     *
//     * @deprecated This method does not automatically escape characters that
//     * are illegal in URLs.  It is recommended that new code convert an
//     * abstract pathname into a URL by first converting it into a URI, via the
//     * {@link #toURI() toURI} method, and then converting the URI into a URL
//     * via the {@link java.net.URI#toURL() URI.toURL} method.
//     */
//    @Deprecated
//    public URL toURL() throws MalformedURLException {
//        if (isInvalid()) {
//            throw new MalformedURLException("Invalid file path");
//        }
//        return new URL("file", "", slashify(getAbsolutePath(), isDirectory()));
//    }
//
//    /**
//     * 转URI文件
//     */
//    public URI toURI() {
//        try {
//            File f = getAbsoluteFile();
//            String sp = slashify(f.getPath(), f.isDirectory());
//            if (sp.startsWith("//"))
//                sp = "//" + sp;
//            return new URI("file", null, sp, null);
//        } catch (URISyntaxException x) {
//            throw new Error(x);         // Can't happen
//        }
//    }
//
//
//    /* -- 属性访问 -- */
//
//    /**
//     * 文件是否可读
//     */
//    public boolean canRead() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkRead(path);
//        }
//        if (isInvalid()) {
//            return false;
//        }
//        return fs.checkAccess(this, FileSystem.ACCESS_READ);
//    }
//
//    /**
//     * 文件是否可写
//     */
//    public boolean canWrite() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkWrite(path);
//        }
//        if (isInvalid()) {
//            return false;
//        }
//        return fs.checkAccess(this, FileSystem.ACCESS_WRITE);
//    }
//
//    /**
//     * 文件是否存在
//     */
//    public boolean exists() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkRead(path);
//        }
//        if (isInvalid()) {
//            return false;
//        }
//        return ((fs.getBooleanAttributes(this) & FileSystem.BA_EXISTS) != 0);
//    }
//
//    /**
//     * 该文件是否为目录
//     */
//    public boolean isDirectory() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkRead(path);
//        }
//        if (isInvalid()) {
//            return false;
//        }
//        return ((fs.getBooleanAttributes(this) & FileSystem.BA_DIRECTORY)
//                != 0);
//    }
//
//    /**
//     * 是否为文件
//     */
//    public boolean isFile() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkRead(path);
//        }
//        if (isInvalid()) {
//            return false;
//        }
//        return ((fs.getBooleanAttributes(this) & FileSystem.BA_REGULAR) != 0);
//    }
//
//    /**
//     * 是否为隐藏文件
//     */
//    public boolean isHidden() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkRead(path);
//        }
//        if (isInvalid()) {
//            return false;
//        }
//        return ((fs.getBooleanAttributes(this) & FileSystem.BA_HIDDEN) != 0);
//    }
//
//    /**
//     * 文件最后修改时间
//     */
//    public long lastModified() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkRead(path);
//        }
//        if (isInvalid()) {
//            return 0L;
//        }
//        return fs.getLastModifiedTime(this);
//    }
//
//    /**
//     * 文件长度
//     */
//    public long length() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkRead(path);
//        }
//        if (isInvalid()) {
//            return 0L;
//        }
//        return fs.getLength(this);
//    }
//
//
//    /* -- 文件操作 -- */
//
//    /**
//     * 文件不存在的时候创建一个文件
//     */
//    public boolean createNewFile() throws IOException {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) security.checkWrite(path);
//        if (isInvalid()) {
//            throw new IOException("Invalid file path");
//        }
//        return fs.createFileExclusively(path);
//    }
//
//    /**
//     * 删除文件或者目录
//     * 如果是目录，必须为空才能删除
//     */
//    public boolean delete() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkDelete(path);
//        }
//        if (isInvalid()) {
//            return false;
//        }
//        return fs.delete(this);
//    }
//
//    /**
//     * 删除文件或者目录
//     * 当虚拟机停止后才真正进行删除
//     */
//    public void deleteOnExit() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkDelete(path);
//        }
//        if (isInvalid()) {
//            return;
//        }
//        //jvm退出后进行删除
//        DeleteOnExitHook.add(path);
//    }
//
//    /**
//     * 返回目录下所有文件名称
//     * 若路径为文件返回null
//     */
//    public String[] list() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkRead(path);
//        }
//        if (isInvalid()) {
//            return null;
//        }
//        return fs.list(this);
//    }
//
//    /**
//     * 根据过滤器返回文件名称
//     */
//    public String[] list(FilenameFilter filter) {
//        String names[] = list();
//        if ((names == null) || (filter == null)) {
//            return names;
//        }
//        List<String> v = new ArrayList<>();
//        for (int i = 0 ; i < names.length ; i++) {
//            if (filter.accept(this, names[i])) {
//                v.add(names[i]);
//            }
//        }
//        return v.toArray(new String[v.size()]);
//    }
//
//    /**
//     * 返回文件夹下所有文件
//     */
//    public File[] listFiles() {
//        String[] ss = list();
//        if (ss == null) return null;
//        int n = ss.length;
//        File[] fs = new File[n];
//        for (int i = 0; i < n; i++) {
//            fs[i] = new File(ss[i], this);
//        }
//        return fs;
//    }
//
//    /**
//     * 根据文件名称过滤器返回文件
//     */
//    public File[] listFiles(FilenameFilter filter) {
//        String ss[] = list();
//        if (ss == null) return null;
//        ArrayList<File> files = new ArrayList<>();
//        for (String s : ss)
//            if ((filter == null) || filter.accept(this, s))
//                files.add(new File(s, this));
//        return files.toArray(new File[files.size()]);
//    }
//
//    /**
//     * 根据文件过滤器返回文件
//     */
//    public File[] listFiles(FileFilter filter) {
//        String ss[] = list();
//        if (ss == null) return null;
//        ArrayList<File> files = new ArrayList<>();
//        for (String s : ss) {
//            File f = new File(s, this);
//            if ((filter == null) || filter.accept(f))
//                files.add(f);
//        }
//        return files.toArray(new File[files.size()]);
//    }
//
//    /**
//     * 创建文件夹
//     */
//    public boolean mkdir() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkWrite(path);
//        }
//        if (isInvalid()) {
//            return false;
//        }
//        return fs.createDirectory(this);
//    }
//
//    /**
//     * 创建文件夹，包含父目录
//     */
//    public boolean mkdirs() {
//        if (exists()) {
//            return false;
//        }
//        if (mkdir()) {
//            return true;
//        }
//        File canonFile = null;
//        try {
//            canonFile = getCanonicalFile();
//        } catch (IOException e) {
//            return false;
//        }
//
//        File parent = canonFile.getParentFile();
//        return (parent != null && (parent.mkdirs() || parent.exists()) &&
//                canonFile.mkdir());
//    }
//
//    /**
//     * 重命名文件
//     */
//    public boolean renameTo(File dest) {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkWrite(path);
//            security.checkWrite(dest.path);
//        }
//        if (dest == null) {
//            throw new NullPointerException();
//        }
//        if (this.isInvalid() || dest.isInvalid()) {
//            return false;
//        }
//        return fs.rename(this, dest);
//    }
//
//    /**
//     * 设置最后一次修改时间
//     */
//    public boolean setLastModified(long time) {
//        if (time < 0) throw new IllegalArgumentException("Negative time");
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkWrite(path);
//        }
//        if (isInvalid()) {
//            return false;
//        }
//        return fs.setLastModifiedTime(this, time);
//    }
//
//    /**
//     * 设置文件只读
//     */
//    public boolean setReadOnly() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkWrite(path);
//        }
//        if (isInvalid()) {
//            return false;
//        }
//        return fs.setReadOnly(this);
//    }
//
//    /**
//     * 设置文件所有者的写入权限
//     */
//    public boolean setWritable(boolean writable, boolean ownerOnly) {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkWrite(path);
//        }
//        if (isInvalid()) {
//            return false;
//        }
//        return fs.setPermission(this, FileSystem.ACCESS_WRITE, writable, ownerOnly);
//    }
//
//    /**
//     * 设置文件所有者的写入权限
//     */
//    public boolean setWritable(boolean writable) {
//        return setWritable(writable, true);
//    }
//
//    /**
//     * 设置文件拥有者读取权限
//     */
//    public boolean setReadable(boolean readable, boolean ownerOnly) {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkWrite(path);
//        }
//        if (isInvalid()) {
//            return false;
//        }
//        return fs.setPermission(this, FileSystem.ACCESS_READ, readable, ownerOnly);
//    }
//
//    /**
//     * 设置文件拥有者读取权限
//     */
//    public boolean setReadable(boolean readable) {
//        return setReadable(readable, true);
//    }
//
//    /**
//     * 设置文件所有者的执行权限
//     */
//    public boolean setExecutable(boolean executable, boolean ownerOnly) {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkWrite(path);
//        }
//        if (isInvalid()) {
//            return false;
//        }
//        return fs.setPermission(this, FileSystem.ACCESS_EXECUTE, executable, ownerOnly);
//    }
//
//    /**
//     * 设置文件所有者的执行权限
//     */
//    public boolean setExecutable(boolean executable) {
//        return setExecutable(executable, true);
//    }
//
//    /**
//     * 文件是否可执行
//     */
//    public boolean canExecute() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkExec(path);
//        }
//        if (isInvalid()) {
//            return false;
//        }
//        return fs.checkAccess(this, FileSystem.ACCESS_EXECUTE);
//    }
//
//
//    /* -- Filesystem interface -- */
//
//    /**
//     * 可用的文件系统根目录
//     * windows c|d|e
//     */
//    public static File[] listRoots() {
//        return fs.listRoots();
//    }
//
//
//    /* -- 磁盘使用情况 -- */
//
//    /**
//     * 返回文件路径的分区大小
//     */
//    public long getTotalSpace() {
//        SecurityManager sm = System.getSecurityManager();
//        if (sm != null) {
//            sm.checkPermission(new RuntimePermission("getFileSystemAttributes"));
//            sm.checkRead(path);
//        }
//        if (isInvalid()) {
//            return 0L;
//        }
//        return fs.getSpace(this, FileSystem.SPACE_TOTAL);
//    }
//
//    /**
//     * 返回文件路径的分区可用大小
//     */
//    public long getFreeSpace() {
//        SecurityManager sm = System.getSecurityManager();
//        if (sm != null) {
//            sm.checkPermission(new RuntimePermission("getFileSystemAttributes"));
//            sm.checkRead(path);
//        }
//        if (isInvalid()) {
//            return 0L;
//        }
//        return fs.getSpace(this, FileSystem.SPACE_FREE);
//    }
//
//    /**
//     * 同方法getFreeSpace，值更准确
//     */
//    public long getUsableSpace() {
//        SecurityManager sm = System.getSecurityManager();
//        if (sm != null) {
//            sm.checkPermission(new RuntimePermission("getFileSystemAttributes"));
//            sm.checkRead(path);
//        }
//        if (isInvalid()) {
//            return 0L;
//        }
//        return fs.getSpace(this, FileSystem.SPACE_USABLE);
//    }
//
//    /* -- 临时文件 -- */
//
//    private static class TempDirectory {
//        private TempDirectory() { }
//
//        // temporary directory location
//        private static final File tmpdir = new File(
//                GetPropertyAction.privilegedGetProperty("java.io.tmpdir"));
//        static File location() {
//            return tmpdir;
//        }
//
//        // file name generation
//        private static final SecureRandom random = new SecureRandom();
//        private static int shortenSubName(int subNameLength, int excess,
//                                          int nameMin) {
//            int newLength = Math.max(nameMin, subNameLength - excess);
//            if (newLength < subNameLength) {
//                return newLength;
//            }
//            return subNameLength;
//        }
//        static File generateFile(String prefix, String suffix, File dir)
//                throws IOException
//        {
//            long n = random.nextLong();
//            String nus = Long.toUnsignedString(n);
//
//            // Use only the file name from the supplied prefix
//            prefix = (new File(prefix)).getName();
//
//            int prefixLength = prefix.length();
//            int nusLength = nus.length();
//            int suffixLength = suffix.length();;
//
//            String name;
//            int nameMax = fs.getNameMax(dir.getPath());
//            int excess = prefixLength + nusLength + suffixLength - nameMax;
//            if (excess <= 0) {
//                name = prefix + nus + suffix;
//            } else {
//                // Name exceeds the maximum path component length: shorten it
//
//                // Attempt to shorten the prefix length to no less then 3
//                prefixLength = shortenSubName(prefixLength, excess, 3);
//                excess = prefixLength + nusLength + suffixLength - nameMax;
//
//                if (excess > 0) {
//                    // Attempt to shorten the suffix length to no less than
//                    // 0 or 4 depending on whether it begins with a dot ('.')
//                    suffixLength = shortenSubName(suffixLength, excess,
//                            suffix.indexOf(".") == 0 ? 4 : 0);
//                    suffixLength = shortenSubName(suffixLength, excess, 3);
//                    excess = prefixLength + nusLength + suffixLength - nameMax;
//                }
//
//                if (excess > 0 && excess <= nusLength - 5) {
//                    // Attempt to shorten the random character string length
//                    // to no less than 5
//                    nusLength = shortenSubName(nusLength, excess, 5);
//                }
//
//                StringBuilder sb =
//                        new StringBuilder(prefixLength + nusLength + suffixLength);
//                sb.append(prefixLength < prefix.length() ?
//                        prefix.substring(0, prefixLength) : prefix);
//                sb.append(nusLength < nus.length() ?
//                        nus.substring(0, nusLength) : nus);
//                sb.append(suffixLength < suffix.length() ?
//                        suffix.substring(0, suffixLength) : suffix);
//                name = sb.toString();
//            }
//
//            // Normalize the path component
//            name = fs.normalize(name);
//
//            File f = new File(dir, name);
//            if (!name.equals(f.getName()) || f.isInvalid()) {
//                if (System.getSecurityManager() != null)
//                    throw new IOException("Unable to create temporary file");
//                else
//                    throw new IOException("Unable to create temporary file, "
//                            + name);
//            }
//            return f;
//        }
//    }
//
//    /**
//     * 在指定文件目录中创建一个空文件，使用给定的前缀和后缀生成名称
//     */
//    public static File createTempFile(String prefix, String suffix,
//                                              File directory)
//            throws IOException
//    {
//        if (prefix.length() < 3) {
//            throw new IllegalArgumentException("Prefix string \"" + prefix +
//                    "\" too short: length must be at least 3");
//        }
//        if (suffix == null)
//            suffix = ".tmp";
//
//        File tmpdir = (directory != null) ? directory
//                : File.TempDirectory.location();
//        SecurityManager sm = System.getSecurityManager();
//        File f;
//        do {
//            f = File.TempDirectory.generateFile(prefix, suffix, tmpdir);
//
//            if (sm != null) {
//                try {
//                    sm.checkWrite(f.getPath());
//                } catch (SecurityException se) {
//                    // don't reveal temporary directory location
//                    if (directory == null)
//                        throw new SecurityException("Unable to create temporary file");
//                    throw se;
//                }
//            }
//        } while ((fs.getBooleanAttributes(f) & FileSystem.BA_EXISTS) != 0);
//
//        if (!fs.createFileExclusively(f.getPath()))
//            throw new IOException("Unable to create temporary file");
//
//        return f;
//    }
//
//    /**
//     * 在临时文件目录中创建一个空文件，使用给定的前缀和后缀生成名称
//     */
//    public static File createTempFile(String prefix, String suffix)
//            throws IOException
//    {
//        return createTempFile(prefix, suffix, null);
//    }
//
//    /* -- Basic infrastructure -- */
//
//    public int compareTo(File pathname) {
//        return fs.compare(this, pathname);
//    }
//
//
//    public boolean equals(Object obj) {
//        if ((obj != null) && (obj instanceof File)) {
//            return compareTo((File)obj) == 0;
//        }
//        return false;
//    }
//
//
//    public int hashCode() {
//        return fs.hashCode(this);
//    }
//
//
//    public String toString() {
//        return getPath();
//    }
//
//
//    private synchronized void writeObject(java.io.ObjectOutputStream s)
//            throws IOException
//    {
//        s.defaultWriteObject();
//        s.writeChar(separatorChar); // Add the separator character
//    }
//
//
//    private synchronized void readObject(java.io.ObjectInputStream s)
//            throws IOException, ClassNotFoundException
//    {
//        ObjectInputStream.GetField fields = s.readFields();
//        String pathField = (String)fields.get("path", null);
//        char sep = s.readChar(); // read the previous separator char
//        if (sep != separatorChar)
//            pathField = pathField.replace(sep, separatorChar);
//        String path = fs.normalize(pathField);
//        UNSAFE.putObject(this, PATH_OFFSET, path);
//        UNSAFE.putIntVolatile(this, PREFIX_LENGTH_OFFSET, fs.prefixLength(path));
//    }
//
//    private static final jdk.internal.misc.Unsafe UNSAFE
//            = jdk.internal.misc.Unsafe.getUnsafe();
//    private static final long PATH_OFFSET
//            = UNSAFE.objectFieldOffset(File.class, "path");
//    private static final long PREFIX_LENGTH_OFFSET
//            = UNSAFE.objectFieldOffset(File.class, "prefixLength");
//
//    /** use serialVersionUID from JDK 1.0.2 for interoperability */
//    private static final long serialVersionUID = 301077366599181567L;
//
//    // -- Integration with java.nio.file --
//
//    private transient volatile Path filePath;
//
//    /**
//     * 返回从此抽象路径构造的一个Path对象
//     */
//    public Path toPath() {
//        Path result = filePath;
//        if (result == null) {
//            synchronized (this) {
//                result = filePath;
//                if (result == null) {
//                    result = FileSystems.getDefault().getPath(path);
//                    filePath = result;
//                }
//            }
//        }
//        return result;
//    }
//}
