package com.jzz.learn.nettylearn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TestBlockingIO {
    public static void main(String[] args) throws Exception {
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
            //response = prcessRequest(request);
            //服务器的响应被发送给客户端
            //out.print(response);
        }
    }
}
