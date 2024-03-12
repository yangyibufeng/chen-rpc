package com.yybf.chenrpc.serializer;

import java.io.IOException;

/**
 * @author yangyibufeng
 * @Description 序列化器接口
 * @date 2024/3/12-21:55
 */
public interface Serializer {
    /**
     * 序列化
     *
     * @param object: 需要序列化的对象
     * @return byte[]: 序列化成的数组
     * @author yangyibufeng
     * @description
     * @date 2024/3/12 21:57
     */
    <T> byte[] serializer(T object) throws IOException;

    /**
     * 反序列化
     *
     * @param bytes: 需要反序列化的数组
     * @param type:  需要反序列化的类型
     * @return T: 反序列化后的类
     * @author yangyibufeng
     * @description
     * @date 2024/3/12 21:57
     */
    <T> T deserializer(byte[] bytes, Class<T> type) throws IOException;
}
