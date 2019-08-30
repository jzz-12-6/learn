# 第 1 章 shell是什么

shell是一个接收由键盘输入的命令，并将其传递给操作系统来执行的程序。

## 1.2 第一次键盘输入

```shell
andms
提示 command not found
```

### 1.2.1 命令历史记录

上下方向键，最多保存500个命令

## 1.3 几个简单的命令

```[root@im-test-app ~]# date
date （查询当前系统的时间和日期）
Mon Aug 26 17:35:46 CST 2019

cal 获取当月日历
August 2019    
Su Mo Tu We Th Fr Sa
             1  2  3 
 4  5  6  7  8  9 10 
11 12 13 14 15 16 17 
18 19 20 21 22 23 24 
25 26 27 28 29 30 31 

df 查看磁盘可用空间
Filesystem      Size  Used Avail Use% Mounted on
devtmpfs        7.6G     0  7.6G   0% /dev
tmpfs           7.6G     0  7.6G   0% /dev/shm
tmpfs           7.6G   33M  7.6G   1% /run
tmpfs           7.6G     0  7.6G   0% /sys/fs/cgroup
/dev/nvme0n1p1  200G   28G  173G  14% /
tmpfs           1.6G     0  1.6G   0% /run/user/0

free 显示可用内存
total        used        free      shared  buff/cache   available
Mem:       15837212    12475660     2183088       51288     1178464     2951084
Swap:             0           0           0

```

## 1.4 结束终端会话

```shell
exist
```

# 第 2 章 导航

## 2.1 理解文件系统数

​	与Windows相同，类UNIX操作系统（如Linux）也是分层目录结构的方式来组织文件的。文件是在树形结构的目录中进行组织的，该树形结构目录可能包含文件和其他目录。

​	文件系统的第一个目录叫做跟目录，它包含了文件和子目录

## 2.2 当前工作目录

```shell
pwd 获取当前目录
/root/logs
```

## 2.3 列出目录内容

```shell
ls
im_backend_api-heartbeat.log    im_backend_api.log.20190729.0.log     
```

## 2.4 更改当前工作目录

```shell
cd 目标路径
```

### 2.4.1 绝对路径名自

```shell
cd /usr/bin
/ 代表绝对路径
相对路径
. 代表当前目录 
.. 代表当前目录的上一级目录
cd 将工作目录改变成主目录
cd ~ 将工作目录改变成先前的工作目录
cd ~ username 将工作目录变成username的工作目录
```

# 第 3 章 Linux系统

## 3.1 ls命令的乐趣

```java
ls 查看当前工作目录
ls -l 长格式显示（同ll）
total 16460
-rw-r--r-- 1 root root   129390 Aug  8 15:26 a.txt
drwxr-xr-x 2 root root       73 Jun 27 11:52 demo
-rw-r--r-- 1 root root 16677425 Feb 27 10:36 demo-0.0.1-SNAPSHOT.jar

ls /usr 指定工作目录
ls ~ /usr 指定多个目录
```

### 3.1.1 选项和参数

命令后面跟有一个或多个选项，命令后面也会跟一个或多个参数，大部分命令如下

```shell
command -options arguments
```

大部分命令使用的选项是在单个字符前加上连接符如-l，也支持单字符前加两个连字符的长选项如--reverse。也可以多个短选项串在一起使用

```shell
ls -lt l长格式输出 t以文件修改时间降序
ls -lt --reverse 反序输出
```

| ls命令常用选项 | 长选项           | 描述                                                         |
| -------------- | ---------------- | ------------------------------------------------------------ |
| -a             | -all             | 列出所有文件，包括以点号开头的文件（这些文件通常是不列出的） |
| -d             | --directory      | 如-l结合使用，可以查看目录的详细信息而不是目录的内容         |
| -F             | --classify       | 选项会在每个累出的名字后面加上类型指示符（如果是目录名，则会加上一个/） |
| -h             | --human-readable | 以长格式列出，以人民可读的方式而不是字节数来显示文件大小。   |
| -l             |                  | 使用长格式显示结果                                           |
| -r             | --reverse        | 倒序显示结果                                                 |
| -S             |                  | 按文件大小对结果排序                                         |
| -t             |                  | 按修改时间排序                                               |

### 3.1.2 进一步了解长列表格式

```shell
-rw-r--r-- 1 root root   129390 Aug  8 15:26 a.txt
drwxr-xr-x 2 root root       73 Jun 27 11:52 demo
-rw-r--r-- 1 root root 16677425 Feb 27 10:36 demo-0.0.1-SNAPSHOT.jar
```

