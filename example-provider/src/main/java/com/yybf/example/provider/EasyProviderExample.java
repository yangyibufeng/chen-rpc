package com.yybf.example.provider;

import com.yybf.chenrpc.registry.LocalRegistry;
import com.yybf.chenrpc.server.HttpServer;
import com.yybf.chenrpc.server.VertxHttpServer;
import com.yybf.example.common.service.UserService;

/**
 * 简易服务提供者示例
 *
 * @author yangyibufeng
 * @date 2024/3/11
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        // 提供服务

        //注册服务 直接通过方法名调用静态方法
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);


        // 启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(18080);

    }

}