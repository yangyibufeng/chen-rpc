package com.yybf.chenrpc.serializer;

import com.yybf.chenrpc.spi.SpiLoader;

/**
 * 序列化器工厂（用于获取序列化器对象）
 * 使用SPI加载序列化器
 * 修改通过懒加载的方式创建序列化器
 *
 * @author yangyibufeng
 * @date 2024/3/17
 */
public class SerializerFactoryDL {
    // 单例实例
    private static SerializerFactoryDL instance;

    // 是否已经初始化
    private static volatile boolean initialized = false;

    // 锁对象
    private static final Object lock = new Object();

    // 私有构造函数，防止外部实例化
    private SerializerFactoryDL() {
        // 防止通过反射方式创建实例
        if (instance != null) {
            throw new IllegalStateException("Already initialized.");
        }
    }

    /**
     * 获取单例实例
     *
     * @return SerializerFactory 单例实例
     */
    public static SerializerFactoryDL getInstance() {
        if (!initialized) { // 如果还未初始化
            synchronized (lock) {
                if (!initialized) {
                    instance = new SerializerFactoryDL();
                    SpiLoader.load(Serializer.class); // 加载序列化器
                    initialized = true;
                }
            }
        }
        return instance;
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