| ls长列表字段 |                                                              |
| ------------ | ------------------------------------------------------------ |
| -rw-r--r--   | 对文件的访问权限，第一个字符表示文件的类型。在不同的类型之间，开头的-表示该文件是一个普通文件，d表示目录，紧接着的三个字符表示文件所有者的访问权，在接着三个字符表示文件所属组中成员的访问权限，最后三个字符表示其他所有人的访问权限 |
| l            | 文件硬连接数目                                               |
| root         | 文件所有者的用户名‘                                          |
| root         | 文件所属用户组的名称                                         |
| 129390       | 以字节数表示文件的大小                                       |
| Aug  8 15:26 | 上次修改文件的日期和时间                                     |
| a.txt        | 文件名                                                       |

## 3.2 使用file命令确定文件类型

```shell
file a.txt 
a.txt: ASCII text
```

## 3.3 使用less命令查看文件内容（同more）

```shell
less fileName
```

| less命令              |                                                          |
| --------------------- | -------------------------------------------------------- |
| Page up 或 b          | 后翻一页                                                 |
| Page down 或 Spacebar | 前翻一页                                                 |
| 上箭头                | 向上一行                                                 |
| 下箭头                | 向下一行                                                 |
| G                     | 跳转到末尾                                               |
| 1G或g                 | 跳转到开头                                               |
| /charecters           | 向前查找指定的字符串                                     |
| n                     | 向前查找下一个出现的字符串，这个字符串是之前所指定查找的 |
| h                     | 显示帮助                                                 |
| q                     | 退出                                                     |

## 3.4 快速浏览

| Linux系统中的目录 |                                                              |
| ----------------- | ------------------------------------------------------------ |
| /                 | 根目录                                                       |
| /bin              | 包含系统启动和运行所需的二进制文件                           |
| /boot             | 包含Linux内核，最初的RAM磁盘映像，以及启动加载程序<br/>grub/grub.conf 或menu.list 用来配置启动加载程序<br/>vmlinuz,Linux内核 |
| /dev              | 这是一个包含设备节点的特殊目录。把一切当成文件也适用于设备。内核将它能够识别的所以设备存在这个目录 |
| /etc              | 包含了所有系统层面的配置文件，同时也包含了一系列shell脚本，系统每次启动时，这些shell脚本都会打开每个系统服务。该目录中包含的内容都应该是可读的文本文件。<br/>crontab 该文件定义了自动化任务运行的时间<br/>fstab 存储设备以及相关挂载点的列表 |
| /home             | 每个用户都会在/home目录中拥有一个属于自己的目录              |
| /lib              | 包含核心系统程序使用的共享库文件。与Windows中的DLL类似       |
| /lost+found       | 当文件系统崩溃时，该目录用于恢复分区。                       |
| /media            | 包含可移除媒体设备的挂载点。比如USB驱动。这些设备插入计算机后，会自动挂载到这个目录下 |
| /opt              | 安装其他可选软件。                                           |
| /proc             | 是一个Linux内核维护的虚拟文件系统。包含的文件是内核的窥视孔。从中可以看到内核是如何监管计算机的 |
| /root             | root账户主目录                                               |
| /sbin             | 放置系统二进制文件                                           |
| /tmp              | 用户存放各类程序创建的临时文件目录，系统每次重启时会清空该目录 |
| /usr              | 包含普通用户使用的所有程序和相关文件<br/>/bin 放置一些Linux发行版安装的可执行程序<br/>/lib bin目录中的程序使用的共享库<br/>/local 让系统使用的程序的安装目录<br/>/sbin 包含更多的系统管理程序<br/>/share 包含了bin中程序的全部共享数据，包含默认配置文件，图标等<br/>/share/doc 文档文件 |
| /var              | 存储可变的数据<br/>/log 包含日志文件，记录了各种系统获得     |

## 3.5 符号链接

```shell
lrw 1 root root  4096 Aug 27 03:21 a.xt -> a-2.6.txt
```

第一个字母是l,而且有两个文件名。这种特殊的文件叫做符号链接(又叫软链接或symlink)。

用处：更改文件的时候不用更改名称，只需要更改引用就行

# 第 4章 操作文件与目录

1. cp 复制文件和目录
2. mv 移动或重命名文件和目录
3. mkdir 创建目录
4. rm 删除文件和目录
5. in 创建硬连接和符号链接

