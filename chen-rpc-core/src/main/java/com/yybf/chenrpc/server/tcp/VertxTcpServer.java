package com.yybf.chenrpc.server.tcp;

import com.yybf.chenrpc.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;

/**
 * Vertx TCP 服务器实现
 *
 * @author yangyibufeng
 * @date 2024/4/5
 */
public class VertxTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData) {
        // 在这里编写处理请求的逻辑，根据requestData的构造响应数据并返回
        // 这里只是一个示例，实际逻辑需要根据集体的业务需求来实现
        return "hello client".getBytes();
    }

    @Override
    public void doStart(int port) {
        // 创建一个Vertx实例
        Vertx vertx = Vertx.vertx();

        // 创建一个TCP服务器
        NetServer server = vertx.createNetServer();

        //处理网络请求
        /*server.connectHandler(socket -> {
            // 处理连接
           socket.handler(buffer -> {
               // 将收到的请求转化成字节数组
               byte[] requestData = buffer.getBytes();
               System.out.println("Received request from server: " + buffer);
               // 使用上面自定义的函数来处理转化成的字节数组
               byte[] responseData = handleRequest(requestData);
               // 发送响应（发送的数据格式为Buffer）
               socket.write(Buffer.buffer(responseData));
           });
        });*/

        // 使用自定义的处理器
        server.connectHandler(new TcpServerHandler());

        // 启动 TCP 服务器，并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server started on port" + port);
            } else {
                System.out.println("Failed to start TCP server: " + result.cause());
            }
        });
    }

    /**
     * 测试TCP粘包半包的服务端
     *
     * @param port:
     * @return void:
     * @author yangyibufeng
     * @date 2024/4/7 22:59
     */
    public void testTCPServer(int port) {
        // 创建一个Vertx实例
        Vertx vertx = Vertx.vertx();

        // 创建一个TCP服务器
        NetServer server = vertx.createNetServer();

        //处理网络请求
        server.connectHandler(socket -> {
            String testMessage = "Hello server!Hello server!Hello server!";
            int messageLength = testMessage.getBytes().length;

            // 构造parser
            RecordParser parser = RecordParser.newFixed(8);
            parser.setOutput(new Handler<Buffer>() {
                // 初始化
                int size = -1;
                /* 读取完整的数据头和体 */
                // 构建返回体 resultBuffer
                Buffer resultBuffer = Buffer.buffer();

                @Override
                public void handle(Buffer buffer) {

                    if (size == -1) { // 读取的是头信息
                        // 读取消息体的长度
                        size = buffer.getInt(4);
                        // 更改截取的长度
                        parser.fixedSizeMode(size);
                        // 将头信息写入结果
                        resultBuffer.appendBuffer(buffer);
                        resultBuffer.appendBytes(" -- ".getBytes());
                    } else { // 读取的是体信息
                        // 写入体信息到结果
                        resultBuffer.appendBuffer(buffer);
                        System.out.println("result：" + resultBuffer);
                        // 重置
                        parser.fixedSizeMode(8);
                        size = -1;
                        resultBuffer = Buffer.buffer();
                    }
                }
            });

            // 装配处理逻辑代码
            socket.handler(parser);
        });

        // 启动 TCP 服务器，并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server started on port" + port);
            } else {
                System.out.println("Failed to start TCP server: " + result.cause());
            }
        });

    }

    public static void main(String[] args) {
//        new VertxTcpServer().doStart(8888);
        new VertxTcpServer().testTCPServer(8888);
    }
}