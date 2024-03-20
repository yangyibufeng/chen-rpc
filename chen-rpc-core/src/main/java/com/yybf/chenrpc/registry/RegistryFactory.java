package com.yybf.chenrpc.registry;

import com.yybf.chenrpc.spi.SpiLoader;

/**
 * 注册中心工厂（用于获取注册中心对象）
 *
 * @author yangyibufeng
 * @date 2024/3/20
 */
public class RegistryFactory {
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry CEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 获取指定的注册中心实例
     *
     * @param key:
     * @return com.yybf.chenrpc.registry.Registry:
     * @author yangyibufeng
     * @date 2024/3/20 19:57
     */
    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }

}