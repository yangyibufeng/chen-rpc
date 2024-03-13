package com.yybf.chenrpc.server;

import io.vertx.core.Vertx;

/**
 * Vertx HTTP 服务器
 *
 * @author yangyibufeng
 * @date 2024/3/12
 */
public class VertxHttpServer implements HttpServer {

    /**
     * 启动服务器
     *
     * @param port:
     * @return void:
     * @author yangyibufeng
     * @date 2024/3/13 15:08
     */
    @Override
    public void doStart(int port) {
        // 创建Vert.x的实例
        Vertx vertx = Vertx.vertx();

        // 创建HTTP服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 监听端口并处理请求
//        server.requestHandler(request -> {
//            // 处理HTTP请求
//            System.out.println("Received request：" + request.method() + " " + request.uri());
//
//            // 发送HTTP请求
//            request.response()
//                    .putHeader("content-type", "text/plain")
//                    .end("Hello from Vert.x HTTP server!");
//        });
        server.requestHandler(new HttpServerHandler());

        // 启动HTTP服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listening on port" + port);
            } else {
                System.err.println("Failed to start server:" + result.cause());
            }
        });


    }
}