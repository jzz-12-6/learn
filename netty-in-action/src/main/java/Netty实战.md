# netty 实战

```pom
 <dependency>
       <groupId>io.netty</groupId>
       <artifactId>netty-all</artifactId>
       <version>4.1.38.Final</version>
</dependency>
```

# 第1章 Netty——异步和事件驱动

netty设计架构方法和设计原则：

* 关注分离点——业务和网络逻辑解耦
* 模块化和可复用性
* 可测试性

## 1.1 Java网络编程

```java
/**
* 阻塞IO试例
*/
public class TestBlockingIO {
    public static void main(String[] args) throws Exception{
        int port = 5;
        //创建一个新的ServerSocket用以监听指定端口上的连接请求
        ServerSocket serverSocket = new ServerSocket(port);
        //accpet()方法的调用将被阻塞，知道一个连接的建立
        Socket clientSocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
        String request,response;
        while ((request = in.readLine())!=null){
            if("Done".equalsIgnoreCase(request)){
                break;
            }
            //请求传递给服务器的处理方法
            response = prcessRequest(request);
            //服务器的响应被发送给客户端
            out.print(response);
        }
    }
}
```

该代码只能同时处理一个连接，要管理多个并发客户端，需要为每个新的客户端Socket创建一个新的Thread；

![1.1blockingIO](C:\Users\T440\Desktop\netty\nettyimg\1.1blockingIO.png)



缺点：

* 任何时刻都可能有大量的线程处于休眠状态，只是等待输入或者输出数据就绪，
* 需要为每个线程分配内存
* 线程上下文切换的开销非常大

### 1.1.1 Java NIO

NIO：new input|outout 或者Non-blocking I/O（非阻塞IO）;

java 提供了非阻塞调用

* setsockopt()方法配置套接字，用于读写没有数据的时候立即返回。

### 1.1.2 选择器

java .nio.channels.Selector 是Java的非阻塞I/O实现的关键，使用了事件通知API以确定在一组非阻塞套接字中有哪些已经就绪能够进行I/O相关操作

![1.1.2 NIO](C:\Users\T440\Desktop\netty\nettyimg\1.1.2 NIO.png)

优势：

1. 使用较少的线程便可以处理多连接，减少内存和上下文切换所带来开销
2. 没有IO需要进行处理的时候，线程也可以被用于其他任务

## 1.2 Netty简介

面向对象的基本概念：用较简单的抽象隐藏底层实现复杂性

netty特点

| 分类     | Netty特性                                            |
| -------- | ---------------------------------------------------- |
| 设计     | 统一的API，支持多种传输类型，阻塞和非阻塞的          |
|          | 简单而强大的线程模型                                 |
|          | 真正的无连接数据报套接字支持                         |
|          | 连接逻辑组件支持复用                                 |
| 易于使用 | 详实的Javadoc和大量的示例                            |
|          | jdk1.6以上支持，部分需要1.8                          |
| 性能     | 拥有比java核心API更高的吞吐量以及更低的延迟          |
|          | 得益于池化和复用，拥有更低的资源消耗                 |
|          | 最少的内存复制                                       |
| 健壮性   | 不会因为慢速、快速或者超载的连接导出OutOfMemoryError |
|          | 消除在高速网络中NIO应用程序常见的不公平读写比率      |
| 安全性   | 完整的SSL/TLS以及StartTLS支持                        |
|          | 可用于受限环境下，如Applet和OSGI                     |
| 社区驱动 | 发布快速且频繁                                       |

### 1.2.2 异步和事件驱动

