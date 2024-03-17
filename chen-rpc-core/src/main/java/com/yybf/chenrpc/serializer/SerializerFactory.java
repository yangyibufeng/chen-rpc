package com.yybf.chenrpc.serializer;

import com.yybf.chenrpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器工厂（用于获取序列化器对象）
 *
 * @author yangyibufeng
 * @date 2024/3/17
 */
public class SerializerFactory {
    static{
        SpiLoader.load(Serializer.class);
    }

    // 默认序列化器
    public static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取实例
     *
     * @param key: 指定的序列化器
     * @return com.yybf.chenrpc.serializer.Serializer:
     * @author yangyibufeng
     * @date 2024/3/17 21:04
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class,key);
    }
}