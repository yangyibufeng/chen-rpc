package com.yybf.chenrpc.bootstrap;

import com.yybf.chenrpc.RpcApplication;
import com.yybf.chenrpc.config.RegistryConfig;
import com.yybf.chenrpc.config.RpcConfig;
import com.yybf.chenrpc.model.ServiceMetaInfo;
import com.yybf.chenrpc.registry.LocalRegistry;
import com.yybf.chenrpc.registry.Registry;
import com.yybf.chenrpc.registry.RegistryFactory;
import com.yybf.chenrpc.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * 服务提供者的启动类
 *
 * @author yangyibufeng
 * @date 2024/4/18
 */
public class ProviderBootstrap {
    /**
     * 初始化
     *  （与com.yybf.example.provider.ProviderExample中的代码基本一致）
     * @param serviceRegisterInfoList: 传入的需要进行注册的服务列表
     * @return void:
     * @author yangyibufeng
     * @date 2024/4/18 21:32
     */
    public static void init(List<ServiceRegisterInfo> serviceRegisterInfoList) {
        RpcApplication.init();
        // 加载全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList){
            String serviceName = serviceRegisterInfo.getServiceName();

            // 本地注册
            LocalRegistry.register(serviceName,serviceRegisterInfo.getImplClass());

            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());

            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // 启动TCP服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getServerPort());
    }
}