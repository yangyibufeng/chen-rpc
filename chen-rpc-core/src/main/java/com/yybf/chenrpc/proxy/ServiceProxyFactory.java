package com.yybf.chenrpc.proxy;

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
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }
}