## 4.1 通配符

| 通配符        | 匹配项                             |
| ------------- | ---------------------------------- |
| *             | 匹配任意多个字符（包括0个和1个）   |
| ？            | 匹配任意单个字符（不包括0个）      |
| [characters]  | 匹配任意一个属于字符集中的字符     |
| [!characters] | 匹配任意一个不属于字符集中的字符   |
| [[:class:]]   | 匹配任意一个属于指定字符类中的字符 |

| 常用字符类 |                          |
| ---------- | ------------------------ |
| [:alnum:]  | 匹配任意一个字母或者数字 |
| [:alpha:]  | 匹配任意一个字母         |
| [:digit:]  | 匹配任意一个数字         |
| [:lower:]  | 匹配任意一个小写字母     |
| [:upper:]  | 匹配任意一个大写字母     |

| 通配符示例      | 匹配项                                             |
| --------------- | -------------------------------------------------- |
| *               | 所以文件                                           |
| g*              | 以g开头的任意文件                                  |
| b*.txt          | 以b开头的,中间任意多个字符，并以,txt结尾的任意文件 |
| Data???         | 以Data开头，后面跟3个字符的任一文件                |
| [abc]*          | 以abc中的任一一个开头的任一文件                    |
| a[0-9]          | 以a开头，后跟一个数字的任一文件                    |
| [[:upper:]]*    | 以大型字母开头的任一文件                           |
| [![:digit:]]*   | 不以数字开头的任一文件                             |
| *[[:lower:]123] | 以小写字母或数字1、2、3中的任一个结尾的任一文件    |

## 4.2 mkdir 创建目录

```shell
mkdir [OPTION]... DIRECTORY...
如果参数后面带...表示该参数可重复
mkdir dir1 dir2 
创建 dir1 dir2两个文件夹
```

## 4.3 cp 复制文件和目录

```shell
Usage: cp [OPTION]... [-T] SOURCE DEST
  or:  cp [OPTION]... SOURCE... DIRECTORY
  or:  cp [OPTION]... -t DIRECTORY SOURCE...
Copy SOURCE to DEST, or multiple SOURCE(s) to DIRECTORY.
```

| cp命令选项          |                                                              |
| ------------------- | ------------------------------------------------------------ |
| -a, --archive       | 复制文件和目录及其属性，包括所有权和权限。<br/>通常来说，复制的文件具有用户所操作文件的默认属性 |
| -i, --interactive   | 在覆盖一个已存在的文件前，提示用户进行确认。<br/>如果没有指定该选项，cp会默认覆盖文件 |
| -R, -r, --recursive | 递归地复制目录及其内容。<br/>复制目录时需要这个选择（或-a选项） |
| -u, --update        | 当将文件从一个目录复制到另一个目录时，只会复制那些目标目录中不存在的文件或是目标目录响应文件的更新文件 |
| -v, --verbose       | 复制文件时，显示信息性消息                                   |

```shell
cp file1 file2 
将file1复制到file2，如果file2存在，则会被file1的内容覆盖，如果file2不存在，则创建file2
cp -i file1 file2
file2存在是，覆盖之前通知用户确认
cp file1 file2 dir1 
将file1，file2复制到目录dir1里。dir1必须已经存在
cp dir1/* dir2 
使用通配符，将dir1中的所有文件复制到dir2中，dir2必须已经存在
cp -r dir1 dir2 
将dir1目录及其内容复制到dir2中，如果dir2不存在，创建dir2，且包含于dir1目录相同的内容
```

## 4.4 mv 移除和重命名文件

```shell
Usage: mv [OPTION]... [-T] SOURCE DEST
  or:  mv [OPTION]... SOURCE... DIRECTORY
  or:  mv [OPTION]... -t DIRECTORY SOURCE...
Rename SOURCE to DEST, or move SOURCE(s) to DIRECTORY.
文件移动和文件重命名操作
```

命令基本和cp命令共享

```shell
操作示例
mv file1 file2 
将file1移动到file2，如果file2存在，则会被file1的内容覆盖，如果file2不存在，则创建file2
mv file1 file2 dir1 
将file1，file2移动到目录dir1里。dir1必须已经存在
mv  dir1 dir2 
将dir1目录及其内容移动到dir2中，如果dir2不存在，创建dir2，同时删除dir1
```

## 4.5 rm 删除文件和目录

```shell
Usage: rm [OPTION]... FILE...
Remove (unlink) the FILE(s).
rm 一旦删除变无法回滚
```

