package com.yybf.chenrpc.serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器工厂（用于获取序列化器对象）
 *
 * @author yangyibufeng
 * @date 2024/3/17
 */
public class SerializerFactoryOld1 {
    // 序列化映射（用于实现单例）
    public static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>() {{
        put(SerializerKeys.JDK, new JdkSerializer());
        put(SerializerKeys.JSON, new JsonSerializer());
        put(SerializerKeys.KRYO, new KryoSerializer());
        put(SerializerKeys.HESSIAN, new HessianSerializer());
    }};

    // 默认序列化器
    public static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get("jdk");

    /**
     * 获取实例
     *
     * @param key: 指定的序列化器
     * @return com.yybf.chenrpc.serializer.Serializer:
     * @author yangyibufeng
     * @date 2024/3/17 21:04
     */
    public static Serializer getInstance(String key) {
        // getOrDefault(key, default)如果存在key, 则返回其对应的value, 否则返回给定的默认值
        return KEY_SERIALIZER_MAP.getOrDefault(key, DEFAULT_SERIALIZER);
    }
}