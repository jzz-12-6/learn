package chapter4;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * 代码清单 4-1 未使用 Netty 的阻塞网络编程
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class PlainOioServer {
    public void serve(int port) throws IOException {
        //将服务器绑定到指定端口
        try (ServerSocket socket = new ServerSocket(port)){
            while (true){
                //接受连接
                final Socket clientSocket = socket.accept();
                System.out.println(
                        "Accepted connection from " + clientSocket);
                new Thread(()->{
                    try ( OutputStream out = clientSocket.getOutputStream()){
                        out.write("Hi!\r\n".getBytes(
                                Charset.forName("UTF-8")));
                        out.flush();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }
}