| rm选项              |                                                              |
| ------------------- | ------------------------------------------------------------ |
| -f, --force         | 忽略不存在文件并无需提示。<br/>该选项会覆盖--interactive选项 |
| -i,--interactive    | 删除已存在的文件，提示用户确认。                             |
| -r, -R, --recursive | 递归删除目录，如果有子目录也会删除。<br/>如果删除一个目录的话，必须指定该选项 |
| -v, --verbose       | 删除文件时显示信息性消息                                     |

```shell
rm file1
删除file1，提示用户确认
rm -r file1 dir1
删除file1，dir1以及它们的内容
rm -rf file1 dir1
删除file1，dir1以及它们的内容。当file1或dir1，rm仍会继续执行，且不提示用户
```

## 4.6 ln 创建连接

```shell
Usage: ln [OPTION]... [-T] TARGET LINK_NAME   (1st form)
  or:  ln [OPTION]... TARGET                  (2nd form)
  or:  ln [OPTION]... TARGET... DIRECTORY     (3rd form)
  or:  ln [OPTION]... -t DIRECTORY TARGET...  (4th form)
In the 1st form, create a link to TARGET with the name LINK_NAME.
In the 2nd form, create a link to TARGET in the current directory.
In the 3rd and 4th forms, create links to each TARGET in DIRECTORY.
Create hard links by default, symbolic links with --symbolic.

ln file link
创建硬连接
ln -s item link
创建符号链接
```

## 4.6.1 硬连接

​	默认情况下每个文件有一个硬连接，该硬连接会给文件起名字。当创建一个硬连接的时候，也为这个文件创建了一个额外的目录条目。

硬链接的局限性：

1. 硬链接不能引用自身文件系统之外的文件。连接不能引用与该连接不在同一磁盘分区的文件
2. 硬链接无法引用目录

硬链接和文件本身没有什么区别。当硬连接被删除时，只是删除了这个链接，但是文件本身的内容一人存在。

### 4.6.2 符号链接

​	符号链接是通过创建一个特殊类型的文件来起作用的，该文件包含了指向引用文件或目录的文本指针。与Windows中的快捷方式相似。

​	当往符号链接写入东西时，也会写进引用文件。当删除符号链接时，不会删除文件本身，如果先删除文件，那么链接依然存在，但却不指向任何文件。这个链接称为坏链接。ls会用不同颜色(如红色)显示怀链接

# 第 5 章 命令的使用

1. type说明如何解释命令名
2. which：显示会指向哪些可执行程序
3. man 显示命令的手册页
4. aproops 显示一系列合适的命令
5. ingo 显示命令的info条目
6. whatis 显示一条命令的简述
7. alias 创建一条命令的别名

## 5.1 什么是命令

	1. 可执行的程序。如usr/bin目录下的文件
 	2. shell内置命令
 	3. shell函数
 	4. alias命令

## 5.2 识别命令

### 5.2.1 type 显示命令类型

```shell
type command
type cd
cd is a shell builtin
type ls
ls is aliased to `ls --color=auto' (ls为什么带有颜色)
```

### 5.2.2 which 显示可执行程序的位置

```shell
# which ls
alias ls='ls --color=auto'
	/usr/bin/ls

输入错误命令
# which a
/usr/bin/which: no a in (/usr/local/java/jdk1.8.0_201/bin:/usr/local/java/jdk1.8.0_201/jre/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/usr/local/java/jdk1.8.0_201/bin:/usr/local/maven3/bin:/root/bin)

```

## 5.3 获得命令文档

### 5.3.1 help 获得shell内置命令的帮助文档

```shell
help cd
cd: cd [-L|[-P [-e]]] [dir]
    Change the shell working directory.
    
    Change the current directory to DIR.  The default DIR is the value of the
    HOME shell variable.

mkdir --help
Usage: mkdir [OPTION]... DIRECTORY...
Create the DIRECTORY(ies), if they do not already exist.

Mandatory arguments to long options are mandatory for short options too.
  -m, --mode=MODE   set file mode (as in chmod), not a=rwx - umask
  -p, --parents     no error if existing, make parent directories as needed
  -v, --verbose     print a message for each created directory
  -Z                   set SELinux security context of each created directory
                         to the default type
      --context[=CTX]  like -Z, or if CTX is specified then set the SELinux
                         or SMACK security context to CTX
      --help     display this help and exit
      --version  output version information and exit

