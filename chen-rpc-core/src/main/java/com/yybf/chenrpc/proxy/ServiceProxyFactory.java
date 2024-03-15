package com.yybf.chenrpc.proxy;

import com.yybf.chenrpc.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * 创建动态代理工厂，根据指定类创建动态代理对象
 * 使用了工厂设计模式
 *
 * @author yangyibufeng
 * @date 2024/3/13
 */
public class ServiceProxyFactory {
    /**
     * 根据服务类获取代理对象
     *
     * @param serviceClass: 具体的服务类
     * @return T:
     * @author yangyibufeng
     * @date 2024/3/13 16:14
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        // 从配置文件中读取是否启用Mock代理
        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }

        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }

    /**
     * 根据服务类获取其Mock代理对象
     *
     * @param serviceClass:
     * @return T:
     * @author yangyibufeng
     * @date 2024/3/15 21:20
     */
    private static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy()
        );
    }
}