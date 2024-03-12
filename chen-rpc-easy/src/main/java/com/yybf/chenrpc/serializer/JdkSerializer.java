package com.yybf.chenrpc.serializer;

import java.io.*;

/**
 * JDK序列化器
 *
 * @author yangyibufeng
 * @date 2024/3/12
 */
public class JdkSerializer implements Serializer {

    /**
     * 序列化
     *
     * @param object:
     * @return byte[]:
     * @author yangyibufeng
     * @description
     * @date 2024/3/12 22:00
     */
    @Override
    public <T> byte[] serializer(T object) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }

    /**
     * 反序列化
     *
     * @param bytes:
     * @param type:
     * @return T:
     * @author yangyibufeng
     * @description
     * @date 2024/3/12 22:01
     */
    @Override
    public <T> T deserializer(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        try {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            objectInputStream.close();
        }
    }
}