```

### 5.3.3 man 显示程序的手册页

```shell
man commond 
man命令调用less命令来显示手册文档（被分成多个部分）
man section commond
查询部分文档
```

| 手册文档的组织结构 |                                |
| ------------------ | ------------------------------ |
| 1                  | 用户命令                       |
| 2                  | 内核系统调用的程序接口         |
| 3                  | C库函数程序接口                |
| 4                  | 特殊文件，如设备节点和驱动程序 |
| 5                  | 文件格式                       |
| 6                  | 游戏和娱乐                     |
| 7                  | 其他杂项                       |
| 8                  | 系统管理命令                   |

### 5.3.4 apropos 显示合适的命令

```shell
apropos floppy
fd (4)               - floppy disk device
fdformat (8)         - low-level format a floppy disk
第一个字段为手册页名称 
第二个字段显示部分（section）
```

### 5.3.5 whatis 显示命令的简要描述

```shell
whatis ls
ls (1)               - list directory contents
```

### 5.3.6 info 显示程序的info条目

info程序读取info文件，该文件是树形就够，分为各个单独的节点，每一个节点包含一个注解。通过前置*号可以识别超链接，并按enter建跳转

| info命令              |                             |
| --------------------- | --------------------------- |
| ?                     | 显示命令帮助                |
| page up or backspace  | 返回上一页                  |
| page down or spacebar | 下一页                      |
| n                     | next-显示下一个节点         |
| p                     | previous--显示上一个节点    |
| u                     | up-显示目前显示节点的父节点 |
| enter                 | 进入光标所指的超链接        |
| q                     | 退出                        |

### 5.3.7 readme 和其他程序文档文件

```shell
zless *.gz
显示由gzip压缩的文本文件的内容
```

## 5.4 使用别名创建自己的命令

 ```shell
command1;command2;command3
可以执行多个命令用;隔开
# cd /root;ls
default.etcd   get.txt                   
先执行cd命令，在执行ls

alias name='command1;command2;command3'
创建别名
name 使用别名
unalias name 删除别名
 ```

# 第 6 章 重定向

1. cat 合并文件
2. sort 对文本行排序
3. uniq 报告或删除文件中重复的行
4. wc 打印文件中的换行符、字和字节的个数
5. grep 打印匹配行
6. head 输出文件的第一部分内容
7. tail 输出文件的最后一部分内容
8. tee 读取标准输入的数据、并将其内容输出到标准输出和文件中

## 6.1 标准输入、标准输出和标准错误

### 6.1.1 标准输出重定向

​	使用重定向操作符“>”，后接文件名，可以把把标准输入重定向到另一个文件中，而不是显示在屏幕上

```shell
[root@im-test-app ~]# ll > b.txt
每次会覆盖文件
[root@im-test-app ~]# ls
a.txt  b.txt  demo  demo-0.0.1-SNAPSHOT.jar  fileName  *.lalazhong.com.tar.gz  logs  messages.txt  register2019-08-08.txt  test2.sh  test_file.zzz  test.sh
[root@im-test-app ~]# more b.txt 
total 16460
-rw-r--r-- 1 root root   129390 Aug  8 15:26 a.txt
-rw-r--r-- 1 root root        0 Aug 29 15:29 b.txt
drwxr-xr-x 2 root root       73 Jun 27 11:52 demo
-rw-r--r-- 1 root root 16677425 Feb 27  2019 demo-0.0.1-SNAPSHOT.jar
-rw-r--r-- 1 root root     2325 Aug  8 16:45 fileName
-rw-r--r-- 1 root root     4946 May 20 17:38 *.lalazhong.com.tar.gz
drwxr-xr-x 3 root root     8192 Aug 29 10:54 logs
-rw-r--r-- 1 root root     1138 May 28 11:36 messages.txt

> b.txt 
删除一个已存在的文件内容或者创建一个新的空文件
ll >> b.txt
追加文件
```

### 6.1.2 标准错误重定向

shell内部用文件描述符0，1，2来对应标准输入文件，标准输出文件和标准错误文件。

```shell
ll 2> error.txt
将标准错误重定向到error.txt
```

### 6.1.3 将标准输出和标准错误重定向到同一个文件

```shell
在旧版本中的shell中使用
ll > info.txt 2>&1
首先重定向标准输出到info.txt文件中，然后使用标记符2》&1把文件描述符2重定向到文件描述符1中，顺序很重要，否则不起作用

