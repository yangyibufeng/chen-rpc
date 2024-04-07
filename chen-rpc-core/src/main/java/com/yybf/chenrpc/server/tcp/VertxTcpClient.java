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

    public void start() {
        // 创建 Vertx 实例
        Vertx vertx = Vertx.vertx();

        vertx.createNetClient().connect(8888, "localhost", result -> {
            if (result.succeeded()) {
                System.out.println("Connect to TCP server");
                NetSocket socket = result.result();
                // 发送数据
                socket.write("hello server!");
                // 接收响应
                socket.handler(buffer -> {
                    System.out.println("Received response from server: " + buffer.toString());
                });
            } else {
                System.out.println("Failed to connect to TCP server");
            }
        });
    }

    /**
     * 通过发送多组信息（1000次）来触发粘包和半包的问题
     *
     * @return void:
     * @author yangyibufeng
     * @date 2024/4/7 21:59
     */
    public void testTCPClient() {
        Vertx vertx = Vertx.vertx();
        vertx.createNetClient().connect(8888, "localhost", result -> {
            if (result.succeeded()) {
                System.out.println("Connect to TCP server");
                NetSocket socket = result.result();

                for (int i = 0; i < 1000; i++) {
                    // 发送数据
                    socket.write("Hello server!Hello server!Hello server!");
                }

                //接收数据
                socket.handler(buffer -> {
                    System.out.println("Received response from server: " + buffer.toString());
                });

            } else {
                System.out.println("Failed to connect to TCP server");
            }
        });
    }

    public static void main(String[] args) {
//        new VertxTcpClient().start();
        new VertxTcpClient().testTCPClient();
    }
}