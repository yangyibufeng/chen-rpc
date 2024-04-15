package com.yybf.chenrpc.loadbalancer;

import com.yybf.chenrpc.spi.SpiLoader;

/**
 * 负载均衡器工厂类（工厂模式，用于获取负载均衡器的实例对象）
 *
 * @author yangyibufeng
 * @date 2024/4/15
 */
public class LoadBalancerFactory {
    static {
        // 使用SPI加载器，扫描加载实现的负载均衡器
        SpiLoader.load(LoadBalancer.class);
    }

    /**
     * 默认的负载均衡器
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    /**
     * 获取指定的负载均衡器的实例
     *
     * @param key:
     * @return com.yybf.chenrpc.loadbalancer.LoadBalancer:
     * @author yangyibufeng
     * @date 2024/4/15 17:25
     */
    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}