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

命令后面跟有一个或多个选项，命令后面也会跟一个或多个参数