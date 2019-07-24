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

| 方法名       | 描述                            |
| ------------ | ------------------------------- |
| id           | 返回全局唯一标识符              |
| eventLoop    | 返回分配给 Channel 的 EventLoop |
| parent       | 返回上一层Channel               |
| config       | 返回配置                        |
| isOpen       | 是否打开状态                    |
| isRegistered | 是否注册到EventLoop             |
| isActive     | 是否激活状态                    |
|              |                                 |

