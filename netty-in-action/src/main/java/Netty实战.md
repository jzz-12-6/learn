# netty 实战

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
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
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

### 6.1.3 ChannelInboundHandler 接口 (5.0之后合并至ChannelHandler )

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
public class DiscardHandler extends ChannelHandlerAdapter {
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
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
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
public class DiscardInboundHandler extends ChannelHandlerAdapter {
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
public class DiscardOutboundHandler extends ChannelHandlerAdapter {
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
public class SharableHandler extends ChannelHandlerAdapter {
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
public class InboundExceptionHandler extends ChannelHandlerAdapter {
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

