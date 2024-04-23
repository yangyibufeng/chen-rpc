package com.yybf.chenrpc.springboot.starter.bootstrap;

import com.yybf.chenrpc.RpcApplication;
import com.yybf.chenrpc.config.RegistryConfig;
import com.yybf.chenrpc.config.RpcConfig;
import com.yybf.chenrpc.model.ServiceMetaInfo;
import com.yybf.chenrpc.registry.LocalRegistry;
import com.yybf.chenrpc.registry.Registry;
import com.yybf.chenrpc.registry.RegistryFactory;
import com.yybf.chenrpc.springboot.starter.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Rpc 服务提供者启动
 *
 * @author yangyibufeng
 * @date 2024/4/20
 */
public class RpcProviderBootStrap implements BeanPostProcessor {
    /**
     * Bean初始化后执行，注册服务
     * 它会检查被初始化的 Bean 是否带有 @RpcService 注解，
     * 如果有的话，就会将该服务注册到本地和远程注册中心。
     *
     * @param bean:     被初始化的 bean 的名称
     * @param beanName: 该 bean 在 spring 容器中的名称
     * @return java.lang.Object:
     * @author yangyibufeng
     * @date 2024/4/22 10:39
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 获取 Bean 对象的 Class 对象
        Class<?> beanClass = bean.getClass();
        // 通过反射获取该 Bean 类上的‘@RpcService’注解
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);

        if (rpcService != null) { // 如果该类上面有注解
            // 获取自定义的注释中的接口属性
            Class<?> interfaceClass = rpcService.interfaceClass();

            if (interfaceClass == void.class) { // 如果没有显式的指明，就会返回默认的void
                // 如果没有显式的指明，那么就使用实现接口中的第一个
                interfaceClass = beanClass.getInterfaces()[0];
            }

            // 从接口中获取服务名称，从注解中获取版本号
            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();

            // 本地注册
            LocalRegistry.register(serviceName, beanClass);

            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());

            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + "服务注册失败" + e);
            }

        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}