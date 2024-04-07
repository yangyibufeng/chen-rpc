package com.yybf.example.provider;

import com.yybf.chenrpc.RpcApplication;
import com.yybf.chenrpc.config.RegistryConfig;
import com.yybf.chenrpc.config.RpcConfig;
import com.yybf.chenrpc.model.ServiceMetaInfo;
import com.yybf.chenrpc.registry.LocalRegistry;
import com.yybf.chenrpc.registry.Registry;
import com.yybf.chenrpc.registry.RegistryFactory;
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
public class ProviderExampleWithHttp {
    public static void main(String[] args) {
        // RPC框架初始化
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName,UserServiceImpl.class);

        // 注册服务到注册中心
        // 获取一个注册中心的实例
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        // 构建服务信息
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            // 注册
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());


    }
}