异步：和同步相对，异步处理不用阻塞当前[线程](https://baike.baidu.com/item/%E7%BA%BF%E7%A8%8B/103101)来等待处理完成，而是允许后续操作，直至其它线程将处理完成，并回调通知此线程。

事件驱动：一个事件的发生触发一次程序

## 1.3 Netty的核心组件

1. Channel
2. 回调
3. Future
4. 事件和ChannelHandler



### 1.3.1 Channel

Channel是Java NIO的一个基本构造，代表一个到实体的开放连接，如读操作和写操作。目前可以把Channel看作是传入或者传出数据的载体，因此可以被打开或者关闭，连接或者断开连接。

### 1.3.2 回调

一个回调其实就是一个方法，一个指向已经被提供给另外一个方法的方法的引用。这使得后者可以在适当的时候调用前者。

Netty在内部使用了回调来处理事件；当一个回调被处罚时，相关的事件可以被一个interface-ChannelHandler的实现处理。

代码实例:

```java
public class ConnectHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client " +ctx.channel().remoteAddress() + " connected");
       // super.channelActive(ctx);
    }
    //当一个新的连接已经被建立时，ChannelHandler的channelActive()回调方法将会被调用
}
```

### 1.3.3 Future

Future 提供了另一种在操作完成时通知应用程序的方式。这个对象可以看作是一个异步操作的结果的占位符；它将在未来的某个时刻完成，并提供对其结果的访问。

JDK内置java.util.concurrent.Future,但是其所提供的实现，只允许手动检查对应的操作是否已经完成，或者一直阻塞到它完成。

Netty提供的ChannelFuture提供了额外的方法，可以注册一个或者多个ChannelFutureListener实例。监听器的回调方法operationComplete()，将会在对应的操作完成时被调用。

每个Netty的出战IO都将返回一个ChannelFuture，也就是都不会阻塞，Netty完全是异步和事件驱动的

```java
  Channel channel = null;
        //异步地连接到远程节点
        ChannelFuture channelFuture = channel.connect(new InetSocketAddress("127.0.0.1", 25));
        //注册一个ChannelFutureListener以便在操作完成时获得通知
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()){
                    ByteBuf buf = Unpooled.copiedBuffer("Hello", Charset.defaultCharset());
                    //将数据异步地发送到远程节点，返回一个ChannelFuture
                    ChannelFuture channelFuture1 = channelFuture.channel().writeAndFlush(buf);
                }else {
                    Throwable cause = channelFuture.cause();
                    cause.printStackTrace();
                }
            }
        });
```

### 1.3.4 事件和ChannelHandler

Netty使用不同的事件来通知我们状态的改变或者是操作的状态；

netty是一个网络编程框架，所以事件是按照它们入站或者出战数据流的相关性进行分类

入站：

* 连接已被激活或者连接失活
* 数据读取
* 用户事件
* 错误事件

出战：

* 数据读取
* 用户事件
* 错误事件
* 打开或者关闭到远程节点的连接
* 将数据写到或者冲刷到套接字

每个事件都可以被分开给ChannelHandler类的某个用户实现的方法。Netty的channelHandler为处理器提供了基本的抽象，以及大量预定义的ChannelHandler实现。

Netty通过触发事件将Selector从应用程序中抽象处理，在内部，将会为每个Channel分配一个EventLoop，用以处理所以事件，EventLoop本身只有一个线程驱动，处理了一个Channel的所有IO事件，且整个生命周期都不会改变。实现了同步。



# 第2章 你的第一款Netty应用程序

## 2.3 编写Echo服务器

所有Netty服务器都需要以下两部分

至少一个ChannelHandler——实现了服务器对从客户端接收的数据处理（业务逻辑）

引导——配置服务器的启动代码

### 2.3.1 ChannelHandler和业务逻辑

ChannelHandler是一个接口族的父接口，它的实现负责接收并相应事件通知。提供了 ChannelInboundHandler 的默认实现

```java
//标示一个Channel-Handler 可以被多个Channel安全地共享
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 对于每个传入的消息都要调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("Server received: " +byteBuf.toString(CharsetUtil.UTF_8));
        //将接受到的消息写给发送者，而不冲刷出战消息
        ctx.write(byteBuf);
        super.channelRead(ctx, msg);
    }

    /**
     * 通知ChannelInboundHandler最后一次对ChannelRead()调用是当前批量读取中的最后一条消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将未决消息冲刷到远程节点，并且关闭该Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        super.channelReadComplete(ctx);
    }

    /**
     * 异常处理，关闭流
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}

```

必须捕获异常，每一个Channel都拥有一个关联的 ChannelPipeline，其持有一个ChannelHandler的实例链。默认情况下ChannelHandler会把对它的方法调用转发给链中的下一个ChannelHandler。如果未捕获异常，所接收的异常将会被传递到ChannelPipeline的尾端并被记录

### 2.3.2 引导服务器

作用：

* 绑定到服务器将在其上监听并接受传入连接请求的端口
* 配置Channel，以将有关的入站消息通知给EchoServerHandler实例

传输：在网络协议的标准多层试图中，传输层提供了端到端或者主机到主机的通信服务

因特网通信是建立在TCP传输上的，NIO传输大多数指的TCP传输

```java
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args)
        throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() +
                " <port>"
            );
            return;
        }
        //设置端口值（如果端口参数的格式不正确，则抛出一个NumberFormatException）
        int port = Integer.parseInt(args[0]);
        //调用服务器的 start()方法
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        //(1) 创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //(2) 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                //(3) 指定所使用的 NIO 传输 Channel
                .channel(NioServerSocketChannel.class)
                //(4) 使用指定的端口设置套接字地址
                .localAddress(new InetSocketAddress(port))
                //(5) 添加一个EchoServerHandler到于Channel的 ChannelPipeline
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        //EchoServerHandler 被标注为@Shareable，所以我们可以总是使用同样的实例
                        //这里对于所有的客户端连接来说，都会使用同一个 EchoServerHandler，因为其被标注为@Sharable，
                        //这将在后面的章节中讲到。
                        ch.pipeline().addLast(serverHandler);
                    }
                });
            //(6) 异步地绑定服务器；调用 sync()方法阻塞等待直到绑定完成
            ChannelFuture f = b.bind().sync();
            System.out.println(EchoServer.class.getName() +
                " started and listening for connections on " + f.channel().localAddress());
            //(7) 获取 Channel 的CloseFuture，并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } finally {
            //(8) 关闭 EventLoopGroup，释放所有的资源
            group.shutdownGracefully().sync();
        }
    }
}
```



引导过程所需步骤

1. 创建一个ServerBootstrap的实例以引导和绑定服务器
2. 创建并分配一个NioEventLoopGroup实例以进行事件的处理，如接受新连接以及读写数据
3. 指定服务器绑定的本地的InetSocketAddress
4. 使用一个EchoServerHandler的实例初始化每一个新的Channel
5. 调用ServerBootstrap.bind()方法以绑定服务器

## 2.4 编写Echo客户端

 Echo客户端将会

1. 连接到服务器
2. 发送一个或者多个消息
3. 对于每个消息，等待并接收从服务器发回的相同的消息
4. 关闭连接

### 2.4.1 通过ChannelHandler实现客户端逻辑

```java
@Sharable
//标记该类的实例可以被多个 Channel 共享
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //当被通知 Channel是活跃的时候，发送一条消息
        //将在一个连接建立时被调用
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",
                CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        //记录已接收消息的转储
        //每当接收数据时，都会调用这个方法，服务器发送的消息可能分块接收
        System.out.println(
                "Client received: " + msg.toString(CharsetUtil.UTF_8));
    }

    @Override
    //在发生异常时，记录错误并关闭Channel
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
```

### 2.4.2 引导客户端

```java
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建 Bootstrap
            Bootstrap b = new Bootstrap();
            //指定 EventLoopGroup 以处理客户端事件；需要适用于 NIO 的实现
            b.group(group)
                //适用于 NIO 传输的Channel 类型
                .channel(NioSocketChannel.class)
                //设置服务器的InetSocketAddress
                .remoteAddress(new InetSocketAddress(host, port))
                //在创建Channel时，向 ChannelPipeline中添加一个 EchoClientHandler实例
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                        throws Exception {
                        ch.pipeline().addLast(
                             new EchoClientHandler());
                    }
                });
            //连接到远程节点，阻塞等待直到连接完成
            ChannelFuture f = b.connect().sync();
            //阻塞，直到Channel 关闭
            f.channel().closeFuture().sync();
        } finally {
            //关闭线程池并且释放所有的资源
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args)
            throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: " + EchoClient.class.getSimpleName() +
                    " <host> <port>"
            );
            return;
        }

        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        new EchoClient(host, port).start();
    }
}
```

引导客户端步骤

1. 为初始化客户端，创建一个Bootstrap实例
2. 为进行事件处理分配一个NioEventLoopGroup实例，其中事件处理包括创建新的连接以及处理入站和 出站数据
3. 为服务器连接创建了一个InetSocketAddress实例
4. 当连接被建立时，一个EchoClientHandler实例会被安装到ChannelPipeline中
5. 当一切设置都完成后，调用Bootstrap.connect()方法连接到远程节点

# 第 3 章 Netty的组件和设计

Netty 解决了两个相应的关注领域，我们可将其大致标记为技术的和体系结构的。首先，它的基于 Java NIO 的异步的和事件驱动的实现，保证了高负载下应用程序性能的最大化和可伸缩性。其次，Netty 也包含了一组设计模式，将应用程序逻辑从网络层解耦，简化了开发过程，同时也最大限度地提高了可测试性、模块化以及代码的可重用性。

## 3.1 Channel、EventLoop 和 ChannelFuture

### 3.1.1 Channel 接口

基本的 I/O 操作（bind()、connect()、read()和 write()）依赖于底层网络传输所提供的原语。Netty 的 Channel 接口所提供的 API，大大地降低了直接使用 Socket 类的复杂性。

### 3.1.2 EventLoop接口

​	EventLoop定义了Netty的核心对象，用于处理连接的生命周期中所发生的事件。

 	1. 一个EventLoopGroup包含一个或者多个EventLoop	
 	2. 一个EventLoop在它的生命周期内只和一个Thread绑定
 	3. 所有由EventLoop处理的I/O事件都将在它专有的Thread上被处理
 	4. 一个Channel在它的生命周期内只注册于一个EventLoop
 	5. 一个EventLoop可能被分配给一个或多个Channel

一个给定Channel的I/O操作都是由相同的Thread执行的，实际上消除了同步的需要

### 3.1.3 ChannelFuture接口

ChannelFuture中的addListener()方法注册了一个ChannelFutureListener，以便在某个操作完成时得到通知

## 3.2 ChannelHandler 和 ChannelPipeline

### 3.2.1 ChannelHandler 接口

ChannelHandler 充当了所有处理入站和出站数据的应用程序逻辑的容器。ChannelHandler 接收入站事件和数据，这些数据随后将会被你的应用程序的业务逻辑所处理。

### 3.2.2 ChannelPipeline 接口

 	ChannelPipeline 提供了ChannelHandler 链的容器，并定义了用于在该链上传播入站和出站事件流的API。当Channel被创建时，它会自动分配到它专属的ChannelPipeline 。

​	过程：

1. 一个ChannelInitializer的实现被注册到了ServerBootstrap中
2. 当ChannelInitializer.initChannel()方法被调用时，ChannelInitializer将在 ChannelPipeline 中安装一组自定义的 ChannelHandler；
3. ChannelInitializer 将它自己从 ChannelPipeline 中移除

ChannelHandler是处理ChannelPipeline 事件的任何代码的通用容器，这些对象接收事件、执行它们所实现的处理逻辑，并将数据传递给链中的下一个ChannelHandler。

当ChannelHandler呗添加到ChannelPipeline 时，它将被分配一个ChannelHandlerContext，其代表了ChannelHandler和ChannelPipeline 之间的绑定。

ChannelPipeline中的每个ChannelHandler将负责把事件转发到链中的下一个 ChannelHandler。这些适配器类（及它们的子类）将自动执行这个操作

### 3.2.4 编码器和解码器

 将网络消息解码成JAVA对象，或者将JAVA对象编码成网络消息。这些基类的名称将类似于 ByteToMessageDecoder 或 MessageToByteEncoder。

### 3.2.5 抽象类 SimpleChannelInboundHandler

扩展基类 SimpleChannelInboundHandler<T>来接收解码消息。

# 第4章 传输

## 4.2 传输API

```
interface Channel extends AttributeMap, Comparable<Channel>
interface ServerChannel extends Channel
interface ChannelPipeline extends Iterable<Entry<String, ChannelHandler>>
interface SocketChannel extends Channel
```

传输的核心是Interface Channel,用于所有的I/O操作。

每个 Channel 都将会被分配一个 ChannelPipeline 和 ChannelConfig。ChannelConfig 包含了该 Channel 的所有配置设置，并且支持热更新。Channel是独一无二的，如果两个不同的Channel实例返回相同的散列码，那么AbstractChannel中的compareTo()方法的实现将会抛出一个 Error

ChannelHandler的用途：

1. 将数据从一种格式转换为另一种格式；
2. 提供异常的通知
3. 提供 Channel 变为活动的或者非活动的通知
4. 提供当 Channel 注册到 EventLoop 或者从 EventLoop 注销时的通知；
5. 提供有关用户自定义事件的通知

Channel方法

| 方法名                | 描述                                                         |
| --------------------- | ------------------------------------------------------------ |
| id                    | 返回全局唯一标识符                                           |
| eventLoop             | 返回分配给 Channel 的 EventLoop                              |
| parent                | 返回上一层Channel                                            |
| config                | 返回配置                                                     |
| isOpen                | 是否打开状态                                                 |
| isRegistered          | 是否注册到EventLoop                                          |
| isActive              | 是否激活状态                                                 |
| metadata              | 返回分配给 Channel 的 ChannelMetadata                        |
| localAddress          | 返回本地的 SokcetAddress                                     |
| remoteAddress         | 返回远程的 SocketAddress                                     |
| closeFuture           | 返回Channel关闭时，收到通知的ChannelFuture，永远返回相同的实例 |
| isWritable            | 是否可写，当I/O线程立即可写的时候返回true                    |
| unsafe                | 返回Unsafe实例                                               |
| pipeline              | 返回分配给 Channel 的 ChannelPipeline                        |
| alloc                 | 返回指定的ByteBufAllocator用于分配ByteBuf                    |
| newPromise            | 返回新的ChannelPromise                                       |
| newProgressivePromise | 返回新的ChannelProgressivePromise                            |
| newSucceededFuture    | 返回新的ChannelFuture，已经被标记成功                        |
| newFailedFuture       | 标记失败返回新的ChannelFuture                                |
| write                 | 将数据写到远程节点。这个数据将被传递给 ChannelPipeline，并且排队直到它被冲刷 |
| flush                 | 将之前已写的数据冲刷到底层传输，如一个 Socket                |
| writeAndFlush         | 一个简便的方法，等同于调用 write()并接着调用 flush()         |

写数据并将其冲刷到远程节点这样的常规任务。

```java
 private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
public static void writingToChannel() {
        //创建Channel实例
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        //创建持有要写数据的 ByteBuf
        ByteBuf buf = Unpooled.copiedBuffer("your data", CharsetUtil.UTF_8);
        ChannelFuture cf = channel.writeAndFlush(buf);
        //添加 ChannelFutureListener 以便在写操作完成后接收通知
        cf.addListener((ChannelFuture future)->{
            //写操作完成，并且没有错误发生
            if (future.isSuccess()) {
                System.out.println("Write successful");
            } else {
                //记录错误
                System.err.println("Write error");
                future.cause().printStackTrace();
            }
        });
    }
```

Netty的Channel 实现是线程安全的因此你可以存储一个到 Channel 的引用，并且每当你需要向远程节点写数据时，都可以使用它，消息将会被保证按顺序发送。

```java
public static void writingToChannelFromManyThreads() {
        final Channel channel = CHANNEL_FROM_SOMEWHERE; 
        //创建持有要写数据的ByteBuf
        final ByteBuf buf = Unpooled.copiedBuffer("your data",
                CharsetUtil.UTF_8);
        //创建将数据写到Channel 的 Runnable
        Runnable writer = ()-> channel.write(buf.duplicate());
          
        //获取到线程池Executor 的引用
        Executor executor = Executors.newCachedThreadPool();

        //递交写任务给线程池以便在某个线程中执行
        // write in one thread
        executor.execute(writer);

        //递交另一个写任务以便在另一个线程中执行
        // write in another thread
        executor.execute(writer);
        //...
    }
```

## 4.3 内置的传输

netty所提供的传输

| 名称     | 包                          | 描述                                                         |
| -------- | --------------------------- | ------------------------------------------------------------ |
| NIO      | io.netty.channel.socket.nio | 使用 java.nio.channels 包作为基础——基于选择器的方式          |
| Epoll    | io.netty.channel.epoll      | 由 JNI 驱动的 epoll()和非阻塞 IO。这个传输支持
只有在Linux上可用的多种特性，如SO_REUSEPORT，比 NIO 传输更快，而且是完全非阻塞的 |
| OIO      | io.netty.channel.socket.oio | 使用 java.net 包作为基础——使用阻塞流                         |
| Local    | io.netty.channel.local      | 可以在 VM 内部通过管道进行通信的本地传输                     |
| Embedded | io.netty.channel.embedded   | Embedded 传输，允许使用 ChannelHandler 而又不需要一个真正的基于网络的传输。这在测试你的ChannelHandler 实现时非常有用 |

### 4.3.1 NIO(非阻塞I/O)

​	NIO提供了一个所有IO操作的全异步实现，利用基于选择器的API，选择器相当于一个注册表，可以请求在Channel的状态发送变化时得到通知，选择器运行在一个检测状态变化并对其做出相应响应的线程上，在应用程序对状态的改变做出响应之后，选择器将会被重置，并重复这个过程

选择器操作的位模式

| 名称       | 描述                                                         |
| ---------- | ------------------------------------------------------------ |
| OP_ACCEPT  | 请求在接受新连接并创建 Channel 时获得通知                    |
| OP_CONNECT | 请求在建立一个连接时获得通知                                 |
| OP_READ    | 请求当数据已经就绪，可以从 Channel 中读取时获得通知          |
| OP_WRITE   | 请求当可以向 Channel 中写更多的数据时获得通知。这处理了套接字缓冲区被完全填满时的情况，这种情况通常发生在数据的发送速度比远程节点可处理的速度更快的时候 |

### 4.3.2 Epoll—用于 Linux 的本地非阻塞传输

Netty为Linux提供了一组NIO API，其以一种和它本身的设计更加一致的方式使用epoll，并且以一种更加轻量的方式使用中断。

### 4.3.3 OIO—旧的阻塞 I/O

 	Netty利用了SO_TIMEOUT这个Socket标志，它指定了等待一个I/O操作完成的最大毫秒数。如果操作在指定的时间间隔内没有完成，则将会抛出一个SocketTimeout Exception。Netty将捕获这个异常并继续处理循环。在EventLoop下一次运行时，它将再次尝试。这实际上也是类似于Netty这样的异步框架能够支持OIO的唯一方式（问题：当一个 SocketTimeoutException 被抛出时填充栈跟踪所需要的时间，其对于性能来说代价很大）

### 4.3.4 用于 JVM 内部通信的 Local 传输

​	Netty 提供了一个 Local 传输，用于在同一个 JVM 中运行的客户端和服务器程序之间的异步通信。同样，这个传输也支持对于所有 Netty 传输实现都共同的 API。在这个传输中，和服务器 Channel 相关联的 SocketAddress 并没有绑定物理网络地址；相反，只要服务器还在运行，它就会被存储在注册表里，并在 Channel 关闭时注销。因为这个传输并不接受真正的网络流量，所以它并不能够和其他传输实现进行互操作。客户端希望连接到（在同一个 JVM 中）使用了这个传输的服务器端时也必须使用它。

### 4.3.5 Embedded 传输

​	Embedded 传输的关键是一个被称为 EmbeddedChannel 的具体的 Channel实现。使得你可以将一组 ChannelHandler 作为帮助器类嵌入到其他的 ChannelHandler 内部。通过这种方式，你将可以扩展一个 ChannelHandler 的功能，而又不需要修改其内部代码。

## 第 5 章 ByteBuf

​	网络数据的基本单位总是字节。Java NIO 提供了 ByteBuffer 作为它的字节容器，Netty提供了ByteBuf

## 5.1 ByteBuf 的 API

​	Netty 的数据处理 API 通过两个组件暴露——abstract class ByteBuf 和 interface ByteBufHolder。

​	优点：

 	1. 可以被用户自定义的缓冲区类型扩展
 	2. 通过内置的复合缓冲区类型实现了透明的零拷贝
 	3. 容量可以按需增长（类似StringBuilder）
 	4. 在读和写这两种模式之间切换不需要调用ByteBuffer 的 flip()方法
 	5. 读和写使用了不同的索引；
 	6. 支持方法的链式调用；
 	7. 支持引用计数
 	8. 支持池化。

## 5.2 ByteBuf 类——Netty 的数据容器

​	ByteBuf 维护了两个不同的索引：一个用于读取，一个用于写入，读取时readerIndex将会被递增已经被读取的字节数，写入时，writerIndex 也会被递增。如果readerIndex=writerIndex，抛出异常IndexOutOfBoundsException。名称以read 或者 write 开头的 ByteBuf 方法，将会推进其对应的索引，以 set 或者 get 开头的操作则不会

### 5.2.2 ByteBuf 的使用模式

​	1.堆缓冲区

​	最常用的 ByteBuf 模式是将数据存储在 JVM 的堆空间中,称为支撑数组，能在没有使用池化的情况下提供快速的分配和释放

```java
 public static void heapBuffer() {
        ByteBuf heapBuf = Unpooled.buffer(1024); 
        //检查 ByteBuf 是否有一个支撑数组
     	//如果为false访问数组，抛出异常UnsupportedOperationException。
        if (heapBuf.hasArray()) {
            //如果有，则获取对该数组的引用
            byte[] array = heapBuf.array();
            //计算第一个字节的偏移量
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            //获得可读字节数
            int length = heapBuf.readableBytes();
            //使用数组、偏移量和长度作为参数调用你的方法
            handleArray(array, offset, length);
        }
    }
```

​	2.直接缓冲区

​	ByteBuffer 类允许 JVM 实现通过本地调用来分配内存。这主要是为了避免在每次调用本地 I/O 操作之前（或者之后）将缓冲区的内容复制到一个中间缓冲区（或者从中间缓冲区把内容复制到缓冲区）直接缓冲区的内容将驻留在常规的会被垃圾回收的堆之外。缺点是相对于基于堆的缓冲区，它们的分配和释放都较为昂贵。

```java
 private static final ByteBuf BYTE_BUF_FROM_SOMEWHERE = Unpooled.buffer(1024);
public static void directBuffer() {
        ByteBuf directBuf = BYTE_BUF_FROM_SOMEWHERE;
        //检查 ByteBuf 是否由数组支撑。如果不是，则这是一个直接缓冲区
        if (!directBuf.hasArray()) {
            //获取可读字节数
            int length = directBuf.readableBytes();
            //分配一个新的数组来保存具有该长度的字节数据
            byte[] array = new byte[length];
            //将字节复制到该数组
            directBuf.getBytes(directBuf.readerIndex(), array);
            //使用数组、偏移量和长度作为参数调用你的方法
            handleArray(array, 0, length);
        }
}

```

​	3 复合缓冲区

​	Netty 通过一个 ByteBuf 子类——CompositeByteBuf——实现为多个 ByteBuf 提供一个聚合视图。在这里你可以根据需要添加或者删除 ByteBuf 实例。

```java
/**
     * 头部和主体——组成的将通过 HTTP 协议
     * 传输的消息。这两部分由应用程序的不同模块产生，将会在消息被发送的时候组装。该应用程序
     * 可以选择为多个消息重用相同的消息主体。当这种情况发生时，对于每个消息都将会创建一个新
     * 的头部
     * 创建了一个包含两个 ByteBuffer 的数组用来保存这些消息组件，同时创建了第三个 ByteBuffer 用来保存所
     * 有这些数据的副本
     * @param header
     * @param body
     */
    public static void byteBufferComposite(ByteBuffer header, ByteBuffer body) {
        // Use an array to hold the message parts
        ByteBuffer[] message =  new ByteBuffer[]{ header, body };

        // Create a new ByteBuffer and use copy to merge the header and body
        ByteBuffer message2 =
                ByteBuffer.allocate(header.remaining() + body.remaining());
        message2.put(header);
        message2.put(body);
        message2.flip();
    }
```

使用了 CompositeByteBuf

```java
 /**
     * 不想为每个消息都重新分配这两个缓冲区，所以使用 CompositeByteBuf 是一个完美的选择。
     */
    public static void byteBufComposite() {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = BYTE_BUF_FROM_SOMEWHERE;
        ByteBuf bodyBuf = BYTE_BUF_FROM_SOMEWHERE;
        //将 ByteBuf 实例追加到 CompositeByteBuf
        messageBuf.addComponents(headerBuf, bodyBuf);
        //...
        //删除位于索引位置为 0（第一个组件）的 ByteBuf
        messageBuf.removeComponent(0); // remove the header
        //循环遍历所有的 ByteBuf 实例
        Iterator<ByteBuf> iterator = messageBuf.iterator();
        while (iterator.hasNext()){
            ByteBuf byteBuf = iterator.next();
            System.out.println(byteBuf.toString());
        }
        //CompositeByteBuf 可能不支持访问其支撑数组，因此访问 CompositeByteBuf 中的数据类似于（访问）直接缓冲区的模式
        //获得可读字节数
        int length = messageBuf.readableBytes();
        //分配一个具有可读字节数长度的新数组
        byte[] array = new byte[length];
        //将字节读到该数组中
        messageBuf.getBytes(messageBuf.readerIndex(), array);
        //使用偏移量和长度作为参数使用该数组
        handleArray(array, 0, array.length);
    }


```

## 5.3 字节级操作

### 5.3.1 随机访问索引

```java
 /**
     * ByteBuf 的索引是从零开始的：
     * 第一个字节的索引是0，最后一个字节的索引总是 capacity() - 1
     */
    public static void byteBufRelativeAccess() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        //不会改变索引位置
        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.getByte(i);
            System.out.println((char) b);
        }
        //可以改变索引位置
        buffer.readerIndex(2);
        buffer.writerIndex(1);
    }


```

### 5.3.2 顺序访问索引

​	虽然 ByteBuf 同时具有读索引和写索引，但是 JDK 的 ByteBuffer 却只有一个索引，这也就是为什么必须调用 flip()方法来在读模式和写模式之间进行切换的原因。

### 5.3.3 可丢弃字节

​	可丢弃字节的分段包含了已经被读过的字节，调用discardReadBytes()方法，可以丢弃它们并回收空间。这个分段的初始大小为 0，存储在 readerIndex 中，会随着 read 操作的执行而增加。这将极有可能会导致内存复制，因为可读字节（图中标记为 CONTENT 的部分）必须被移动到缓冲区的开始位置。

### 5.3.4 可读字节

​	ByteBuf 的可读字节分段存储了实际数据。新分配的、包装的或者复制的缓冲区的默认的readerIndex 值为 0。任何名称以 read 或者 skip 开头的操作都将检索或者跳过位于当前readerIndex 的数据，并且将它增加已读字节数。果被调用的方法需要一个 ByteBuf 参数作为写入的目标，并且没有指定目标索引参数，那么该目标缓冲区的 writerIndex 也将被增加

```java
public static void readAllData() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        while (buffer.isReadable()) {
            System.out.println(buffer.readByte());
        }
}
```

### 5.3.5 可写字节

​	可写字节分段是指一个拥有未定义内容的、写入就绪的内存区域。新分配的缓冲区的writerIndex 的默认值为 0。任何名称以 write 开头的操作都将从当前的 writerIndex 处开始写数据，并将它增加已经写入的字节数。如果写操作的目标也是 ByteBuf，并且没有指定源索引的值，则源缓冲区的 readerIndex 也同样会被增加相同的大小。

```java
    public static void write() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; 
        while (buffer.writableBytes() >= 4) {
            buffer.writeInt(random.nextInt());
        }
    }

```

### 5.3.6 索引管理

​	JDK 的 InputStream 定义了 mark(int readlimit)和 reset()方法，这些方法分别被用来将流中的当前位置标记为指定的值，以及将流重置到该位置

​	可以通过调用 markReaderIndex()、markWriterIndex()、resetWriterIndex()和 resetReaderIndex()来标记和重置 ByteBuf 的 readerIndex 和 writerIndex。可以通过调用 clear()方法来将 readerIndex 和 writerIndex 都设置为0

### 5.3.7 查找操作

 ```java
public static void byteProcessor() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        //查找空格
        buffer.indexOf(0,buffer.readerIndex(),Byte.decode("/r"));
        int index = buffer.forEachByte(ByteBufProcessor.FIND_CR);
}
 ```

### 5.3.8 派生缓冲区

​	派生缓冲区为 ByteBuf 提供了以专门的方式来呈现其内容的视图。这类视图是通过以下方法被创建的：duplicate()，slice()，slice(int, int)，Unpooled.unmodifiableBuffer(…)，order(ByteOrder)，readSlice(int)每个这些方法都将返回一个新的 ByteBuf 实例，它具有自己的读索引、写索引和标记索引。如果修改了内容，也会同时修改对应的源实例。

​	使用copy()或者 copy(int, int)方法，会拥有独立的数据副本。

```java
	/**
     * 代码清单 5-10 对 ByteBuf 进行切片
     */
    public static void byteBufSlice() {
        Charset utf8 = Charset.forName("UTF-8");
        //创建一个用于保存给定字符串的字节的 ByteBuf
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        //创建该 ByteBuf 从索引 0 开始到索引 15 结束的一个新切片
        ByteBuf sliced = buf.slice(0, 15);
        //将打印“Netty in Action”
        System.out.println(sliced.toString(utf8));
        //更新索引 0 处的字节
        buf.setByte(0, (byte)'J');
        //将会成功，因为数据是共享的，对其中一个所做的更改对另外一个也是可见的
        assert buf.getByte(0) == sliced.getByte(0);
    }

    /**
     * 代码清单 5-11 复制一个 ByteBuf
     */
    public static void byteBufCopy() {
        Charset utf8 = Charset.forName("UTF-8");
        //创建 ByteBuf 以保存所提供的字符串的字节
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        //创建该 ByteBuf 从索引 0 开始到索引 15 结束的分段的副本
        ByteBuf copy = buf.copy(0, 15);
        //将打印“Netty in Action”
        System.out.println(copy.toString(utf8));
        //更新索引 0 处的字节
        buf.setByte(0, (byte)'J');
        //将会成功，因为数据不是共享的
        assert buf.getByte(0) != copy.getByte(0);
    }
```

### 5.3.9 读/写操作

​	get()和set()操作，从给定索引开始，并且保持索引不变

​	read()和wtite()操作，从给定的索引开始，并且会根据已经访问过的字节数对索引进行调整

```java
 public static void byteBufSetGet() {
        Charset utf8 = Charset.forName("UTF-8");
        //创建一个新的 ByteBuf以保存给定字符串的字节
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        //打印第一个字符'N'
        System.out.println((char)buf.getByte(0));
        //存储当前的 readerIndex 和 writerIndex
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        //将索引 0 处的字 节更新为字符'B'
        buf.setByte(0, (byte)'B');
        //打印第一个字符，现在是'B'
        System.out.println((char)buf.getByte(0));
        //将会成功，因为这些操作并不会修改相应的索引
        assert readerIndex == buf.readerIndex();
        assert writerIndex == buf.writerIndex();
    }
 /**
     * 代码清单 5-13 ByteBuf 上的 read()和 write()操作
     */
    public static void byteBufWriteRead() {
        Charset utf8 = Charset.forName("UTF-8");
        //创建一个新的 ByteBuf 以保存给定字符串的字节
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        //打印第一个字符'N'
        System.out.println((char)buf.readByte());
        //存储当前的readerIndex
        int readerIndex = buf.readerIndex();
        //存储当前的writerIndex
        int writerIndex = buf.writerIndex();
        //将字符 '?'追加到缓冲区
        buf.writeByte((byte)'?');
        assert readerIndex == buf.readerIndex();
        //将会成功，因为 writeByte()方法移动了 writerIndex
        assert writerIndex != buf.writerIndex();
    }
```

### 5.3.10 更多操作

| 名称            | 描述                                                         |
| --------------- | ------------------------------------------------------------ |
| isReadable()    | 如果至少有一个字节可供读取，则返回 true                      |
| isWritable()    | 如果至少有一个字节可被写入，则返回 true                      |
| readableBytes() | 返回可被读取的字节数                                         |
| writableBytes() | 返回可被写入的字节数                                         |
| capacity()      | 返回 ByteBuf 可容纳的字节数。在此之后，它会尝试再次扩展直到达到 maxCapacity() |
| maxCapacity()   | 返回 ByteBuf 可以容纳的最大字节数                            |
| hasArray()      | 如果 ByteBuf 由一个字节数组支撑，则返回 true                 |
| array()         | 如果 ByteBuf 由一个字节数组支撑则返回该数组；否则，它将抛出一个UnsupportedOperationException 异常 |

## 5.5 ByteBuf 分配

### 5.5.1 按需分配：ByteBufAllocator 接口

​	为了降低分配和释放内存的开销，Netty 通过 interface ByteBufAllocator 实现了（ByteBuf 的）池化，它可以用来分配我们所描述过的任意类型的 ByteBuf 实例

| ByteBufAllocator方法                                         | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| buffer()
buffer(int initialCapacity);
buffer(int initialCapacity, int maxCapacity); | 返回一个基于堆或者直接内存存储的 ByteBuf                     |
| heapBuffer()
heapBuffer(int initialCapacity)
heapBuffer(int initialCapacity, int maxCapacity) | 返回一个基于堆内存存储的ByteBuf                              |
| directBuffer()
directBuffer(int initialCapacity)
directBuffer(int initialCapacity, int maxCapacity) | 返回一个基于直接内存存储的ByteBuf                            |
| compositeBuffer()
compositeBuffer(int maxNumComponents)
compositeDirectBuffer()
compositeDirectBuffer(int maxNumComponents);
compositeHeapBuffer()
compositeHeapBuffer(int maxNumComponents); | 返回一个可以通过添加最大到指定数目的基于堆的或者直接内存存储的缓冲区来扩展的CompositeByteBuf |
| ioBuffer()                                                   | 返回一个用于套接字的 I/O 操作的 ByteBuf                      |

获取ByteBufAllocator引用

```java
    /**
     * 代码清单 5-14 获取一个到 ByteBufAllocator 的引用
     */
    public static void obtainingByteBufAllocatorReference(){
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        //从 Channel 获取一个到ByteBufAllocator 的引用
        ByteBufAllocator allocator = channel.alloc();
        //...
        ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE;
        //从 ChannelHandlerContext 获取一个到 ByteBufAllocator 的引用
        ByteBufAllocator allocator2 = ctx.alloc();
        //...
    }
```

Netty提供了两种ByteBufAllocator的实现：PooledByteBufAllocator和UnpooledByteBufAllocator。前者池化了ByteBuf的实例以提高性能并最大限度地减少内存碎片。后者的实现不池化ByteBuf实例，并且在每次它被调用时都会返回一个新的实例。

### 5.5.2 Unpooled 缓冲区

| Unpooled 的方法                                              | 描述                                       |
| ------------------------------------------------------------ | ------------------------------------------ |
| buffer()
buffer(int initialCapacity)
buffer(int initialCapacity, int maxCapacity) | 返回一个未池化的基于堆内存存储的ByteBuf    |
| directBuffer()
directBuffer(int initialCapacity)
directBuffer(int initialCapacity, int maxCapacity) | 返回一个未池化的基于直接内存存储的 ByteBuf |
| wrappedBuffer()                                              | 返回一个包装了给定数据的 ByteBuf           |
| copiedBuffer()                                               | 返回一个复制了给定数据的 ByteBuf           |

### 5.5.3 ByteBufUtil类

ByteBufUtil 提供了用于操作 ByteBuf 的静态的辅助方法。

| 方法                     | 描述                                   |
| ------------------------ | -------------------------------------- |
| hexdump()                | 以十六进制的表示形式打印ByteBuf 的内容 |
| equals(ByteBuf, ByteBuf) | 判断两个 ByteBuf实例的相等性           |

## 5.6 引用计数

​	引用计数是一种通过在某个对象所持有的资源不再被其他对象引用时释放该对象所持有的资源来优化内存使用和性能的技术。ByteBuf 和 ByteBufHolder 引入了引用计数技术，它们都实现了 interface ReferenceCounted。

​	跟踪到某个特定对象的活动引用的数量。一个 ReferenceCounted 实现的实例将通常以活动的引用计数为 1 作为开始。只要引用计数大于 0，就能保证对象不会被释放。当活动引用的数量减少到 0 时，该实例就会被释放。试图访问一个已经被释放的引用计数的对象，将会抛出异常IllegalReferenceCountException

​	引用计数对于池化实现（如 PooledByteBufAllocator）来说是至关重要的，降低了内存分配的开销。

```java
    /**
     * 代码清单 5-15 引用计数
     * */
    public static void referenceCounting(){
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        //从 Channel 获取ByteBufAllocator
        ByteBufAllocator allocator = channel.alloc();
        //...
        //从 ByteBufAllocator分配一个 ByteBuf
        ByteBuf buffer = allocator.directBuffer();
        //检查引用计数是否为预期的 1
        assert buffer.refCnt() == 1;
        //...
    }

    /**
     * 代码清单 5-16 释放引用计数的对象
     */
    public static void releaseReferenceCountedObject(){
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        //减少到该对象的活动引用。当减少到 0 时，该对象被释放，并且该方法返回 true
        boolean released = buffer.release();
        //...
    }


```



# 第 6 章 ChannelHandler和ChannelPipeline

## 6.1 ChannelHandler 家族

### 6.1.1 Channel 的生命周期

ChannelRegistered--->ChannelActive--->ChannelInactive--->ChannelUnregistered

| Channel 的生命周期状态 | 描述                                                         |
| ---------------------- | ------------------------------------------------------------ |
| ChannelUnregistered    | Channel 已经被创建，但还未注册到 EventLoop                   |
| ChannelRegistered      | Channel 已经被注册到了 EventLoop                             |
| ChannelActive          | Channel 处于活动状态（已经连接到它的远程节点）。它现在可以接收和发送数据了 |
| ChannelInactive        | Channel 没有连接到远程节点                                   |

### 6.1.2 ChannelHandler 的生命周期

| ChannelHandler 的生命周期方法 | 描 述                                                 |
| ----------------------------- | ----------------------------------------------------- |
| handlerAdded                  | 当把 ChannelHandler 添加到 ChannelPipeline 中时被调用 |
| handlerRemoved                | 当从 ChannelPipeline 中移除ChannelHandler 时被调用    |
| exceptionCaught               | 当处理过程中在 ChannelPipeline 中有错误产生时被调用   |

### 6.1.3 ChannelInboundHandler 接口

| ChannelInboundHandler 的方法 | 描述                                                         |
| ---------------------------- | ------------------------------------------------------------ |
| channelRegistered            | 当 Channel 已经注册到它的 EventLoop 并且能够处理 I/O 时被调用 |
| channelUnregistered          | 当 Channel 从它的 EventLoop 注销并且无法处理任何 I/O 时被调用 |
| channelActive                | 当 Channel 处于活动状态时被调用；Channel 已经连接/绑定并且已经就绪 |
| channelInactive              | 当 Channel 离开活动状态并且不再连接它的远程节点时被调用      |
| channelReadComplete          | 当Channel上的一个读操作完成时被调用（会被多次调用）          |
| channelRead                  | 当从 Channel 读取数据时被调用                                |
| ChannelWritabilityChanged    | 当 Channel 的可写状态发生改变时被调用。用户可以确保写操作不会完成得太快（以避免发生 OutOfMemoryError）或者可以在 Channel 变为再次可写时恢复写入。可以通过调用 Channel 的 isWritable()方法来检测Channel 的可写性。与可写性相关的阈值可以通过 Channel.config().setWriteHighWaterMark()Channel.config().setWriteLowWaterMark()方法来设置 |
| userEventTriggered           | 当 ChannelnboundHandler.fireUserEventTriggered()方法被调用时被调用，因为一个 POJO 被传经了 ChannelPipeline |

当某个ChannelHandler的实现重写channelRead()方法时，它将负责显式地释放与池化的 ByteBuf 实例相关的内存。

```java
/**
 * 代码清单 6-1 释放消息资源
 * 扩展了 ChannelInboundHandlerAdapter
 */
//标示一个Channel-Handler 可以被多个Channel安全地共享
@Sharable
public class DiscardHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //丢弃已接收的消息
        ReferenceCountUtil.release(msg);
    }

}
```

使用SimpleChannelInboundHandler可以自动释放资源

```java
/**
 * 代码清单 6-2 使用 SimpleChannelInboundHandler
 *
 */
@Sharable
public class SimpleDiscardHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //不需要任何显式的资源释放
    }
    
}
```

### 6.1.4 ChannelOutboundHandler 接口(5.0之后合并至ChannelHandler )

出站操作和数据将由 ChannelOutboundHandler 处理。它的方法将被 Channel、ChannelPipeline 以及 ChannelHandlerContext 调用.是可以按需推迟操作或者事件，这使得可以通过一些复杂的方法来处理请求。例如，如果到远程节点的写入被暂停了，那么你可以推迟冲刷操作并在稍后继续.

| ChannelOutboundHandler 的方法                                | 描述                                                |
| ------------------------------------------------------------ | --------------------------------------------------- |
| bind(ChannelHandlerContext,SocketAddress,ChannelPromise)     | 当请求将 Channel 绑定到本地地址时被调用             |
| connect(ChannelHandlerContext,SocketAddress,SocketAddress,ChannelPromise) | 当请求将 Channel 连接到远程节点时被调用             |
| disconnect(ChannelHandlerContext,ChannelPromise)             | 当请求将 Channel 从远程节点断开时被调用             |
| close(ChannelHandlerContext,ChannelPromise)                  | 当请求关闭 Channel 时被调用                         |
| deregister(ChannelHandlerContext,ChannelPromise)             | 当请求将 Channel 从它的 EventLoop 注销时被调用      |
| read(ChannelHandlerContext)                                  | 当请求从 Channel 读取更多的数据时被调用             |
| flush(ChannelHandlerContext)                                 | 当请求通过 Channel 将入队数据冲刷到远程节点时被调用 |
| write(ChannelHandlerContext,Object,ChannelPromise)           | 当请求通过 Channel 将数据写到远程节点时被调用       |

ChannelPromise是ChannelFuture的一个子类，其定义了一些可写的方法，如setSuccess()和setFailure()，从而使ChannelFuture不可变 。

### 6.1.5 ChannelHandler 适配器

```java
class ChannelHandlerAdapter implements ChannelHandler
```

提供了isSharable()。判断对应的实现被标注为Sharable，如果为true，表示它可以被添加到多个 ChannelPipeline

### 6.1.6 资源管理

​	每当通过调用 ChannelInboundHandler.channelRead()或者 ChannelOutboundHandler.write()方法来处理数据时，你都需要确保没有任何的资源泄漏。

​	Netty提供了class ResourceLeakDetector级 别，它将对你应用程序的缓冲区分配做大约 1%的采样来检测内存泄露。

| 泄漏检测级别 | 描述                                                         |
| ------------ | ------------------------------------------------------------ |
| DISABLED     | 禁用泄漏检测。只有在详尽的测试之后才应设置为这个值           |
| SIMPLE       | 使用 1%的默认采样率检测并报告任何发现的泄露。这是默认级别，适合绝大部分的情况 |
| ADVANCED     | 使用默认的采样率，报告所发现的任何的泄露以及对应的消息被访问的位置 |
| PARANOID     | 类似于 ADVANCED，但是其将会对每次（对消息的）访问都进行采样。这对性能将会有很大的影响，应该只在调试阶段使用 |

设置检测级别

```java
java -Dio.netty.leakDetectionLevel=ADVANCED
```

```java
/**
 * 代码清单 6-3 消费并释放入站消息
 *
 */
@Sharable
public class DiscardInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //通过调用 ReferenceCountUtil.release()方法释放资源
        //不会通过调用 ChannelHandlerContext.fireChannelRead()方法将入站消息转发给下一个 ChannelInboundHandler
        ReferenceCountUtil.release(msg);
    }
}
```

SimpleChannelInboundHandler实现会在消息被channelRead()方法消费之后自动释放消息

```java
/**
 * 代码清单 6-4 丢弃并释放出站消息
 *
 */
@Sharable
public class DiscardOutboundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx,
        Object msg, ChannelPromise promise) {
        //通过使用 ReferenceCountUtil.realse(...)方法释放资源
        ReferenceCountUtil.release(msg);
        //通知 ChannelPromise数据已经被处理了
        promise.setSuccess();
    }
}
```

​	重要的是，不仅要释放资源，还要通知 ChannelPromise。否则可能会出现 ChannelFutureListener 收不到某个消息已经被处理了的通知的情况。如果一个消息被消费或者丢弃了，并且没有传递给ChannelPipeline 中的下一个ChannelOutboundHandler，那么用户就有责任调用 ReferenceCountUtil.release()。如果消息到达了实际的传输层，那么当它被写入时或者 Channel 关闭时，都将被自动释放。

## 6.2 ChannelPipeline 接口

​	每一个新创建的 Channel 都将会被分配一个新的 ChannelPipeline。这项关联是永久性的。

​	根据事件的起源，事件会被ChannelHandler处理，随后调用ChannelHandlerContext实现，它将被转发给同一超类型的下一个ChannelHandler。

​	ChannelHandlerContext可以使ChannelHandler和它的ChannelPipeline 以及其他的ChannelHandler相互交互。ChannelHandler可以通知其所属的ChannelPipeline 中的下一个ChannelHandler，甚至可以动态修改它所属的ChannelPipeline 。

​	在ChannelPipeline传播事件时，会测试ChannelPipeline中的下一个ChannelHandler类型是否和事件的运动方向相匹配（出站或者入站）。不匹配则跳过该ChannelHandler并前进到下一个。

### 6.2.1 修改ChannelPipeline



| ChannelHandler 的用于修改 ChannelPipeline 的方法 | 描述                                                         |
| ------------------------------------------------ | ------------------------------------------------------------ |
| addFirst/addLat/addBefore/addAfter               | 将一个ChannelHandler 添加到ChannelPipeline 中                |
| remove                                           | 将一个ChannelHandler 从ChannelPipeline 中移除                |
| replace                                          | 将 ChannelPipeline 中的一个 ChannelHandler 替换为另一个 ChannelHandler |

```java
    /**
     * 代码清单 6-5 修改 ChannelPipeline
     * */
    public static void modifyPipeline() {
        ChannelPipeline pipeline = CHANNEL_PIPELINE_FROM_SOMEWHERE;
        //创建一个 FirstHandler 的实例
        FirstHandler firstHandler = new FirstHandler();
        //将该实例作为"handler1"添加到ChannelPipeline 中
        pipeline.addLast("handler1", firstHandler);
        //将一个 SecondHandler的实例作为"handler2"添加到 ChannelPipeline的第一个槽中。这意味着它将被放置在已有的"handler1"之前
        pipeline.addFirst("handler2", new SecondHandler());
        //将一个 ThirdHandler 的实例作为"handler3"添加到 ChannelPipeline 的最后一个槽中
        pipeline.addLast("handler3", new ThirdHandler());
        //...
        //通过名称移除"handler3"
        pipeline.remove("handler3");
        //通过引用移除FirstHandler（它是唯一的，所以不需要它的名称）
        pipeline.remove(firstHandler);
        //将 SecondHandler("handler2")替换为 FourthHandler:"handler4"
        pipeline.replace("handler2", "handler4", new FourthHandler());
    }
    private static final class FirstHandler
            extends ChannelHandlerAdapter {

    }
```

​	ChannelHandler 的执行和阻塞:通常 ChannelPipeline 中的每一个 ChannelHandler 都是通过它的 EventLoop（I/O 线程）来处理传递给它的事件的。所以至关重要的是不要阻塞这个线程，因为这会对整体的 I/O 处理产生负面的影响。ChannelPipeline 有一些接受一个 EventExecutorGroup 的 add()方法。如果一个事件被传递给一个自定义的 EventExecutorGroup，它将被包含在这个 EventExecutorGroup 中的某个 EventExecutor 所处理，从而被从该Channel 本身的 EventLoop 中移除。Netty 提供了一个叫 DefaultEventExecutorGroup 的默认实现

| ChannelPipeline 的用于访问 ChannelHandler 的操作 | 描述                                               |
| ------------------------------------------------ | -------------------------------------------------- |
| get                                              | 通过类型或者名称返回 ChannelHandler                |
| context                                          | 返回和 ChannelHandler 绑定的 ChannelHandlerContext |
| names                                            | 返回 ChannelPipeline 中所有 ChannelHandler 的名称  |

### 6.2.2 触发事件



| ChannelPipeline 的入站操作    | 描述                                                         |
| ----------------------------- | ------------------------------------------------------------ |
| fireChannelRegistered         | 调用 ChannelPipeline 中下一个 ChannelInboundHandler 的
channelRegistered(ChannelHandlerContext)方法 |
| fireChannelUnregistered       | 调用 ChannelPipeline 中下一个 ChannelInboundHandler 的
channelUnregistered(ChannelHandlerContext)方法 |
| fireChannelActive             | 调用 ChannelPipeline 中下一个 ChannelInboundHandler 的
channelActive(ChannelHandlerContext)方法 |
| fireChannelInactive           | 调用 ChannelPipeline 中下一个 ChannelInboundHandler 的
channelInactive(ChannelHandlerContext)方法 |
| fireExceptionCaught           | 调用 ChannelPipeline 中下一个 ChannelInboundHandler 的
exceptionCaught(ChannelHandlerContext, Throwable)方法 |
| fireUserEventTriggered        | 调用 ChannelPipeline 中下一个 ChannelInboundHandler 的
userEventTriggered(ChannelHandlerContext, Object)方法 |
| fireChannelRead               | 调用 ChannelPipeline 中下一个 ChannelInboundHandler 的
channelRead(ChannelHandlerContext, Object msg)方法 |
| fireChannelReadComplete       | 调用 ChannelPipeline 中下一个 ChannelInboundHandler 的
channelReadComplete(ChannelHandlerContext)方法 |
| fireChannelWritabilityChanged | 调用 ChannelPipeline 中下一个 ChannelInboundHandler 的
channelWritabilityChanged(ChannelHandlerContext)方法 |

| ChannelPipeline 的出站操作 | 描述                                                         |
| -------------------------- | ------------------------------------------------------------ |
| bind                       | 将 Channel 绑定到一个本地地址，这将调用 ChannelPipeline 中的下一个
ChannelOutboundHandler 的 bind(ChannelHandlerContext, SocketAddress, ChannelPromise)方法 |
| connect                    | 将 Channel 连接到一个远程地址，这将调用 ChannelPipeline 中的下一个
ChannelOutboundHandler 的 connect(ChannelHandlerContext, SocketAddress, ChannelPromise)方法 |
| disconnect                 | 将Channel 断开连接。这将调用ChannelPipeline 中的下一个ChannelOutboundHandler 的 disconnect(ChannelHandlerContext, Channel Promise)方法 |
| close                      | 将 Channel 关闭。这将调用 ChannelPipeline 中的下一个 ChannelOutboundHandler 的 close(ChannelHandlerContext, ChannelPromise)方法 |
| deregister                 | 将 Channel 从它先前所分配的 EventExecutor（即 EventLoop）中注销。这将调
用 ChannelPipeline 中的下一个 ChannelOutboundHandler 的 deregister
(ChannelHandlerContext, ChannelPromise)方法 |
| flush                      | 冲刷Channel所有挂起的写入。这将调用ChannelPipeline中的下一个ChannelOutboundHandler 的 flush(ChannelHandlerContext)方法 |
| write                      | 将消息写入 Channel。这将调用 ChannelPipeline 中的下一个 ChannelOutboundHandler的write(ChannelHandlerContext, Object msg, ChannelPromise)方法。注意：这并不会将消息写入底层的 Socket，而只会将它放入队列中。要将它写入 Socket，需要调用 flush()或者 writeAndFlush()方法 |
| writeAndFlush              | 这是一个先调用 write()方法再接着调用 flush()方法的便利方法   |
| read                       | 请求从 Channel 中读取更多的数据。这将调用 ChannelPipeline 中的下一个
ChannelOutboundHandler 的 read(ChannelHandlerContext)方法 |

ChannelPipeline 保存了与 Channel 相关联的 ChannelHandler；
ChannelPipeline 可以根据需要，通过添加或者删除 ChannelHandler 来动态地修改；
ChannelPipeline 有着丰富的 API 用以被调用，以响应入站和出站事件

## 6.3 ChannelHandlerContext 接口

​	ChannelHandlerContext 代表了 ChannelHandler 和 ChannelPipeline 之间的关联，每当有 ChannelHandler 添加到 ChannelPipeline 中时，都会创建 ChannelHandlerContext。ChannelHandlerContext 的主要功能是管理它所关联的 ChannelHandler 和在同一个 ChannelPipeline 中的其他 ChannelHandler 之间的交互。如果调用 Channel 或者 ChannelPipeline 上的这些方法，它们将沿着整个 ChannelPipeline 进行传播。而调用位于 ChannelHandlerContext上的相同方法，则将从当前所关联的 ChannelHandler 开始，并且只会传播给位于该ChannelPipeline 中的下一个能够处理该事件的 ChannelHandler

| ChannelHandlerContext 的 API  | 描述                                                         |
| ----------------------------- | ------------------------------------------------------------ |
| alloc                         | 返回和这个实例相关联的Channel 所配置的 ByteBufAllocator      |
| bind                          | 绑定到给定的 SocketAddress，并返回 ChannelFuture             |
| channel                       | 返回绑定到这个实例的 Channel                                 |
| close                         | 关闭 Channel，并返回 ChannelFuture                           |
| connect                       | 连接给定的 SocketAddress，并返回 ChannelFuture               |
| deregister                    | 从之前分配的 EventExecutor 注销，并返回 ChannelFuture        |
| disconnect                    | 从远程节点断开，并返回 ChannelFuture                         |
| executor                      | 返回调度事件的 EventExecutor                                 |
| fireChannelActive             | 触发对下一个 ChannelInboundHandler 上的
channelActive()方法（已连接）的调用 |
| fireChannelInactive           | 触发对下一个 ChannelInboundHandler 上的
channelInactive()方法（已关闭）的调用 |
| fireChannelRead               | 触发对下一个 ChannelInboundHandler 上的
channelRead()方法（已接收的消息）的调用 |
| fireChannelReadComplete       | 触发对下一个ChannelInboundHandler上的
channelReadComplete()方法的调用 |
| fireChannelRegistered         | 触发对下一个 ChannelInboundHandler 上的
fireChannelRegistered()方法的调用 |
| fireChannelUnregistered       | 触发对下一个 ChannelInboundHandler 上的
fireChannelUnregistered()方法的调用 |
| fireChannelWritabilityChanged | 触发对下一个 ChannelInboundHandler 上的
fireChannelWritabilityChanged()方法的调用 |
| fireExceptionCaught           | 触发对下一个 ChannelInboundHandler 上的
fireExceptionCaught(Throwable)方法的调用 |
| fireUserEventTriggered        | 触发对下一个 ChannelInboundHandler 上的
fireUserEventTriggered(Object evt)方法的调用 |
| handler                       | 返回绑定到这个实例的 ChannelHandler                          |
| isRemoved                     | 如果所关联的 ChannelHandler 已经被从 ChannelPipeline
中移除则返回 true |
| name                          | 返回这个实例的唯一名称                                       |
| pipeline                      | 返回这个实例所关联的 ChannelPipeline                         |
| read                          | 将数据从Channel读取到第一个入站缓冲区；如果读取成功则触
发 一个channelRead事件，并（在最后一个消息被读取完成后）
通 知 ChannelInboundHandler 的 channelReadComplete
(ChannelHandlerContext)方法 |
| write                         | 通过这个实例写入消息并经过 ChannelPipeline                   |
| writeAndFlush                 | 通过这个实例写入并冲刷消息并经过 ChannelPipeline             |

​	ChannelHandlerContext 和 ChannelHandler 之间的关联（绑定）是永远不会改变的，所以缓存对它的引用是安全的；
 	相对于其他类的同名方法，ChannelHandlerContext
的方法将产生更短的事件流，应该尽可能地利用这个特性来获得最大的性能

### 6.3.1 使用 ChannelHandlerContext

![ChannelHandlerContext1.png](https://github.com/jzz-12-6/image/blob/master/ChannelHandlerContext1.png?raw=true)

```java
    /**
     * 代码清单 6-6 从 ChannelHandlerContext 访问 Channel
     * */
    public static void writeViaChannel() {
        ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE;
        //获取到与 ChannelHandlerContext相关联的 Channel 的引用
        Channel channel = ctx.channel();
        //通过 Channel 写入缓冲区
        channel.write(Unpooled.copiedBuffer("Netty in Action",
                CharsetUtil.UTF_8));

    }
    /**
     * 代码清单 6-7 通过 ChannelHandlerContext 访问 ChannelPipeline
     * */
    public static void writeViaChannelPipeline() {
        ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE; 
        //获取到与 ChannelHandlerContext相关联的 ChannelPipeline 的引用
        ChannelPipeline pipeline = ctx.pipeline();
        //通过 ChannelPipeline写入缓冲区
        pipeline.write(Unpooled.copiedBuffer("Netty in Action",
                CharsetUtil.UTF_8));

    }
 	/**
     * 代码清单 6-8 调用 ChannelHandlerContext 的 write()方法
     * */
    public static void writeViaChannelHandlerContext() {
        //获取到 ChannelHandlerContext 的引用
        ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE;
        //write()方法将把缓冲区数据发送到下一个 ChannelHandler
        ctx.write(Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8));
    }
```

![ChannelHandler2.png](https://github.com/jzz-12-6/image/blob/master/ChannelHandler2.png?raw=true)

​	为什么会想要从 ChannelPipeline 中的某个特定点开始传播事件呢？

​	1.为了减少将事件传经对它不感兴趣的 ChannelHandler 所带来的开销

​	2.为了避免将事件传经那些可能会对它感兴趣的 ChannelHandler

![ChannelHandler3.png](https://github.com/jzz-12-6/image/blob/master/ChannelHandler3.png?raw=true)

### 6.3.2 ChannelHandler 和 ChannelHandlerContext 的高级用法

```java
/**
 * 代码清单 6-9 缓存到 ChannelHandlerContext 的引用
 *
 */
public class WriteHandler extends ChannelHandlerAdapter {
    private ChannelHandlerContext ctx;
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        //存储到 ChannelHandlerContext的引用以供稍后使用
        this.ctx = ctx;
    }
    public void send(String msg) {
        //使用之前存储的到 ChannelHandlerContext的引用来发送消息
        ctx.writeAndFlush(msg);
    }
}

/**
 * 代码清单 6-10 可共享的 ChannelHandler
 * 多个 ChannelPipeline 中共享同一个 ChannelHandler
 * 只应该在确定了你的 ChannelHandler 是线程安全的时才使用@Sharable 注解。
 */
@Sharable
public class SharableHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("channel read message " + msg);
        //记录方法调用，并转发给下一个 ChannelHandler
        ctx.fireChannelRead(msg);
    }
}
```

## 6.4 异常处理

### 6.4.1 处理入站异常

```java
/**
 * 代码清单 6-12 基本的入站异常处理
 * 异常会按照入站方向流动 一般异常处理放在ChannelPipeline的最后
 */
public class InboundExceptionHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
        Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

```

​	ChannelHandler.exceptionCaught()的默认实现是简单地将当前异常转发给ChannelPipeline 中的下一个 ChannelHandler；
​	如果异常到达了 ChannelPipeline 的尾端，它将会被记录为未被处理；
​	要想定义自定义的处理逻辑，你需要重写 exceptionCaught()方法。然后你需要决定是否需要将该异常传播出去

### 6.4.2 处理出战异常

​	用于处理出站操作中的正常完成以及异常的选项，都基于以下的通知机制。

​	1.每个出站操作都将返回一个 ChannelFuture。注册到 ChannelFuture 的 ChannelFutureListener 将在操作完成时被通知该操作是成功了还是出错了。
​	2.几乎所有的 ChannelOutboundHandler 上的方法都会传入一个 ChannelPromise的实例。作为 ChannelFuture 的子类，ChannelPromise 也可以被分配用于异步通知的监听器。但是，ChannelPromise 还具有提供立即通知的可写方法ChannelPromise setSuccess() || setFailure(Throwable cause);

```java
	 /**
     * 代码清单 6-13 添加 ChannelFutureListener 到 ChannelFuture
     * */
    public static void addingChannelFutureListener(){
        Channel channel = new NioSocketChannel();
        ByteBuf someMessage = Unpooled.buffer(1024);
        //获取出战操作的ChannelFuture
        ChannelFuture future = channel.write(someMessage);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) {
                if (!f.isSuccess()) {
                    f.cause().printStackTrace();
                    f.channel().close();
                }
            }
        });
    }
/**
 * 代码清单 6-14 添加 ChannelFutureListener 到 ChannelPromise
 * 将 ChannelFutureListener 添加到即将作为参数传递给 ChannelOutboundHandler 的方法的 ChannelPromise
 */
public class OutboundExceptionHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        
        promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) {
                if (!f.isSuccess()) {
                    f.cause().printStackTrace();
                    f.channel().close();
                }
            }
        });
    }
}

```



# 第 7 章 EventLoop和线程模型

## 7.1 线程模型概述

​	基本的线程池化模式：

1. 从池的空闲线程列表中选择一个 Thread，并且指派它去运行一个已提交的任务（一个Runnable 的实现）；
2. 当任务完成时，将该 Thread 返回给该列表，使其可被重用。

![Executoræ§è¡é"è¾.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/Executor%E6%89%A7%E8%A1%8C%E9%80%BB%E8%BE%91.png?raw=true)

## 7.2 EventLoop 接口

​	事件循环:运行任务来处理在连接的生命周期内发生的事件是任何网络框架的基本功能

```java
    /**
     * 代码清单 7-1 在事件循环中执行任务
     * */
    public static void executeTaskInEventLoop() {
        boolean terminated = true;
        while (!terminated) {
            //阻塞，直到有事件已经就绪可被运行
            List<Runnable> readyEvents = blockUntilEventsReady();
            for (Runnable ev: readyEvents) {
                //循环遍历，并处理所有的事件
                ev.run();
            }
        }
    }
```

​	Netty 的 EventLoop 是协同设计的一部分，它采用了两个基本的 API：并发和网络编程。

1. io.netty.util.concurrent 包构建在 JDK 的 java.util.concurrent 包上，用来提供线程执行器。
2. io.netty.channel 包中的类，为了与 Channel 的事件进行交互，扩展了这些接口/类。

![EventLoop çç±"å±æ¬¡ç"æ.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/EventLoop%20%E7%9A%84%E7%B1%BB%E5%B1%82%E6%AC%A1%E7%BB%93%E6%9E%84.png?raw=true)

​	在这个模型中，一个 EventLoop 将由一个永远都不会改变的 Thread 驱动，同时任务（Runnable 或者 Callable）可以直接提交给 EventLoop 实现，以立即执行或者调度执行。根据配置和可用核心的不同，可能会创建多个 EventLoop 实例用以优化资源的使用，并且单个EventLoop 可能会被指派用于服务多个 Channel。EventLoop.parent():返回到当前EventLoop实现的实例所属的EventLoopGroup的引用。

### 7.2.1 Netty 4 中的 I/O 和事件处理

​	由 I/O 操作触发的事件将流经安装了一个或者多个ChannelHandler 的 ChannelPipeline。传播这些事件的方法调用可以随后被 ChannelHandler 所拦截，并且可以按需地处理事件。在Netty 4 中，所有的I/O操作和事件都由已经被分配给了EventLoop的那个Thread来处理

### 7.3 任务调度

### 7.3.1 JDK的任务调度API

| java.util.concurrent.Executors 类的工厂方法                  | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| newScheduledThreadPool(int corePoolSize)

newScheduledThreadPool(int corePoolSize,ThreadFactorythreadFactory) | 创建一个 ScheduledThreadExecutorService，
用于调度命令在指定延迟之后运行或者周期性地执行。它使用 corePoolSize 参数来计算线程数 |
| newSingleThreadScheduledExecutor()
newSingleThreadScheduledExecutor( ThreadFactorythreadFactory) | 创建一个 ScheduledThreadExecutorService，
用于调度命令在指定延迟之后运行或者周期性地执
行。它使用一个线程来执行被调度的任务 |

```java
    /**
     * 代码清单 7-2 使用 ScheduledExecutorService 调度任务
     * 作为线程池管理的一部分，将会有额外的线程创建。如果有大量任务被紧凑地调度，那么这将成为一个瓶颈
     * */
    public static void schedule() {
        //创建一个其线程池具有 10 个线程的ScheduledExecutorService
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

        ScheduledFuture<?> future = executor.schedule(()->{
                    //创建一个 Runnable，以供调度稍后执行
                    System.out.println("Now it is 60 seconds later");
                },
        //调度任务在从现在开始的 60 秒之后执行
        60, TimeUnit.SECONDS);
        //一旦调度任务执行完成，就关闭 ScheduledExecutorService 以释放资源
        executor.shutdown();
    }
```

### 7.3.2 使用 EventLoop 调度任务

```java
    /**
     * 代码清单 7-3 使用 EventLoop 调度任务
     * */
    public static void scheduleViaEventLoop() {
        Channel ch = new NioSocketChannel();;
        ScheduledFuture<?> future = ch.eventLoop().schedule(()->{
            //创建一个 Runnable以供调度稍后执行
            System.out.println("60 seconds later");
            //调度任务在从现在开始的 60 秒之后执行
            },60,TimeUnit.SECONDS);
        ScheduledFuture<?> future = ch.eventLoop().scheduleAtFixedRate(()->{
            //创建一个 Runnable以供调度稍后执行这将一直运行，直到ScheduledFuture 被取消
            System.out.println("60 seconds later");
            //调度在 60 秒之后，并且以后每间隔 60 秒运行
        },60,60,TimeUnit.SECONDS);
         boolean mayInterruptIfRunning = false;
        //取消该任务，防止它再次运行
        future.cancel(mayInterruptIfRunning);
    }
```

### 7.4 实现细节

### 7.4.1 线程管理

​	EventLoop将负责处理一个Channel的整个生命周期内的所有事件，如果（当前）调用线程正是支撑 EventLoop 的线程，那么所提交的代码块将会被（直接）执行。否则，EventLoop 将调度该任务以便稍后执行，并将它放入到内部队列中。任何的 Thread 与 Channel 直接交互而无需在 ChannelHandler 中进行额外同步。

​	每个 EventLoop 都有它自已的任务队列，独立于任何其他的 EventLoop。

![å¾7-3 EventLoopçæ§è¡é"è¾.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE7-3%20EventLoop%E7%9A%84%E6%89%A7%E8%A1%8C%E9%80%BB%E8%BE%91.png?raw=true)

永远不要将一个长时间运行的任务放入到执行队列中，因为它将阻塞需要在同一线程上执行的任何其他任务

### 7.4.2 EventLoop/线程的分配

1．异步传输

​	异步传输实现只使用了少量的 EventLoop（以及和它们相关联的 Thread），而且在当前的线程模型中，它们可能会被多个 Channel 所共享。可以通过尽可能少量的 Thread 来支撑大量的 Channel，而不是每个 Channel 分配一个 Thread。

![å¾ 7-4 ç¨äºéé"å¡ä¼ è¾ï¼å¦ NIO å AIOï¼ç EventLoop åéæ¹å¼.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%207-4%20%E7%94%A8%E4%BA%8E%E9%9D%9E%E9%98%BB%E5%A1%9E%E4%BC%A0%E8%BE%93%EF%BC%88%E5%A6%82%20NIO%20%E5%92%8C%20AIO%EF%BC%89%E7%9A%84%20EventLoop%20%E5%88%86%E9%85%8D%E6%96%B9%E5%BC%8F.png?raw=true)

​	EventLoopGroup 负责为每个新创建的 Channel 分配一个 EventLoop。在当前实现中，使用顺序循环（round-robin）的方式进行分配以获取一个均衡的分布，并且相同的 EventLoop可能会被分配给多个 Channel。对于所有相关联的 Channel 来说，ThreadLocal 都将是一样的。

2. 阻塞传输

   每一个 Channel 都将被分配给一个 EventLoop（以及它的 Thread）。

![å¾ 7-5 é"å¡ä¼ è¾ï¼å¦ OIOï¼ç EventLoop åéæ¹å¼.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%207-5%20%E9%98%BB%E5%A1%9E%E4%BC%A0%E8%BE%93%EF%BC%88%E5%A6%82%20OIO%EF%BC%89%E7%9A%84%20EventLoop%20%E5%88%86%E9%85%8D%E6%96%B9%E5%BC%8F.png?raw=true)



# 第 8 章 引导（Bootstrapping）

​	引导一个应用程序是指对它进行配置，并使它运行起来的过程

## 8.1 Bootstrap 类

引导类的层次结构

![å¾ 8-1 å¼å¯¼ç±»çå±æ¬¡ç»æ.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%208-1%20%E5%BC%95%E5%AF%BC%E7%B1%BB%E7%9A%84%E5%B1%82%E6%AC%A1%E7%BB%93%E6%9E%84.png?raw=true)

```java
//子类型 B 是其父类型的一个类型参数，因此可以返回到运行时实例的引用以支持方法的链式调用（也就是所谓的流式语法）
public abstract class AbstractBootstrap<B extends AbstractBootstrap<B, C>, C extends Channel> implements Cloneable 
public class Bootstrap extends AbstractBootstrap<Bootstrap, Channel>
public class ServerBootstrap extends AbstractBootstrap<ServerBootstrap, ServerChannel> 
    
```



​	服务器致力于使用一个父 Channel 来接受来自客户端的连接，并创建子 Channel 以用于它们之间的通信；而客户端将最可能只需要一个单独的、没有父 Channel 的 Channel 来用于所有的网络交互。两种应用程序类型之间通用的引导步骤由 AbstractBootstrap 处理，特定于客户端或者服务器的引导步骤则分别由 Bootstrap 或 ServerBootstrap 处理。

为什么引导类是 Cloneable?

​	可能会需要创建多个具有类似配置或者完全相同配置的Channel。为了支持这种模式而又不需要为每个 Channel 都创建并配置一个新的引导类实例，在一个已经配置完成的引导类实例上调用clone()方法将返回另一个可以立即使用的引导类实例。这种方式只会创建引导类实例的EventLoopGroup的一个浅拷贝，所以，后者将在所有克隆的Channel实例之间共享。

### 8.2 引导客户端和无连接协议

​	Bootstrap 类被用于客户端或者使用了无连接协议的应用程序中。

| Bootstrap 类的 API                                           | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| Bootstrap group(EventLoopGroup)                              | 设置用于处理 Channel 所有事件的 EventLoopGroup               |
| Bootstrap channel(Class<? extends C>)<br/>Bootstrap channelFactory(ChannelFactory<? extends C>) | channel()方法指定了Channel的实现类。如果该实现类没提供默认的构造函数 ，可以通过调用channelFactory()方法来指定一个工厂类，它将会被bind()方法调用 |
| Bootstrap localAddress(SocketAddress)                        | 指定 Channel 应该绑定到的本地地址。如果没有指定，则将由操作系统创建一个随机的地址。或者，也可以通过bind()或者 connect()方法指定 localAddress |
| Bootstrap option(<br/>ChannelOption<T> option,T value)       | 设置 ChannelOption，其将被应用到每个新创建的Channel 的 ChannelConfig。这些选项将会通过bind()或者 connect()方法设置到 Channel，不管哪个先被调用。这个方法在 Channel 已经被创建后再调用将不会有任何的效果。支持的 ChannelOption 取决于使用的 Channel 类型 |
| Bootstrap attr(<br/>Attribute<T> key, T value)               | 指定新创建的 Channel 的属性值。这些属性值是通过bind()或者 connect()方法设置到 Channel 的，具体取决于谁最先被调用。这个方法在 Channel 被创建后将不会有任何的效果。 |
| Bootstrap <br/>handler(ChannelHandler)                       | 设置将被添加到 ChannelPipeline 以接收事件通知的ChannelHandler |
| Bootstrap clone()                                            | 创建一个当前 Bootstrap 的克隆，其具有和原始的Bootstrap 相同的设置信息 |
| Bootstrap remoteAddress(<br/>SocketAddress)                  | 设置远程地址。或者，也可以通过 connect()方法来指定它         |
| ChannelFuture connect()                                      | 连接到远程节点并返回一个 ChannelFuture，其将会在连接操作完成后接收到通知 |
| ChannelFuture bind()                                         | 绑定 Channel 并返回一个 ChannelFuture，其将会在绑定操作完成后接收到通知，在那之后必须调用 Channel.connect()方法来建立连接 |



### 8.2.1 引导客户端

​	Bootstrap 类负责为客户端和使用无连接协议的应用程序创建 Channel

![å¾ 8-2 å¼å¯¼è¿ç¨.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%208-2%20%E5%BC%95%E5%AF%BC%E8%BF%87%E7%A8%8B.png?raw=true)

```java
    /**
     * 代码清单 8-1 引导一个客户端
     * */
    public void bootstrap() {
        //设置 EventLoopGroup，提供用于处理 Channel 事件的 EventLoop
        EventLoopGroup group = new NioEventLoopGroup();
        //创建一个Bootstrap类的实例以创建和连接新的客户端Channel
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
            //指定要使用的Channel 实现
            .channel(NioSocketChannel.class)
            //设置用于 Channel 事件和数据的ChannelInboundHandler
            .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(
                            ChannelHandlerContext channelHandlerContext,
                            ByteBuf byteBuf) throws Exception {
                        System.out.println("Received data");
                    }
                });
        //连接到远程主机
        ChannelFuture future = bootstrap.connect(
            new InetSocketAddress("www.manning.com", 80));
        future.addListener((ChannelFuture channelFuture)->{
            if (channelFuture.isSuccess()) {
                System.out.println("Connection established");
            } else {
                System.err.println("Connection attempt failed");
                channelFuture.cause().printStackTrace();
            }
        });
    }

```

### 8.2.2 Channel 和 EventLoopGroup 的兼容性

​		可以从包名以及与其相对应的类名的前缀看到，对于 NIO 以及 OIO 传输两者来说，都有相关的 EventLoopGroup 和Channel 实现。

```html
channel
├───nio
│ NioEventLoopGroup
├───oio
│ OioEventLoopGroup
└───socket
  	  ├───nio
	  │		NioDatagramChannel
	  │ 	NioServerSocketChannel
	  │     NioSocketChannel
	  └───oio
			OioDatagramChannel
			OioServerSocketChannel
			OioSocketChannel

```

```java
    /**
     * 代码清单 8-3 不兼容的 Channel 和 EventLoopGroup
     * 不能混用具有不同前缀的组件，否则抛出异常IllegalStateException
     * */
    public void bootstrap() {
        EventLoopGroup group = new NioEventLoopGroup();
        //创建一个新的 Bootstrap 类的实例，以创建新的客户端Channel
        Bootstrap bootstrap = new Bootstrap();
        //指定一个适用于 NIO 的 EventLoopGroup 实现
        bootstrap.group(group)
            //指定一个适用于 OIO 的 Channel 实现类
            .channel(OioSocketChannel.class)
            //设置一个用于处理 Channel的 I/O 事件和数据的 ChannelInboundHandler
               .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(
                            ChannelHandlerContext channelHandlerContext,
                            ByteBuf byteBuf) throws Exception {
                        System.out.println("Received data");
                    }
                });
        //尝试连接到远程节点
        ChannelFuture future = bootstrap.connect(
                new InetSocketAddress("www.manning.com", 80));
        future.syncUninterruptibly();
    }
```

## 8.3 引导服务器

### 8.3.1 ServerBootstrap 类

| ServerBootstrap 类的方法 | 描述                                                         |
| ------------------------ | ------------------------------------------------------------ |
| group                    | 设置 ServerBootstrap 要用的 EventLoopGroup。这个 EventLoopGroup将用于 ServerChannel 和被接受的子 Channel 的 I/O 处理 |
| channel                  | 设置将要被实例化的 ServerChannel 类                          |
| channelFactory           | 如果不能通过默认的构造函数 创建Channel，那么可以提供一个ChannelFactory |
| localAddress             | 指定 ServerChannel 应该绑定到的本地地址。如果没有指定，则将由操作系统使用一个随机地址。或者，可以通过 bind()方法来指定该 localAddress |
| option                   | 指定要应用到新创建的 ServerChannel 的 ChannelConfig 的 ChannelOption。这些选项将会通过 bind()方法设置到 Channel。在 bind()方法被调用之后，设置或者改变 ChannelOption 都不会有任何的效果。所支持的 ChannelOption 取决于所使用的 Channel 类型。 |
| childOption              | 指定当子 Channel 被接受时，应用到子 Channel 的 ChannelConfig 的ChannelOption。所支持的 ChannelOption 取决于所使用的 Channel 的类型。 |
| attr                     | 指定 ServerChannel 上的属性，属性将会通过 bind()方法设置给 Channel。在调用 bind()方法之后改变它们将不会有任何的效果 |
| childAttr                | 将属性设置给已经被接受的子 Channel。接下来的调用将不会有任何的效果 |
| handler                  | 设置被添加到ServerChannel 的ChannelPipeline中的ChannelHandler。更加常用的方法参见 childHandler() |
| childHandler             | 设置将被添加到已被接受的子 Channel 的 ChannelPipeline 中的 ChannelHandler。handler()方法和 childHandler()方法之间的区别是：前者所添加的 ChannelHandler 由接受子 Channel 的 ServerChannel 处理，而childHandler()方法所添加的 ChannelHandler 将由已被接受的子 Channel处理，其代表一个绑定到远程节点的套接字 |
| clone                    | 克隆一个设置和原始的 ServerBootstrap 相同的 ServerBootstrap  |
| bind                     | 绑定 ServerChannel 并且返回一个 ChannelFuture，其将会在绑定操作完成后收到通知（带着成功或者失败的结果） |

### 8.3.2 引导服务器

![å¾ 8-3 ServerBootstrap å ServerChannel.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%208-3%20ServerBootstrap%20%E5%92%8C%20ServerChannel.png?raw=true)

```java
    /**
     * 代码清单 8-4 引导服务器
     * */
    public void bootstrap() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        //创建 Server Bootstrap
        ServerBootstrap bootstrap = new ServerBootstrap();
        //设置 EventLoopGroup，其提供了用于处理 Channel 事件的EventLoop
        bootstrap.group(group)
            //指定要使用的 Channel 实现
            .channel(NioServerSocketChannel.class)
            //设置用于处理已被接受的子 Channel 的I/O及数据的 ChannelInboundHandler
            .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                                ByteBuf byteBuf) throws Exception {
                        System.out.println("Received data");
                    }
                });
        //通过配置好的 ServerBootstrap 的实例绑定该 Channel
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
        future.addListener((ChannelFuture channelFuture)->{
            if (channelFuture.isSuccess()) {
                System.out.println("Server bound");
            } else {
                System.err.println("Bind attempt failed");
                channelFuture.cause().printStackTrace();
            }
        });
    }
    class SimpleChannelInboundHandlerEt extends SimpleChannelInboundHandler<ByteBuf>{

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            System.out.println("Received data");
        }
    }
```

## 8.4 从 Channel 引导客户端

​	通过将已被接受的子 Channel 的 EventLoop 传递给 Bootstrap的 group()方法来共享该EventLoop。因为分配给 EventLoop 的所有 Channel 都使用同一个线程，所以这避免了额外的线程创建，以及前面所提到的相关的上下文切换。

![å¾ 8-4 å¨ä¸¤ä¸ª Channel ä¹é´å±äº« EventLoop.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%208-4%20%E5%9C%A8%E4%B8%A4%E4%B8%AA%20Channel%20%E4%B9%8B%E9%97%B4%E5%85%B1%E4%BA%AB%20EventLoop.png?raw=true)

```java
    /**
     * 代码清单 8-5 引导服务器
     * */
    public void bootstrap() {
        //创建 ServerBootstrap 以创建 ServerSocketChannel，并绑定它
        ServerBootstrap bootstrap = new ServerBootstrap();
        //设置 EventLoopGroup，其将提供用以处理 Channel 事件的 EventLoop
        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
            //指定要使用的 Channel 实现
            .channel(NioServerSocketChannel.class)
            //设置用于处理已被接受的子 Channel 的 I/O 和数据的 ChannelInboundHandler
             .childHandler(
                        new SimpleChannelInboundHandler<ByteBuf>() {
                            ChannelFuture connectFuture;
                            @Override
                            public void channelActive(ChannelHandlerContext ctx)
                                    throws Exception {
                                //创建一个 Bootstrap 类的实例以连接到远程主机
                                Bootstrap bootstrap = new Bootstrap();
                                //指定 Channel 的实现
                                bootstrap.channel(NioSocketChannel.class).handler(
                                        //为入站 I/O 设置 ChannelInboundHandler
                                        new SimpleChannelInboundHandler<ByteBuf>() {
                                            @Override
                                            protected void channelRead0(
                                                    ChannelHandlerContext ctx, ByteBuf in)
                                                    throws Exception {
                                                System.out.println("Received data");
                                            }
                                        });
                                //使用与分配给已被接受的子Channel相同的EventLoop
                                bootstrap.group(ctx.channel().eventLoop());
                                connectFuture = bootstrap.connect(
                                        //连接到远程节点
                                        new InetSocketAddress("www.manning.com", 80));
                            }

                            @Override
                            protected void channelRead0(
                                    ChannelHandlerContext channelHandlerContext,
                                    ByteBuf byteBuf) throws Exception {
                                if (connectFuture.isDone()) {
                                    //当连接完成时，执行一些数据操作（如代理）
                                    // do something with the data
                                }
                            }
                        });
        //通过配置好的 ServerBootstrap 绑定该 ServerSocketChannel
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
        future.addListener((ChannelFuture channelFuture)->{
            if (channelFuture.isSuccess()) {
                System.out.println("Server bound");
            } else {
                System.err.println("Bind attempt failed");
                channelFuture.cause().printStackTrace();
            }
        });
    }

 
```

## 8.5 在引导过程中添加多个 ChannelHandler

```java
public abstract class ChannelInitializer<C extends Channel>
extends ChannelInboundHandlerAdapter{
//将多个 ChannelHandler 添加到一个 ChannelPipeline
//且一旦 Channel 被注册到了它的 EventLoop 之后，就会调用你的initChannel()版本。在该方法返回之后，ChannelInitializer 的实例将会从 ChannelPipeline 中移除它自己
protected abstract void initChannel(C ch) throws Exception;
}
    /**
     * 代码清单 8-6 引导和使用 ChannelInitializer
     * */
    public void bootstrap() throws InterruptedException {
        //创建 ServerBootstrap 以创建和绑定新的 Channel
        ServerBootstrap bootstrap = new ServerBootstrap();
        //设置 EventLoopGroup，其将提供用以处理 Channel 事件的 EventLoop
        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
            //指定 Channel 的实现
            .channel(NioServerSocketChannel.class)
            //注册一个 ChannelInitializerImpl 的实例来设置 ChannelPipeline
            .childHandler(new ChannelInitializerImpl());
        //绑定到地址
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
        future.sync();
    }

    //用以设置 ChannelPipeline 的自定义 ChannelInitializerImpl 实现
    final class ChannelInitializerImpl extends ChannelInitializer<Channel> {
        @Override
        //将所需的 ChannelHandler 添加到 ChannelPipeline
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new HttpClientCodec());
            pipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));

        }
    }
```

## 8.6 使用 Netty 的 ChannelOption 和属性

​	以使用 option()方法来将 ChannelOption 应用到引导。

```java
    /**
     * 代码清单 8-7 使用属性值
     * */
    public void bootstrap() {
        //创建一个 AttributeKey 以标识该属性
        final AttributeKey<Integer> id = AttributeKey.newInstance("ID");
        //创建一个 Bootstrap 类的实例以创建客户端 Channel 并连接它们
        Bootstrap bootstrap = new Bootstrap();
        //设置 EventLoopGroup，其提供了用以处理 Channel 事件的 EventLoop
        bootstrap.group(new NioEventLoopGroup())
            //指定 Channel 的实现
            .channel(NioSocketChannel.class)
            .handler(
                //设置用以处理 Channel 的 I/O 以及数据的 ChannelInboundHandler
                new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx)
                        throws Exception {
                        //使用 AttributeKey 检索属性以及它的值
                        Integer idValue = ctx.channel().attr(id).get();
                        // do something with the idValue
                    }

                    @Override
                    protected void channelRead0(
                        ChannelHandlerContext channelHandlerContext,
                        ByteBuf byteBuf) throws Exception {
                        System.out.println("Received data");
                    }
                }
            );
        //设置 ChannelOption，其将在 connect()或者bind()方法被调用时被设置到已经创建的 Channel 上
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        //存储该 id 属性
        bootstrap.attr(id, 123456);
        //使用配置好的 Bootstrap 实例连接到远程主机
        ChannelFuture future = bootstrap.connect(
            new InetSocketAddress("www.manning.com", 80));
        future.syncUninterruptibly();
    }
```

## 8.7 引导 DatagramChannel

```java
    /**
     * 代码清单 8-8 使用 Bootstrap 和 DatagramChannel
     * 用于无连接的协议
     */
    public void bootstrap() {
        //创建一个 Bootstrap 的实例以创建和绑定新的数据报 Channel
        Bootstrap bootstrap = new Bootstrap();
        //设置 EventLoopGroup，其提供了用以处理 Channel 事件的 EventLoop
        bootstrap.group(new OioEventLoopGroup()).channel(
            //指定 Channel 的实现
            OioDatagramChannel.class).handler(
                    //设置用以处理 Channel 的I/O 以及数据的 ChannelInboundHandler
                    new SimpleChannelInboundHandler<DatagramPacket>() {
                          @Override
                          protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
                              
                          }
                      }
        );
        //调用 bind() 方法，因为该协议是无连接的
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(0));
        future.addListener((ChannelFuture channelFuture)->{
            if (channelFuture.isSuccess()) {
                System.out.println("Channel bound");
            } else {
                System.err.println("Bind attempt failed");
                channelFuture.cause().printStackTrace();
            }
        });
    }
```

## 8.8 关闭

​	调用 EventLoopGroup.shutdownGracefully()方法，将会返回一个 Future，这个 Future 将在关闭完成时接收到通知shutdownGracefully()方法也是一个异步的操作，所以你需要阻塞等待直到它完成，或者向所返回的 Future 注册一个监听器以在关闭完成时获得通知。

```java
    /**
     * 代码清单 8-9 优雅关闭
     */
    public void bootstrap() {
        //创建处理 I/O 的EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        //创建一个 Bootstrap 类的实例并配置它
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
             .channel(NioSocketChannel.class)
        //...
             .handler(
                new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf)  {
                        System.out.println("Received data");
                    }
                }
             );
        bootstrap.connect(new InetSocketAddress("www.manning.com", 80)).syncUninterruptibly();
        //,,,
        //shutdownGracefully()方法将释放所有的资源，并且关闭所有的当前正在使用中的 Channel
        Future<?> future = group.shutdownGracefully();
        // block until the group has shutdown
        future.syncUninterruptibly();
    }
```

# 第 9 章  单元测试

## 9.1 EmbeddedChannel 概述

​	将入站数据或者出站数据写入到 EmbeddedChannel 中，然后检查是否有任何东西到达了 ChannelPipeline 的尾端。以这种方式，你便可以确定消息是否已经被编码或者被解码过了，以及是否触发了任何的 ChannelHandler 动作。

| EmbeddedChannel 方法               | 职责                                                         |
| ---------------------------------- | ------------------------------------------------------------ |
| writeInbound(<br/>Object... msgs)  | 将入站消息写到 EmbeddedChannel 中。如果可以通过 readInbound()方法从 EmbeddedChannel 中读取数据，则返回 true |
| readInbound()                      | 从 EmbeddedChannel 中读取一个入站消息。任何返回的东西都穿越了整个 ChannelPipeline。如果没有任何可供读取的，则返回 null |
| writeOutbound(<br/>Object... msgs) | 将出站消息写到EmbeddedChannel中。如果现在可以通过readOutbound()方法从 EmbeddedChannel 中读取到什么东西，则返回 true |
| readOutbound()                     | 从 EmbeddedChannel 中读取一个出站消息。任何返回的东西都穿越了整个 ChannelPipeline。如果没有任何可供读取的，则返回 null |
| finish()                           | 将 EmbeddedChannel 标记为完成，并且如果有可被读取的入站数据或者出站数据，则返回 true。这个方法还将会调用 EmbeddedChannel 上的close()方法 |

​	入站数据由 ChannelInboundHandler 处理，代表从远程节点读取的数据。出站数据由ChannelOutboundHandler 处理，代表将要写到远程节点的数据。使用 writeOutbound()方法将消息写到 Channel 中，并通过 ChannelPipeline 沿着出站的方向传递。随后，你可以使用 readOutbound()方法来读取已被处理过的消息，以确定结果是否和预期一样。消息都将会传递过 ChannelPipeline，并且被相关的 ChannelInboundHandler 或者 ChannelOutboundHandler 处理。如果消息没有被消费，那么你可以使用readInbound()或者readOutbound()方法来在处理过了这些消息之后，酌情把它们从Channel中读出来

![å¾ 9-1 EmbeddedChannel çæ°æ®æµ.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%209-1%20EmbeddedChannel%20%E7%9A%84%E6%95%B0%E6%8D%AE%E6%B5%81.png?raw=true)

## 9.2 使用 EmbeddedChannel 测试 ChannelHandler

### 9.2.1 测试入站消息

```java
/**
 * 代码清单9-1 FixedLengthFrameDecoder
 *
 * 扩展 ByteToMessageDecoder 以处理入站字节，并将它们解码为消息
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
    private final int frameLength;

    /**
     * 指定要生成的帧的长度
     * @param frameLength 长度
     */
    public FixedLengthFrameDecoder(int frameLength) {
        if (frameLength <= 0) {
            throw new IllegalArgumentException("frameLength must be a positive integer: " + frameLength);
        }
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        //检查是否有足够的字节可以被读取，以生成下一个帧
        while (in.readableBytes() >= frameLength) {
            //从 ByteBuf 中读取一个新帧
            ByteBuf buf = in.readBytes(frameLength);
            //将该帧添加到已被解码的消息列表中
            out.add(buf);
        }
    }
}

    /**
     * 一个包含 9 个可读字节的 ByteBuf 被解码为 3个 ByteBuf，每个都包含了 3 字节。
     */
    @Test
    public void testFramesDecoded() {
        //创建一个 ByteBuf，并存储 9 字节
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        //创建一个EmbeddedChannel，并添加一个FixedLengthFrameDecoder，其将以 3 字节的帧长度被测试
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        // write bytes
        //将数据写入EmbeddedChannel
        assertTrue(channel.writeInbound(input.retain()));
        //标记 Channel 为已完成状态
        assertTrue(channel.finish());

        // read messages
        //读取所生成的消息，并且验证是否有 3 帧（切片），其中每帧（切片）都为 3 字节
        ByteBuf read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }
    /**
     * 入站 ByteBuf 是通过两个步骤写入的
     */
    @Test
    public void testFramesDecoded2() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        //返回 false，因为没有一个完整的可供读取的帧
        assertFalse(channel.writeInbound(input.readBytes(2)));
        assertTrue(channel.writeInbound(input.readBytes(7)));

        assertTrue(channel.finish());
        ByteBuf read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }
```

### 9.2.2 测试出站消息

```java
/**
 * 代码清单9-3 AbsIntegerEncoder
 *
 * 扩展 MessageToMessageEncoder 以将一个消息编码为另外一种格式
 */
public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
        ByteBuf in, List<Object> out) throws Exception {
        //检查是否有足够的字节用来编码
        while (in.readableBytes() >= 4) {
            //从输入的 ByteBuf中读取下一个整数，并且计算其绝对值
            int value = Math.abs(in.readInt());
            //将该整数写入到编码消息的 List 中
            out.add(value);
        }
    }
        @Test
    public void testEncoded() {
        //(1) 创建一个 ByteBuf，并且写入 9 个负整数
        ByteBuf buf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buf.writeInt(i * -1);
        }

        //(2) 创建一个EmbeddedChannel，并安装一个要测试的 AbsIntegerEncoder
        EmbeddedChannel channel = new EmbeddedChannel(
            new AbsIntegerEncoder());
        //(3) 写入 ByteBuf，并断言调用 readOutbound()方法将会产生数据
        assertTrue(channel.writeOutbound(buf));
        //(4) 将该 Channel 标记为已完成状态
        assertTrue(channel.finish());

        // read bytes
        //(5) 读取所产生的消息，并断言它们包含了对应的绝对值
        for (int i = 1; i < 10; i++) {
            assertEquals((long)i, (long)channel.readOutbound());
        }
        assertNull(channel.readOutbound());
    }
}

```

## 9.3 测试异常处理

```java
/**
 * 代码清单9-5 FrameChunkDecoder
 *
 * 扩展 ByteToMessageDecoder以将入站字节解码为消息
 */
public class FrameChunkDecoder extends ByteToMessageDecoder {
    private final int maxFrameSize;

    /**
     * 指定将要产生的帧的最大允许大小
     * @param maxFrameSize 大小
     */
    public FrameChunkDecoder(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int readableBytes = in.readableBytes();
        if (readableBytes > maxFrameSize) {
            // discard the bytes
            //如果该帧太大，则丢弃它并抛出一个 TooLongFrameException……
            in.clear();
            throw new TooLongFrameException();
        }
        //……否则，从 ByteBuf 中读取一个新的帧
        ByteBuf buf = in.readBytes(readableBytes);
        //将该帧添加到解码 读取一个新的帧消息的 List 中
        out.add(buf);
    }
        @Test
    public void testFramesDecoded() {
        //创建一个 ByteBuf，并向它写入 9 字节
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();

        //创建一个 EmbeddedChannel，并向其安装一个帧大小为 3 字节的 FixedLengthFrameDecoder
        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));

        //向它写入 2 字节，并断言它们将会产生一个新帧
        assertTrue(channel.writeInbound(input.readBytes(2)));
        try {
            //写入一个 4 字节大小的帧，并捕获预期的TooLongFrameException
            channel.writeInbound(input.readBytes(4));
            //如果上面没有 们将会产生一个新帧抛出异常，那么就会到达这个断言，并且测试失败
            Assert.fail();
        } catch (TooLongFrameException e) {
            // expected exception
        }
        //写入剩余的2字节，并断言将会产生一个有效帧
        assertTrue(channel.writeInbound(input.readBytes(3)));
        //将该 Channel 标记为已完成状态
        assertTrue(channel.finish());

        // Read frames
        //读取产生的消息，并且验证值
        ByteBuf read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(2), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.skipBytes(4).readSlice(3), read);
        read.release();
        buf.release();
    }
}
```

如果该类实现了 exceptionCaught()方法并处理了该异常，那么它将不会被 catch块所捕获.

# 第 10 章 编解码器框架

## 10.1 什么是编解码器

​	每个网络应用程序都必须定义如何解析在两个节点之间来回传输的原始字节，以及如何将其和目标应用程序的数据格式做相互转换。这种转换逻辑由编解码器处理，它们每种都可以将字节流从一种格式转换为另一种格式。

​	编码器是将消息转换为适合于传输的格式（最有可能的就是字节流）；而对应的解码器则是将网络字节流转换回应用程序的消息格式。因此，编码器操作出站数据，而解码器处理入站数据。

## 10.2 解码器

​	将字节解码为消息——ByteToMessageDecoder 和 ReplayingDecoder；

​	将一种消息类型解码为另一种——MessageToMessageDecoder。

​	什么时候会用到解码器呢？很简单：每当需要为 ChannelPipeline 中的下一个 ChannelInboundHandler 转换入站数据时会用到。可以将多个解码器链接在一起，以实现任意复杂的转换逻辑

### 10.2.1 抽象类 ByteToMessageDecoder

| ByteToMessageDecoder API                                     | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| decode(<br/>ChannelHandlerContext ctx,<br/>ByteBuf in,<br/>List<Object> out) | 这是你必须实现的唯一抽象方法。decode()方法被调用时将会传入一个包含了传入数据的 ByteBuf，以及一个用来添加解码消息的 List。对这个方法的调用将会重复进行，直到确定没有新的元素被添加到该 List，或者该 ByteBuf 中没有更多可读取的字节时为止。然后，如果该 List 不为空，那么它的内容将会被传递给ChannelPipeline 中的下一个 ChannelInboundHandler |
| decodeLast(<br/>ChannelHandlerContext ctx,<br/>ByteBuf in,<br/>List<Object> out) | Netty提供的这个默认实现只是简单地调用了decode()方法。当Channel的状态变为非活动时，这个方法将会被调用一次。可以重写该方法以提供特殊的处理 |

![å¾ 10-1 ToIntegerDecoder.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%2010-1%20ToIntegerDecoder.png?raw=true)

```java
/**
 * 代码清单 10-1 ToIntegerDecoder 类扩展了 ByteToMessageDecoder
 *
 * 扩展ByteToMessageDecoder类，以将字节解码为特定的格式
 */
public class ToIntegerDecoder extends ByteToMessageDecoder {
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //检查是否至少有 4 字节可读（一个 int 的字节长度）
        if (in.readableBytes() >= 4) {
            //从入站 ByteBuf 中读取一个 int，并将其添加到解码消息的 List 中
            out.add(in.readInt());
        }
    }
}
```

### 10.2.2 抽象类 ReplayingDecoder

```java
//类型参数 S 指定了用于状态管理的类型，其中 Void 代表不需要状态管理
public abstract class ReplayingDecoder<S> extends ByteToMessageDecoder
```

```java
/**
 * 代码清单 10-2 ToIntegerDecoder2 类扩展了 ReplayingDecoder
 *
 * 扩展 ReplayingDecoder<Void> 以将字节解码为消息
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {
    /**
     *
     * @param ctx ChannelHandlerContext
     * @param in ByteBuf ReplayingDecoderByteBuf扩展了ByteBuf
     * @param out List<Object>
     * @throws Exception
     */
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //从入站 ByteBuf 中读取 一个 int，并将其添加到解码消息的 List 中
        //如果没有足够的字节可用，readInt()方法的实现将会抛出一个Error其将在基类中被捕获并处理
        out.add(in.readInt());
    }
}
```

1. 并不是所有的 ByteBuf 操作都被支持，如果调用了一个不被支持的方法，将会抛出UnsupportedOperationException；
2. ReplayingDecoder 稍慢于 ByteToMessageDecoder。

如果使用 ByteToMessageDecoder 不会引入太多的复杂性，那么请使用它；否则，请使用 ReplayingDecoder。

| 解码器                | 描述                                            |
| --------------------- | ----------------------------------------------- |
| LineBasedFrameDecoder | 使用了行尾控制字符（\n 或者\r\n）来解析消息数据 |
| HttpObjectDecoder     | 一个 HTTP 数据的解码器                          |

### 10.2.3 抽象类 MessageToMessageDecoder（两个消息格式之间进行转换）

```java
//类型参数 I 指定了 decode()方法的输入参数 msg 的类型
public abstract class MessageToMessageDecoder<I> extends ChannelInboundHandlerAdapter
```

| MessageToMessageDecoder API                                  | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| decode(<br/>ChannelHandlerContext ctx,<br/>I msg,<br/>List<Object> out) | 对于每个需要被解码为另一种格式的入站消息来说，该方法都将会被调用。解码消息随后会被传递给 ChannelPipeline中的下一个 ChannelInboundHandler |

![å¾ 10-2 IntegerToStringDecoder.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%2010-2%20IntegerToStringDecoder.png?raw=true)

```java
/**
 * 代码清单 10-3 IntegerToStringDecoder 类
 *
 * 扩展了MessageToMessageDecoder<Integer>
 */
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {
    @Override
    public void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
        //将 Integer 消息转换为它的 String 表示，并将其添加到输出的 List 中
        out.add(String.valueOf(msg));
    }
}
```

### 10.2.4 TooLongFrameException 类

​	Netty 是一个异步框架，所以需要在字节可以解码之前在内存中缓冲它们。因此，不能让解码器缓冲大量的数据以至于耗尽可用的内存。你可以设置一个最大字节数的阈值，如果超出该阈值，则会导致抛出TooLongFrameException（随后会被 ChannelHandler.exceptionCaught()方法捕获）。

```java
/**
 * 代码清单 10-4 TooLongFrameException
 *
 * 扩展 ByteToMessageDecoder 以将字节解码为消息
 */
public class SafeByteToMessageDecoder extends ByteToMessageDecoder {
    private static final int MAX_FRAME_SIZE = 1024;
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            int readable = in.readableBytes();
            //检查缓冲区中是否有超过 MAX_FRAME_SIZE 个字节
            if (readable > MAX_FRAME_SIZE) {
                //跳过所有的可读字节，抛出 TooLongFrameException 并通知 ChannelHandler
                in.skipBytes(readable);
                throw new TooLongFrameException("Frame too big!");
        }
        // do something
        // ...
    }
}
```

## 10.3 编码器

### 10.3.1 抽象类 MessageToByteEncoder

| MessageToByteEncoder API                                     |                                                              |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| encode(<br/>ChannelHandlerContext ctx,<br/>I msg,<br/>ByteBuf out) | encode()方法是你需要实现的唯一抽象方法。它被调用时将会传入要被该类编码为 ByteBuf 的（类型为 I 的）出站消息。该 ByteBuf 随后将会被转发给 ChannelPipeline中的下一个 ChannelOutboundHandl |

```java
/**
 * 代码清单 10-5 ShortToByteEncoder 类
 * 接受一个 Short 类型的实例作为消息，将它编码为 Short 的原子类型值，并将它写入 ByteBuf 中，
 * 其将随后被转发给 ChannelPipeline 中的下一个 ChannelOutboundHandler。
 * 每个传出的 Short 值都将会占用 ByteBuf 中的 2 字节
 * 扩展了MessageToByteEncoder
 */
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {
    @Override
    public void encode(ChannelHandlerContext ctx, Short msg, ByteBuf out) throws Exception {
        //将 Short 写入 ByteBuf 中
        out.writeShort(msg);
    }
}
```

Netty 提供了一些专门化的 MessageToByteEncoder，WebSocket08FrameEncoder 类。

![å¾ 10-3 ShortToByteEncoder.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%2010-3%20ShortToByteEncoder.png?raw=true)

### 10.3.2 抽象类 MessageToMessageEncoder

出站数据将如何从一种消息编码为另一种

| MessageToMessageEncoder API                                  | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| encode(<br/>ChannelHandlerContext ctx,<br/>I msg,<br/>List<Object> out) | 这是你需要实现的唯一方法。每个通过 write()方法写入的消息都将会被传递给 encode()方法，以编码为一个或者多个出站消息。随后，这些出站消息将会被转发给 ChannelPipeline中的下一个 ChannelOutboundHandler |

![å¾ 10-4 IntegerToStringEncoder.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%2010-4%20IntegerToStringEncoder.png?raw=true)

```java
/**
 * 代码清单 10-6 IntegerToStringEncoder 类
 *
 * 扩展了 MessageToMessageEncoder
 */
public class IntegerToStringEncoder
    extends MessageToMessageEncoder<Integer> {
    @Override
    public void encode(ChannelHandlerContext ctx, Integer msg,
        List<Object> out) throws Exception {
        //将 Integer 转换为 String，并将其添加到 List 中
        out.add(String.valueOf(msg));
    }
}
```

ProtobufEncoder类处理了由 Google 的 Protocol Buffers 规范所定义的数据格式

## 10.4 抽象的编解码器类

### 10.4.1 抽象类 ByteToMessageCodec

​	将字节解码为某种形式的消息，随后再次对它进行编码。ByteToMessageCodec结合了ByteToMessageDecoder 以及它的逆向——MessageToByteEncoder

| ByteToMessageCodec API                                       | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| decode(<br/>ChannelHandlerContext ctx,<br/>ByteBuf in,<br/>List<Object>) | 只要有字节可以被消费，这个方法就将会被调用。它将入站ByteBuf 转换为指定的消息格式，并将其转发给ChannelPipeline 中的下一个 ChannelInboundHandler |
| decodeLast(<br/>ChannelHandlerContext ctx,<br/>ByteBuf in,<br/>List<Object> out) | 这个方法的默认实现委托给了 decode()方法。它只会在Channel 的状态变为非活动时被调用一次。它可以被重写以实现特殊的处理 |
| encode(<br/>ChannelHandlerContext ctx,<br/>I msg,<br/>ByteBuf out) | 对于每个将被编码并写入出站 ByteBuf 的（类型为 I 的）消息来说，这个方法都将会被调用 |

### 10.4.2 抽象类 MessageToMessageCodec

​	单个类中实现将一种消息格式转换为另外一种消息格式

| MessageToMessageCodec 的方法                                 |                                                              |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| protected abstract decode(<br/>ChannelHandlerContext ctx,<br/>INBOUND_IN msg,<br/>List<Object> out) | 这个方法被调用时会被传入 INBOUND_IN 类型的消息。它将把它们解码为 OUTBOUND_IN 类型的消息，这些消息将被转发给 ChannelPipeline 中的下一个 ChannelInboundHandler |
| protected abstract encode(<br/>ChannelHandlerContext ctx,<br/>OUTBOUND_IN msg,<br/>List<Object> out) | 对于每个 OUTBOUND_IN 类型的消息，这个方法都将会被调用。这些消息将会被编码为 INBOUND_IN 类型的消息，然后被转发给 ChannelPipeline 中的下一个ChannelOutboundHandler |

```java
/**
 * 代码清单 10-7 使用 MessageToMessageCodec
 *
 */
@Sharable
public class WebSocketConvertHandler extends
     MessageToMessageCodec<WebSocketFrame,
     WebSocketConvertHandler.MyWebSocketFrame> {
     @Override
     //将 MyWebSocketFrame 编码为指定的 WebSocketFrame 子类型
     protected void encode(ChannelHandlerContext ctx,
         WebSocketConvertHandler.MyWebSocketFrame msg,
         List<Object> out) throws Exception {
         ByteBuf payload = msg.getData().duplicate().retain();
         //实例化一个指定子类型的 WebSocketFrame
         switch (msg.getType()) {
             case BINARY:
                 out.add(new BinaryWebSocketFrame(payload));
                 break;
             case TEXT:
                 out.add(new TextWebSocketFrame(payload));
                 break;
             case CLOSE:
                 out.add(new CloseWebSocketFrame(true, 0, payload));
                 break;
             case CONTINUATION:
                 out.add(new ContinuationWebSocketFrame(payload));
                 break;
             case PONG:
                 out.add(new PongWebSocketFrame(payload));
                 break;
             case PING:
                 out.add(new PingWebSocketFrame(payload));
                 break;
             default:
                 throw new IllegalStateException(
                     "Unsupported websocket msg " + msg);}
    }

    @Override
    //将 WebSocketFrame 解码为 MyWebSocketFrame，并设置 FrameType
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg,
        List<Object> out) throws Exception {
        ByteBuf payload = msg.content().duplicate().retain();
        if (msg instanceof BinaryWebSocketFrame) {
            out.add(new MyWebSocketFrame(
                    MyWebSocketFrame.FrameType.BINARY, payload));
        } else
        if (msg instanceof CloseWebSocketFrame) {
            out.add(new MyWebSocketFrame (
                    MyWebSocketFrame.FrameType.CLOSE, payload));
        } else
        if (msg instanceof PingWebSocketFrame) {
            out.add(new MyWebSocketFrame (
                    MyWebSocketFrame.FrameType.PING, payload));
        } else
        if (msg instanceof PongWebSocketFrame) {
            out.add(new MyWebSocketFrame (
                    MyWebSocketFrame.FrameType.PONG, payload));
        } else
        if (msg instanceof TextWebSocketFrame) {
            out.add(new MyWebSocketFrame (
                    MyWebSocketFrame.FrameType.TEXT, payload));
        } else
        if (msg instanceof ContinuationWebSocketFrame) {
            out.add(new MyWebSocketFrame (
                    MyWebSocketFrame.FrameType.CONTINUATION, payload));
        } else
        {
            throw new IllegalStateException(
                    "Unsupported websocket msg " + msg);
        }
    }

    //声明 WebSocketConvertHandler 所使用的 OUTBOUND_IN 类型
    public static final class MyWebSocketFrame {
        //定义拥有被包装的有效负载的 WebSocketFrame 的类型
        public enum FrameType {
            BINARY,
            CLOSE,
            PING,
            PONG,
            TEXT,
            CONTINUATION
        }
        private final FrameType type;
        private final ByteBuf data;

        public MyWebSocketFrame(FrameType type, ByteBuf data) {
            this.type = type;
            this.data = data;
        }
        public FrameType getType() {
            return type;
        }
        public ByteBuf getData() {
            return data;
        }
    }
}
```

### 10.4.3 CombinedChannelDuplexHandler 类

```java
//结合一个解码器和编码器,分别继承了解码器类和编码器类的类型
public class CombinedChannelDuplexHandler
<I extends ChannelInboundHandler,
O extends ChannelOutboundHandler>
```

```java
/**
 * 代码清单 10-8 ByteToCharDecoder 类
 *
 */
public class ByteToCharDecoder extends ByteToMessageDecoder {
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)  {
        if (in.readableBytes() >= 2) {
            //将一个或者多个 Character 对象添加到传出的 List 中
            out.add(in.readChar());
        }
    }
}
/**
 * 代码清单 9 CharToByteEncoder 类
 * 能将 Character 转换回字节,
 */
public class CharToByteEncoder extends MessageToByteEncoder<Character> {
    @Override
    public void encode(ChannelHandlerContext ctx, Character msg, ByteBuf out)  {
        //将 Character 解码为 char，并将其写入到出站 ByteBuf 中
        out.writeChar(msg);
    }
}
/**
 * 代码清单 CombinedChannelDuplexHandler<I,O>
 *
 * 通过该解码器和编码器实现参数化 CombinedByteCharCodec
 */
//
public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {
    public CombinedByteCharCodec() {
        //将委托实例传递给父类
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }
}
```

# 第 11 章 预置的ChannelHandler和编解码器

## 11.1 通过 SSL/TLS 保护 Netty 应用程序

​	为了支持 SSL/TLS，Java 提供了 javax.net.ssl 包，它的 SSLContext 和 SSLEngine类使得实现解密和加密相当简单直接。Netty 通过一个名为 SslHandler 的 ChannelHandler实现利用了这个 API，其中 SslHandler 在内部使用 SSLEngine 来完成实际的工作。

​	Netty 还提供了使用 OpenSSL 工具包包（www.openssl.org）的 SSLEngine 实现。比 JDK 提供的 SSLEngine 实现更好的性能。如果OpenSSL库可用，可以将Netty应用程序（客户端和服务器）配置为默认使用OpenSslEngine。如果不可用，Netty 将会回退到 JDK 实现。

![å¾ 11-1 éè¿ SslHandler è¿è¡è§£å¯åå å¯çæ°æ®æµ.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%2011-1%20%E9%80%9A%E8%BF%87%20SslHandler%20%E8%BF%9B%E8%A1%8C%E8%A7%A3%E5%AF%86%E5%92%8C%E5%8A%A0%E5%AF%86%E7%9A%84%E6%95%B0%E6%8D%AE%E6%B5%81.png?raw=true)

```java
/**
 * 代码清单 11-1 添加 SSL/TLS 支持
 * 使用ChannelInitializer来将SslHandler添加到ChannelPipeline
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean startTls;

    /**
     * 传入要使用的 SslContext
     * 如果设置为 true，第一个写入的消息将不会被加密（客户端应该设置为 true）
     * @param context 要使用的 SslContext
     * @param startTls 第一个消息是否加密
     */
    public SslChannelInitializer(SslContext context, boolean startTls) {
        this.context = context;
        this.startTls = startTls;
    }
    @Override
    protected void initChannel(Channel ch) throws Exception {
        //对于每个 SslHandler 实例，都使用 Channel 的 ByteBufAllocator 从 SslContext 获取一个新的 SSLEngine
        SSLEngine engine = context.newEngine(ch.alloc());
        //将 SslHandler 作为第一个 ChannelHandler 添加到 ChannelPipeline 中
        ch.pipeline().addFirst("ssl",
            new SslHandler(engine, startTls));
    }
}
```

在大多数情况下，SslHandler 将是 ChannelPipeline 中的第一个 ChannelHandler。这确保了只有在所有其他的 ChannelHandler 将它们的逻辑应用到数据之后，才会进行加密。

| SslHandler 的方法                                            | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| setHandshakeTimeout (long,TimeUnit)<br/>setHandshakeTimeoutMillis (long)<br/>getHandshakeTimeoutMillis() | 设置和获取超时时间，超时之后，握手ChannelFuture 将会被通知失败 |
| setCloseNotifyTimeout (long,TimeUnit)<br/>setCloseNotifyTimeoutMillis (long)<br/>getCloseNotifyTimeoutMillis() | 设置和获取超时时间，超时之后，将会触发一个关闭通知并关闭连接。这也将会导致通知该 ChannelFuture 失败 |
| handshakeFuture()                                            | 返回一个在握手完成后将会得到通知的ChannelFuture。如果握手先前已经执行过了，则返回一个包含了先前的握手结果的 ChannelFuture |
| close()<br/>close(ChannelPromise)<br/>close(ChannelHandlerContext,ChannelPromise) | 发送 close_notify 以请求关闭并销毁<br/>底层的 SslEngine      |

## 11.2 构建基于 Netty 的 HTTP/HTTPS 应用程序

### 11.2.1 HTTP 解码器、编码器和编解码器

生产和消费 HTTP 请求和 HTTP 响应的方法

![å¾ 11-2 HTTP è¯·æ±çç»æé¨å.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%2011-2%20HTTP%20%E8%AF%B7%E6%B1%82%E7%9A%84%E7%BB%84%E6%88%90%E9%83%A8%E5%88%86.png?raw=true)

![å¾ 11-3 HTTP ååºçç»æé¨å.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%2011-3%20HTTP%20%E5%93%8D%E5%BA%94%E7%9A%84%E7%BB%84%E6%88%90%E9%83%A8%E5%88%86.png?raw=true)

一个 HTTP 请求/响应可能由多个数据部分组成，并且它总是以一个 LastHttpContent 部分作为结束。FullHttpRequest 和 FullHttpResponse 消息是特殊的子类型，分别代表了完整的请求和响应。所有类型的 HTTP 消息都实现了 HttpObject 接口。

| HTTP 解码器和编码器 | 描述                                                         |
| ------------------- | ------------------------------------------------------------ |
| HttpRequestEncoder  | 将HttpRequest、HttpContent 和 LastHttpContent 消息编码为字节 |
| HttpResponseEncoder | 将HttpResponse、HttpContent 和LastHttpContent 消息编码为字节 |
| HttpRequestDecoder  | 将字节解码为HttpRequest、HttpContent 和 LastHttpContent 消息 |
| HttpResponseDecoder | 将字节解码为HttpResponse、HttpContent 和LastHttpContent 消息 |

```java
/**
 * 代码清单 11-2 添加 HTTP 支持
 * 只需要将正确的 ChannelHandler 添加到 ChannelPipeline 中
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HttpPipelineInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            //如果是客户端，则添加 HttpResponseDecoder 以处理来自服务器的响应
            pipeline.addLast("decoder", new HttpResponseDecoder());
            //如果是客户端，则添加 HttpRequestEncoder 以向服务器发送请求
            pipeline.addLast("encoder", new HttpRequestEncoder());
        } else {
            //如果是服务器，则添加 HttpRequestDecoder 以接收来自客户端的请求
            pipeline.addLast("decoder", new HttpRequestDecoder());
            //如果是服务器，则添加 HttpResponseEncoder 以向客户端发送响应
            pipeline.addLast("encoder", new HttpResponseEncoder());
        }
    }
}
```

### 11.2.2 聚合 HTTP 消息

​	由于 HTTP 的请求和响应可能由许多部分组成，因此你需要聚合它们以形成完整的消息。Netty 提供了一个聚合器，它可以将多个消息部分合并为 FullHttpRequest 或者 FullHttpResponse 消息.

```java
/**
 * 代码清单 11-3 自动聚合 HTTP 的消息片段
 * 只需要 ChannelPipeline 中添加另外一个 ChannelHandler
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {
    private final boolean isClient;

    public HttpAggregatorInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (isClient) {
            //如果是客户端，则添加 HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            //如果是服务器，则添加 HttpServerCodec
            pipeline.addLast("codec", new HttpServerCodec());
        }
        //将最大的消息大小为 512 KB 的 HttpObjectAggregator 添加到 ChannelPipeline
        pipeline.addLast("aggregator",
                new HttpObjectAggregator(512 * 1024));
    }
}
```

### 11.2.3 HTTP压缩

​	当使用 HTTP 时，建议开启压缩功能以尽可能多地减小传输数据的大小。虽然压缩会带来一些 CPU 时钟周期上的开销Netty 为压缩和解压缩提供了 ChannelHandler 实现，它们同时支持 gzip 和 deflate 编码。

```java
/**
 * 代码清单 11-4 自动压缩 HTTP 消息
 *
 */
public class HttpCompressionInitializer extends ChannelInitializer<Channel> {
    private final boolean isClient;

    public HttpCompressionInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (isClient) {
            //如果是客户端，则添加 HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());
            //如果是客户端，则添加 HttpContentDecompressor 以处理来自服务器的压缩内容
            pipeline.addLast("decompressor", new HttpContentDecompressor());
        } else {
            //如果是服务器，则添加 HttpServerCodec
            pipeline.addLast("codec", new HttpServerCodec());
            //如果是服务器，则添加HttpContentCompressor 来压缩数据（如果客户端支持它）
            pipeline.addLast("compressor", new HttpContentCompressor());
        }
    }
}
```

### 11.2.4 使用 HTTPS

```java
/**
 * 代码清单 11-5 使用 HTTPS
 * 启用 HTTPS 只需要将 SslHandler 添加到 ChannelPipeline 的ChannelHandler组合中
 */
public class HttpsCodecInitializer extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean isClient;

    public HttpsCodecInitializer(SslContext context, boolean isClient) {
        this.context = context;
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine engine = context.newEngine(ch.alloc());
        //将 SslHandler 添加到ChannelPipeline 中以使用 HTTPS
        pipeline.addFirst("ssl", new SslHandler(engine));

        if (isClient) {
            //如果是客户端，则添加 HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            //如果是服务器，则添加 HttpServerCodec
            pipeline.addLast("codec", new HttpServerCodec());
        }
    }
}
```

​	Netty 的架构方式是如何将代码重用变为杠杆作用的?只需要简单地将一个 ChannelHandler 添加到 ChannelPipeline 中，便可以提供一项新功能。

### 11.2.5 WebSocket

​	WebSocket解决了一个长期存在的问题：既然底层的协议（HTTP）是一个请求/响应模式的交互序列，那么如何实时地发布信息呢？WebSocket提供了“在一个单个的TCP连接上提供双向的通信……结合WebSocket API……它为网页和远程服务器之间的双向通信提供了一种替代HTTP轮询的方案。

![å¾ 11-4 WebSocket åè®®.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%2011-4%20WebSocket%20%E5%8D%8F%E8%AE%AE.png?raw=true)

| WebSocketFrame 类型        |                                                              |
| -------------------------- | ------------------------------------------------------------ |
| BinaryWebSocketFrame       | 数据帧：二进制数据                                           |
| TextWebSocketFrame         | 数据帧：文本数据                                             |
| ContinuationWebSocketFrame | 数据帧：属于上一个 BinaryWebSocketFrame 或者 TextWebSocketFrame 的文本的或者二进制数据 |
| CloseWebSocketFrame        | 控制帧：一个 CLOSE 请求、关闭的状态码以及关闭的原因          |
| PingWebSocketFrame         | 控制帧：请求一个 PongWebSocketFrame                          |
| PongWebSocketFrame         | 控制帧：对 PingWebSocketFrame 请求的响应                     |

```java
/**
 * 代码清单 11-6 在服务器端支持 WebSocket
 * 这个类处理协议升级握手，以及 3 种控制帧——Close、Ping和Pong。
 * Text和Binary数据帧将会被传递给下一个（由你实现的）ChannelHandler进行处理
 */
public class WebSocketServerInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(
            new HttpServerCodec(),
            //为握手提供聚合的 HttpRequest
            new HttpObjectAggregator(65536),
            //如果被请求的端点是"/websocket"，则处理该升级握手
            new WebSocketServerProtocolHandler("/websocket"),
            //TextFrameHandler 处理 TextWebSocketFrame
            new TextFrameHandler(),
            //BinaryFrameHandler 处理 BinaryWebSocketFrame
            new BinaryFrameHandler(),
            //ContinuationFrameHandler 处理 ContinuationWebSocketFrame
            new ContinuationFrameHandler());
    }

    public static final class TextFrameHandler extends
        SimpleChannelInboundHandler<TextWebSocketFrame> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx,
            TextWebSocketFrame msg) throws Exception {
            // Handle text frame
        }
    }

    public static final class BinaryFrameHandler extends
        SimpleChannelInboundHandler<BinaryWebSocketFrame> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx,
            BinaryWebSocketFrame msg) throws Exception {
            // Handle binary frame
        }
    }

    public static final class ContinuationFrameHandler extends
        SimpleChannelInboundHandler<ContinuationWebSocketFrame> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx,
            ContinuationWebSocketFrame msg) throws Exception {
            // Handle continuation frame
        }
    }
}
```

要想为 WebSocket 添加安全性，只需要将 SslHandler 作为第一个 ChannelHandler 添加到
ChannelPipeline 中

## 11.3 空闲的连接和超时

| 用于空闲连接以及超时的 ChannelHandler |                                                              |
| ------------------------------------- | ------------------------------------------------------------ |
| IdleStateHandler                      | 当连接空闲时间太长时，将会触发一个 IdleStateEvent 事件。然后，你可以通过在你的 ChannelInboundHandler 中重写 userEventTriggered()方法来处理该 IdleStateEvent 事件 |
| ReadTimeoutHandler                    | 如果在指定的时间间隔内没有收到任何的入站数据，则抛出一个 ReadTimeoutException 并关闭对应的 Channel。可以通过重写你的ChannelHandler 中的 exceptionCaught()方法来检测该 ReadTimeoutException |
| WriteTimeoutHandler                   | 如果在指定的时间间隔内没有任何出站数据写入，则抛出一个 WriteTimeoutException 并关闭对应的 Channel 。可以通过重写你的ChannelHandler 的 exceptionCaught()方法检测该 WriteTimeoutException |

```java
/**
 * 代码清单 11-7 发送心跳
 *
 */
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel>
    {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(
                //(1) IdleStateHandler 将在被触发时发送一个IdleStateEvent 事件
                new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));
        //将一个 HeartbeatHandler 添加到ChannelPipeline中
        pipeline.addLast(new HeartbeatHandler());
    }

    //实现 userEventTriggered() 方法以发送心跳消息
    public static final class HeartbeatHandler
        extends ChannelInboundHandlerAdapter {
        //发送到远程节点的心跳消息
        private static final ByteBuf HEARTBEAT_SEQUENCE =
                Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
                "HEARTBEAT", CharsetUtil.ISO_8859_1));
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx,
            Object evt) throws Exception {
            //(2) 发送心跳消息，并在发送失败时关闭该连接
            if (evt instanceof IdleStateEvent) {
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate())
                     .addListener(
                         ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                //不是 IdleStateEvent 事件，所以将它传递给下一个 ChannelInboundHandler
                super.userEventTriggered(ctx, evt);
            }
        }
    }
}
```

## 11.4 解码基于分隔符的协议和基于长度的协议

### 11.4.1 基于分隔符的协议

​	基于分隔符的（delimited）消息协议使用定义的字符来标记的消息或者消息段（通常被称为帧）的开头或者结尾。

| 用于处理基于分隔符的协议和基于长度的协议的解码器 |                                                              |
| ------------------------------------------------ | ------------------------------------------------------------ |
| DelimiterBasedFrameDecoder                       | 使用任何由用户提供的分隔符来提取帧的通用解码器               |
| LineBasedFrameDecoder                            | 提取由行尾符（\n 或者\r\n）分隔的帧的解码器。这个解码器比 DelimiterBasedFrameDecoder 更快 |

![å¾ 11-5 ç±è¡å°¾ç¬¦åéçå¸§.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%2011-5%20%E7%94%B1%E8%A1%8C%E5%B0%BE%E7%AC%A6%E5%88%86%E9%9A%94%E7%9A%84%E5%B8%A7.png?raw=true)

```java
/**
 * 代码清单 11-8 处理由行尾符分隔的帧
 *
 */
public class LineBasedHandlerInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //该 LineBasedFrameDecoder 将提取的帧转发给下一个 ChannelInboundHandler
        pipeline.addLast(new LineBasedFrameDecoder(64 * 1024));
        //添加 FrameHandler 以接收帧
        pipeline.addLast(new FrameHandler());
    }

    public static final class FrameHandler
        extends SimpleChannelInboundHandler<ByteBuf> {
        @Override
        //传入了单个帧的内容
        public void channelRead0(ChannelHandlerContext ctx,
            ByteBuf msg) throws Exception {
            // Do something with the data extracted from the frame
        }
    }
}
```

使用 DelimiterBasedFrameDecoder，只需要将特定的分隔符序列指定到其构造函数即可.

比如协议规范：

1. 传入数据流是一系列的帧，每个帧都由换行符（\n）分隔；
2. 每个帧都由一系列的元素组成，每个元素都由单个空格字符分隔；
3. 一个帧的内容代表一个命令，定义为一个命令名称后跟着数目可变的参数。
   1. Cmd—将帧（命令）的内容存储在 ByteBuf 中，一个 ByteBuf 用于名称，另一个用于参数；
   2. CmdDecoder—从被重写了的 decode()方法中获取一行字符串，并从它的内容构建一个 Cmd 的实例；
   3. CmdHandler—从 CmdDecoder 获取解码的 Cmd 对象，并对它进行一些处理；
   4. CmdHandlerInitializer把前面的这些类定义为专门的 ChannelInitializer 的嵌套类其将会把这些 ChannelInboundHandler 安装到 ChannelPipeline 中。

```java
/**
 * 代码清单 11-9 使用 ChannelInitializer 安装解码器
 *
 */
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {
    private static final byte SPACE = (byte)' ';
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //添加 CmdDecoder 以提取 Cmd 对象，并将它转发给下一个 ChannelInboundHandler
        pipeline.addLast(new CmdDecoder(64 * 1024));
        //添加 CmdHandler 以接收和处理 Cmd 对象
        pipeline.addLast(new CmdHandler());
    }

    //Cmd POJO
    public static final class Cmd {
        private final ByteBuf name;
        private final ByteBuf args;

        public Cmd(ByteBuf name, ByteBuf args) {
            this.name = name;
            this.args = args;
        }

        public ByteBuf name() {
            return name;
        }

        public ByteBuf args() {
            return args;
        }
    }

    public static final class CmdDecoder extends LineBasedFrameDecoder {
        public CmdDecoder(int maxLength) {
            super(maxLength);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer)
            throws Exception {
            //从 ByteBuf 中提取由行尾符序列分隔的帧
            ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
            if (frame == null) {
                //如果输入中没有帧，则返回 null

                return null;
            }
            //查找第一个空格字符的索引。前面是命令名称，接着是参数
            int index = frame.indexOf(frame.readerIndex(),
                    frame.writerIndex(), SPACE);
            //使用包含有命令名称和参数的切片创建新的 Cmd 对象
            return new Cmd(frame.slice(frame.readerIndex(), index),
                    frame.slice(index + 1, frame.writerIndex()));
        }
    }

    public static final class CmdHandler
        extends SimpleChannelInboundHandler<Cmd> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, Cmd msg)
            throws Exception {
            // Do something with the command
            //处理传经 ChannelPipeline 的 Cmd 对象
        }
    }
}
```

### 11.4.2 基于长度的协议

​	基于长度的协议通过将它的长度编码到帧的头部来定义帧，而不是使用特殊的分隔符来标记它的结束.

| 用于基于长度的协议的解码器   |                                                              |
| ---------------------------- | ------------------------------------------------------------ |
| FixedLengthFrameDecoder      | 提取在调用构造函数时指定的定长帧                             |
| LengthFieldBasedFrameDecoder | 根据编码进帧头部中的长度值提取帧；该字段的偏移量以及长度在构造函数中指定 |

FixedLengthFrameDecoder 的功能，其在构造时已经指定了帧长度为 8字节

![å¾ 11-6 è§£ç é¿åº¦ä¸º 8 å­èçå¸§.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%2011-6%20%E8%A7%A3%E7%A0%81%E9%95%BF%E5%BA%A6%E4%B8%BA%208%20%E5%AD%97%E8%8A%82%E7%9A%84%E5%B8%A7.png?raw=true)

LengthFieldBasedFrameDecoder处理变长字节

![å¾ 11-7 å°åé¿å¸§å¤§å°ç¼ç è¿å¤´é¨çæ¶æ¯.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%2011-7%20%E5%B0%86%E5%8F%98%E9%95%BF%E5%B8%A7%E5%A4%A7%E5%B0%8F%E7%BC%96%E7%A0%81%E8%BF%9B%E5%A4%B4%E9%83%A8%E7%9A%84%E6%B6%88%E6%81%AF.png?raw=true)

```java
/**
 * 代码清单 11-10 使用 LengthFieldBasedFrameDecoder 解码器基于长度的协议
 *
 */
public class LengthBasedInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(
                //使用 LengthFieldBasedFrameDecoder 解码将帧长度编码到帧起始的前 8 个字节中的消息
                new LengthFieldBasedFrameDecoder(64 * 1024, 0, 8));
        //添加 FrameHandler 以处理每个帧
        pipeline.addLast(new FrameHandler());
    }

    public static final class FrameHandler
        extends SimpleChannelInboundHandler<ByteBuf> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx,
             ByteBuf msg) throws Exception {
            // Do something with the frame
            //处理帧的数据
        }
    }
}
```

## 11.5 写大型数据

```java
/**
 * 代码清单 11-11 使用 FileRegion 传输文件的内容
 * 零拷贝消除了将文件的内容从文件系统移动到网络栈的复制过程
 * 使用一个 FileRegion 接口的实现
 */
public class FileRegionWriteHandler extends ChannelInboundHandlerAdapter {
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
    private static final File FILE_FROM_SOMEWHERE = new File("");

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        File file = FILE_FROM_SOMEWHERE;
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        //...
        //创建一个 FileInputStream
        FileInputStream in = new FileInputStream(file);
        //以该文件的完整长度创建一个新的 DefaultFileRegion
        FileRegion region = new DefaultFileRegion(in.getChannel(), 0, file.length());
        //发送该 DefaultFileRegion，并注册一个 ChannelFutureListener
        channel
                .writeAndFlush(region).
                addListener((ChannelFuture future)->{
                    if (!future.isSuccess()) {
                        //处理失败
                        Throwable cause = future.cause();
                        // Do something
                    }
                });
    }
}
```

​	需要将数据从文件系统复制到用户内存中时，可以使用 ChunkedWriteHandler，它支持异步写大型数据流，而又不会导致大量的内存消耗。

​	interface ChunkedInput<B>，其中类型参数 B 是 readChunk()方法返回的类型。Netty 预置了该接口的 4 个实现，如表 11-7 中所列出的。每个都代表了一个将由 ChunkedWriteHandler 处理的不定长度的数据流。

​	

| ChunkedInput 的实现 |                                                              |
| ------------------- | ------------------------------------------------------------ |
| ChunkedFile         | 从文件中逐块获取数据，当你的平台不支持零拷贝或者你需要转换数据时使用 |
| ChunkedNioFile      | 和 ChunkedFile 类似，只是它使用了 FileChannel                |
| ChunkedStream       | 从 InputStream 中逐块传输内容                                |
| ChunkedNioStream    | 从 ReadableByteChannel 中逐块传输内容                        |

```java
/**
 * 代码清单 11-12 使用 ChunkedStream 传输文件内容
 *
 */
public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {
    private final File file;
    private final SslContext sslCtx;
    public ChunkedWriteHandlerInitializer(File file, SslContext sslCtx) {
        this.file = file;
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //将 SslHandler 添加到 ChannelPipeline 中
        pipeline.addLast(new SslHandler(sslCtx.newEngine(ch.alloc())));
        //添加 ChunkedWriteHandler 以处理作为 ChunkedInput 传入的数据
        pipeline.addLast(new ChunkedWriteHandler());
        //一旦连接建立，WriteStreamHandler 就开始写文件数据
        pipeline.addLast(new WriteStreamHandler());
    }

    public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {

        @Override
        //当连接建立时，channelActive() 方法将使用 ChunkedInput 写文件数据
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            ctx.writeAndFlush(
            new ChunkedStream(new FileInputStream(file)));
        }
    }
}
```

## 11.6 序列化数据

​	JDK 提供了 ObjectOutputStream 和 ObjectInputStream，用于通过网络对 POJO 的基本数据类型和图进行序列化和反序列化。可以被应用于任何实现了java.io.Serializable 接口的对象。但是它的性能也不是非常高效的。

### 11.6.1 JDK 序列化

| JDK 序列化编解码器      |                                                              |
| ----------------------- | ------------------------------------------------------------ |
| CompatibleObjectEncoder | 和使用 JDK 序列化的非基于 Netty 的远程节点进行互操作的编码器 |
| ObjectDecoder           | 构建于 JDK 序列化之上的使用自定义的序列化来解码的解码器；当没有其他的外部依赖时，它提供了速度上的改进。否则其他的序列化实现更加可取 |
| ObjectEncoder           | 构建于 JDK 序列化之上的使用自定义的序列化来编码的编码器；当没有其他的外部依赖时，它提供了速度上的改进。否则其他的序列化实现更加可取 |

### 11.6.2 使用 JBoss Marshalling 进行序列化

​	比JDK序列化最多快 3 倍，而且也更加紧凑。

| JBoss Marshalling 编解码器                                   |                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------- |
| CompatibleMarshallingDecoder<br/>CompatibleMarshallingEncoder | 与只使用 JDK 序列化的远程节点兼容                       |
| MarshallingDecoder<br/>MarshallingEncoder                    | 适用于使用 JBoss Marshalling 的节点。这些类必须一起使用 |

```java
/**
 * 代码清单 11-13 使用 JBoss Marshalling
 *
 */
public class MarshallingInitializer extends ChannelInitializer<Channel> {
    private final MarshallerProvider marshallerProvider;
    private final UnmarshallerProvider unmarshallerProvider;

    public MarshallingInitializer(UnmarshallerProvider unmarshallerProvider, MarshallerProvider marshallerProvider) {
        this.marshallerProvider = marshallerProvider;
        this.unmarshallerProvider = unmarshallerProvider;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //添加 MarshallingDecoder 以将 ByteBuf 转换为 POJO
        pipeline.addLast(new MarshallingDecoder(unmarshallerProvider));
        //添加 MarshallingEncoder 以将POJO 转换为 ByteBuf
        pipeline.addLast(new MarshallingEncoder(marshallerProvider));
        //添加 ObjectHandler，以处理普通的实现了Serializable 接口的 POJO
        pipeline.addLast(new ObjectHandler());
    }

    public static final class ObjectHandler
        extends SimpleChannelInboundHandler<Serializable> {
        @Override
        public void channelRead0(
            ChannelHandlerContext channelHandlerContext,
            Serializable serializable) throws Exception {
            // Do something
        }
    }
}
```

### 11.6.3 通过 Protocol Buffers 序列化

| Protobuf 编解码器                    |                                                              |
| ------------------------------------ | ------------------------------------------------------------ |
| ProtobufDecoder                      | 使用 protobuf 对消息进行解码                                 |
| ProtobufEncoder                      | 使用 protobuf 对消息进行编码                                 |
| ProtobufVarint32FrameDecoder         | 根据消息中的 Google Protocol Buffers 的“Base 128 Varints”整型长度字段值动态地分割所接收到的 ByteBuf |
| ProtobufVarint32LengthFieldPrepender | 向 ByteBuf 前追加一个 Google Protocal Buffers 的“Base 128 Varints”整型的长度字段值 |

```java
/**
 * 代码清单 11-14 使用 protobuf
 * 只需要将将正确的 ChannelHandler 添加到 ChannelPipeline 中
 */
public class ProtoBufInitializer extends ChannelInitializer<Channel> {
    private final MessageLite lite;

    public ProtoBufInitializer(MessageLite lite) {
        this.lite = lite;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //添加 ProtobufVarint32FrameDecoder 以分隔帧
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        //添加 ProtobufEncoder 以处理消息的编码
        pipeline.addLast(new ProtobufEncoder());
        //添加 ProtobufDecoder 以解码消息
        pipeline.addLast(new ProtobufDecoder(lite));
        //添加 ObjectHandler 以处理解码消息
        pipeline.addLast(new ObjectHandler());
    }

    public static final class ObjectHandler extends SimpleChannelInboundHandler<Object> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {
            // Do something with the object
        }
    }
}
```

# 第三部分 网络协议

# 第 12 章 WebSocket

### 12.1 WebSocket 简介

​	WebSocket 协议是完全重新设计的协议，旨在为 Web 上的双向数据传输问题提供一个切实可行的解决方案，使得客户端和服务器之间可以在任意时刻传输消息，因此，这也就要求它们异步地处理消息回执。

## 12.2 我们的 WebSocket 示例应用程序

1. 客户端发送一个消息；
2. 该消息将被广播到所有其他连接的客户端。

![å¾ 12-1 WebSocket åºç¨ç¨åºé»è¾.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%2012-1%20WebSocket%20%E5%BA%94%E7%94%A8%E7%A8%8B%E5%BA%8F%E9%80%BB%E8%BE%91.png?raw=true)

## 12.3 添加 WebSocket 支持

​	在从标准的HTTP或者HTTPS协议切换到WebSocket时，将会使用一种称为升级握手 的机制。因此，使用WebSocket的应用程序将始终以HTTP/S作为开始，然后再执行升级。这个升级动作发生的确切时刻特定于应用程序。它可能会发生在启动时，也可能会发生在请求了某个特定的URL之后。

​	我们的应用程序将采用下面的约定：如果被请求的 URL 以/ws 结尾，那么我们将会把该协议升级为 WebSocket；否则，服务器将使用基本的 HTTP/S。在连接已经升级完成之后，所有数据都将会使用 WebSocket 进行传输。	

![å¾ 12-2 æå¡å¨é»è¾.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%2012-2%20%E6%9C%8D%E5%8A%A1%E5%99%A8%E9%80%BB%E8%BE%91.png?raw=true)

### 12.3.1 处理 HTTP 请求

```java
/**
 * 代码清单 12-1 HTTPRequestHandler
 *
 * 扩展 SimpleChannelInboundHandler 以处理 FullHttpRequest 消息
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class
             .getProtectionDomain()
             .getCodeSource().getLocation();
        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate index.html", e);
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        //(1) 如果请求了 WebSocket 协议升级，则增加引用计数（调用 retain()方法），并将它传递给下一 个 ChannelInboundHandler
        if (wsUri.equalsIgnoreCase(request.uri())) {
            ctx.fireChannelRead(request.retain());
        } else {
            //(2) 处理 100 Continue 请求以符合 HTTP 1.1 规范
            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
            //读取 index.html
            RandomAccessFile file = new RandomAccessFile(INDEX, "r");
            HttpResponse response = new DefaultHttpResponse(
                request.protocolVersion(), HttpResponseStatus.OK);
            response.headers().set(
                    HttpHeaderNames.CONTENT_TYPE,
                "text/html; charset=UTF-8");
            boolean keepAlive = HttpUtil.isKeepAlive(request);
            //如果请求了keep-alive，则添加所需要的 HTTP 头信息
            if (keepAlive) {
                response.headers().set(
                        HttpHeaderNames.CONTENT_LENGTH, file.length());
                response.headers().set( HttpHeaderNames.CONNECTION,
                        HttpHeaderValues.KEEP_ALIVE);
            }
            //(3) 将 HttpResponse 写到客户端
            ctx.write(response);
            //(4) 将 index.html 写到客户端
            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.write(new DefaultFileRegion(
                    file.getChannel(), 0, file.length()));
            } else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }
            //(5) 写 LastHttpContent 并冲刷至客户端
            ChannelFuture future = ctx.writeAndFlush(
                LastHttpContent.EMPTY_LAST_CONTENT);
            //(6) 如果没有请求keep-alive，则在写操作完成后关闭 Channel
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

```

### 12.3.2 处理 WebSocket 帧

| WebSocketFrame 的类型      |                                                              |
| -------------------------- | ------------------------------------------------------------ |
| BinaryWebSocketFrame       | 包含了二进制数据                                             |
| TextWebSocketFrame         | 包含了文本数据                                               |
| ContinuationWebSocketFrame | 包含属于上一个BinaryWebSocketFrame或TextWebSocketFrame 的文本数据或者二进制数据 |
| CloseWebSocketFrame        | 表示一个 CLOSE 请求，包含一个关闭的状态码和关闭的原因        |
| PingWebSocketFrame         | 请求传输一个 PongWebSocketFrame                              |
| PongWebSocketFrame         | 作为一个对于 PingWebSocketFrame 的响应被发送                 |

```java
/**
 * 代码清单 12-2 处理文本帧
 *
 * 扩展 SimpleChannelInboundHandler，并处理 TextWebSocketFrame 消息
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    //重写 userEventTriggered()方法以处理自定义事件
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //如果该事件表示握手成功，则从该 ChannelPipeline 中移除HttpRequest-Handler，因为将不会接收到任何HTTP消息了
        if (evt == WebSocketServerProtocolHandler
             .ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            ctx.pipeline().remove(HttpRequestHandler.class);
            //(1) 通知所有已经连接的 WebSocket 客户端新的客户端已经连接上了
            group.writeAndFlush(new TextWebSocketFrame(
                    "Client " + ctx.channel() + " joined"));
            //(2) 将新的 WebSocket Channel 添加到 ChannelGroup 中，以便它可以接收到所有的消息
            group.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //(3) 增加消息的引用计数，并将它写到 ChannelGroup 中所有已经连接的客户端
        group.writeAndFlush(msg.retain());
    }
}
```

### 12.3.3 初始化 ChannelPipeline

```java
/**
 * 代码清单 12-3 初始化 ChannelPipeline
 *
 */
public class ChatServerInitializer extends ChannelInitializer<Channel> {
    private final ChannelGroup group;

    public ChatServerInitializer(ChannelGroup group) {
        this.group = group;
    }

    @Override
    //将所有需要的 ChannelHandler 添加到 ChannelPipeline 中
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        pipeline.addLast(new HttpRequestHandler("/ws"));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new TextWebSocketFrameHandler(group));
    }
}
```

| 基于 WebSocket 聊天服务器的 ChannelHandler |                                                              |
| ------------------------------------------ | ------------------------------------------------------------ |
| HttpServerCodec                            | 将字节解码为 HttpRequest、HttpContent 和 LastHttpContent。并将 HttpRequest、HttpContent 和 LastHttpContent 编码为字节 |
| ChunkedWriteHandler                        | 写入一个文件的内容                                           |
| HttpObjectAggregator                       | 将一个 HttpMessage 和跟随它的多个 HttpContent 聚合为单个 FullHttpRequest 或者 FullHttpResponse（取决于它是被用来处理请求还是响应）。安装了这个之后，ChannelPipeline 中的下一个 ChannelHandler 将只会收到完整的 HTTP 请求或响应 |
| HttpRequestHandler                         | 处理 FullHttpRequest（那些不发送到/ws URI 的请求）           |
| WebSocketServerProtocolHandler             | 按照 WebSocket 规范的要求，处理 WebSocket 升级握手、PingWebSocketFrame 、 PongWebSocketFrame 和CloseWebSocketFrame |
| TextWebSocketFrameHandler                  | 处理 TextWebSocketFrame 和握手完成事件                       |

Netty 的 WebSocketServerProtocolHandler 处理了所有委托管理的 WebSocket 帧类型以及升级握手本身。如果握手成功，那么所需的 ChannelHandler 将会被添加到 ChannelPipeline中，而那些不再需要的 ChannelHandler 则将会被移除。

升级之前的 ChannelPipeline 的状态,代表了刚刚被ChatServerInitializer 初始化之后的 ChannelPipeline。

![å¾ 12-3 WebSocket åè®®åçº§ä¹åç ChannelPipeline.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%2012-3%20WebSocket%20%E5%8D%8F%E8%AE%AE%E5%8D%87%E7%BA%A7%E4%B9%8B%E5%89%8D%E7%9A%84%20ChannelPipeline.png?raw=true)

​	当 WebSocket 协议升级完成之后，WebSocketServerProtocolHandler 将会把 HttpRequestDecoder 替换为 WebSocketFrameDecoder，把 HttpResponseEncoder 替换为WebSocketFrameEncoder。为了性能最大化，它将移除任何不再被 WebSocket 连接所需要的ChannelHandler。

Netty目前支持 4个版本的WebSocket协议，它们每个都具有自己的实现类。Netty将会根据客户端所支持的版本自动地选择正确版本的WebSocketFrameDecoder和WebSocketFrameEncoder

![å¾ 12-4 WebSocket åè®®åçº§å®æä¹åç ChannelPipeline.png](https://github.com/jzz-12-6/image/blob/master/netty-in-action/%E5%9B%BE%2012-4%20WebSocket%20%E5%8D%8F%E8%AE%AE%E5%8D%87%E7%BA%A7%E5%AE%8C%E6%88%90%E4%B9%8B%E5%90%8E%E7%9A%84%20ChannelPipeline.png?raw=true)

### 12.3.4 引导

