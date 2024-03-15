package com.yybf.example.provider;

import com.yybf.chenrpc.RpcApplication;
import com.yybf.chenrpc.registry.LocalRegistry;
import com.yybf.chenrpc.server.HttpServer;
import com.yybf.chenrpc.server.VertxHttpServer;
import com.yybf.example.common.service.UserService;

/**
 * 简易服务提供者示例
 * 测试全局配置对象加载
 *
 * @author yangyibufeng
 * @date 2024/3/15
 */
public class ProviderExample {
    public static void main(String[] args) {
        // RPC框架初始化
        RpcApplication.init();

        // 注册服务
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        // 启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());


    }
}