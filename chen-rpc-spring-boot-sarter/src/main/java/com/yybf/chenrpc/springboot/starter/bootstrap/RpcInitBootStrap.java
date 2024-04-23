package com.yybf.chenrpc.springboot.starter.bootstrap;

import com.yybf.chenrpc.RpcApplication;
import com.yybf.chenrpc.config.RpcConfig;
import com.yybf.chenrpc.server.tcp.VertxTcpServer;
import com.yybf.chenrpc.springboot.starter.annotation.EnableRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 启动 RPC 框架
 *
 * @author yangyibufeng
 * @date 2024/4/20
 */
@Slf4j
public class RpcInitBootStrap implements ImportBeanDefinitionRegistrar {
    /**
     * 在spring初始化的时执行，用于初始化RPC框架
     *
     * @param importingClassMetadata:
     * @param registry:
     * @return void:
     * @author yangyibufeng
     * @date 2024/4/20 12:08
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取EnableRpc注解中的“needServer”属性的值
        boolean needServer = (boolean) importingClassMetadata
                .getAnnotationAttributes(EnableRpc.class.getName())
                .get("needServer");

        // 初始化RPC框架 （初始化配置和注册中心）
        RpcApplication.init();

        // 获取全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 启动服务器
        if (needServer) {
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getServerPort());
        } else {
            log.info("不启动 server");
        }
    }
}