ll &> info.txt 作用同上
```

### 6.1.4 处理不想要的输出

```shell
隐藏一个命令的错误信息
ll 2> /dev/null
```

### 6.1.5 标准输入重定向

```shell
读取一个或多个文件，并把它们复制到标准输出文件中
[root@im-test-app ~]# cat --help
Usage: cat [OPTION]... [FILE]...
Concatenate FILE(s), or standard input, to standard output.

  -A, --show-all           equivalent to -vET
  -b, --number-nonblank    number nonempty output lines, overrides -n
  -e                       equivalent to -vE
  -E, --show-ends          display $ at end of each line
  -n, --number             number all output lines
  -s, --squeeze-blank      suppress repeated empty output lines
  -t                       equivalent to -vT
  -T, --show-tabs          display TAB characters as ^I
  -u                       (ignored)
  -v, --show-nonprinting   use ^ and M- notation, except for LFD and TAB
      --help     display this help and exit
      --version  output version information and exit

With no FILE, or when FILE is -, read standard input.

Examples:
  cat f - g  Output f's contents, then standard input, then g's contents.
  cat        Copy standard input to standard output.
cat *.txt > a.txt
将所有txt文件合并一个
cat > a.txt
将键盘输入内容重定向到a.txt
```

### 6.2 管道

使用管道操作符"|"可以把一个命令的标准输出传送到另一个命令的标准输入中

```shell
command1 | command2

ll | less
检查任意一条生成标准输出的命令的运行结果
```

### 6.2.1 过滤器

```shell
ls /root /var | sort | less
将/root和/var目录下合并成一个列表，并排序
```

### 6.2.2 uniq 报告忽略文件中重复的行

```shell
ls /root /var | sort | uniq | less
删除列表中的重复行
ls /root /var | sort | uniq -d | less
只查看重复行
```

### 6.2.3 wc 打印行数、字数和字节数

```shell
# wc 3.sh 
 19  51 496 3.sh
# wc -l 3.sh  只显示行数
19 3.sh
```

### 6.2.4 grep 打印匹配行

在文件中查找匹配文本

```shell
grep pattern [file...]
grep for 3.sh 
for line in $(cat ${filePath})
-i grep在搜索时忽略大小写
-v 输出不匹配项
```

### 6.2.5 head/tall 打印文件的开头部分/结尾部分

```shell
head/tail -n 10 a.txt
打印a.txt中前/后10行 
-n 调整输出的行数
tail -f a.txt
tail将持续监视这个文件，一旦添加了新行，新行就会立即显示。Ctrl+C停止
```

### 6.2.6 tee 从stdin读取数据，并同时输出到stdout和文件

tee 程序读取标准输入，再把读到的内容复制到标准输出

```shell
ls | tee a.txt | grep zip
获取整个目录并输入到a.txt
```

# 第 7 章 透过shell看世界

## 7.1 扩展

```shell
echo 将文本参数内容打印到标准输出
```

### 7.1.1 路径名扩展

```shell
echo D*
打印 D开头的文件
```

### 7.1.2 波浪线扩展

```shell
echo ~ 当前用户的主目录
/root
```

### 7.1.3 算术扩展

```shell
算术扩展格式
$((expression))
# echo $((2+2))
4
```

| 算术运算符 |                                        |
| ---------- | -------------------------------------- |
| +          |                                        |
| -          |                                        |
| *          |                                        |
| /          | 除（只支持整数运算，所以结果也是整数） |
| %          | 取余                                   |
| **         | 取幂                                   |

### 7.1.4 花括号扩展

花括号表达式本身可以包含一系列逗号分隔的字符串或者一系列整数，单个字符

```shell
#echo Food-{1,2,3}-Name
Food-1-Name Food-2-Name Food-3-Name
# echo Number_{1..4}
Number_1 Number_2 Number_3 Number_4
# echo {a..z}
a b c d e f g h i j k l m n o p q r s t u v w x y z
支持嵌套
#echo a{A{1,2},B{3,4}}b
aA1b aA2b aB3b aB4b
mkdir {2009..2011}-0{1..9}
批量创建文件夹，从2009-01到2011-09
```

### 7.1.5 参数扩展

```shell
# echo $USER
root
# printenv | less
查看key的变量列表
```

### 7.1.6 命令替换

命令替换可以把一个命令的输出作为一个扩展模式使用

```shell
echo $(ls)
```

## 7.2 引用

```shell
echo $100.00
00.00
因为$1是未定义的变量，所以参数扩展将把$1的值替换成空字符串
```

7.2.1 双引号