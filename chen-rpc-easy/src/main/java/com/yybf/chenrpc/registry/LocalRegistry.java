package com.yybf.chenrpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地服务注册器
 *
 * @author yangyibufeng
 * @date 2024/3/12
 */
public class LocalRegistry {
    /**
     * 注册信息存储 用线程安全的 ConcurrentHashMap存储
     */
    public static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 注册服务
     */
    public static void register(String serviceName, Class<?> implClass) {
        map.put(serviceName, implClass);
    }

    /**
     * 获取服务
     */
    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    /**
     * 删除服务
     */
    public static void remove(String serviceName) {
        map.remove(serviceName);
    }

}