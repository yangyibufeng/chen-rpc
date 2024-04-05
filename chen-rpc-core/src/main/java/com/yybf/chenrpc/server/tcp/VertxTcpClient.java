package com.yybf.chenrpc.server.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

/**
 * Vertx TCP 客户端实现
 *
 * @author yangyibufeng
 * @date 2024/4/5
 */
public class VertxTcpClient {

    public void start(){
        // 创建 Vertx 实例
        Vertx vertx = Vertx.vertx();

        vertx.createNetClient().connect(8888,"localhost",result ->{
            if(result.succeeded()){
                System.out.println("Connect to TCP server");
                NetSocket socket = result.result();
                // 发送数据
                socket.write("hello server!");
                // 接收响应
                socket.handler(buffer -> {
                    System.out.println("Received response from server: " + buffer.toString());
                });
            }else{
                System.out.println("Failed to connect to TCP server");
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}