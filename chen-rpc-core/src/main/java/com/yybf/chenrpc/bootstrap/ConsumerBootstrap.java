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
 * 服务消费者的启动类
 *
 * @author yangyibufeng
 * @date 2024/4/20
 */
public class ConsumerBootstrap {
    /**
     * 初始化
     */
    public static void init() {
        // RPC框架初始化（用于初始化配置和注册中心） -- 但是我感觉这一步没啥必要
        RpcApplication.init();
    }
}