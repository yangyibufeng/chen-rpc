package com.yybf.chenrpc.server;

/**
 * @author yangyibufeng
 * @Description HTTP服务器接口
 * @date 2024/3/12-14:02
 */
public interface HttpServer {
    /**
     * 启动服务器
     *
     * @param port:
     * @return void:
     * @author yangyibufeng
     * @description
     * @date 2024/3/12 14:03
     */
    void doStart(int port);
}
