package com.yybf.chenrpc.serializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yybf.chenrpc.model.RpcRequest;
import com.yybf.chenrpc.model.RpcResponse;

import java.io.IOException;

/**
 * Json序列化器
 *
 * @author yangyibufeng
 * @date 2024/3/17
 */
public class JsonSerializer implements Serializer {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 序列化对象为byte[]数组
     *
     * @param object 需要序列化的对象
     * @return 序列化后的byte[]数组
     * @throws IOException 如果序列化过程中出现问题
     */
    @Override
    public <T> byte[] serializer(T object) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }

    /**
     * 反序列化byte[]数组为对象
     *
     * @param bytes 需要反序列化的数组
     * @param type  反序列化的目标类型
     * @return 反序列化后的对象
     * @throws IOException 如果反序列化过程中出现问题
     */
    @Override
    public <T> T deserializer(byte[] bytes, Class<T> type) throws IOException {
        T object = OBJECT_MAPPER.readValue(bytes, type);
        if (object instanceof RpcRequest) {
            return handleRequest((RpcRequest) object, type);
        } else if (object instanceof RpcResponse) {
            return handleResponse((RpcResponse) object, type);
        }

        return object;
    }

    /**
     * 由于 Object 的原始对象会被擦除，
     * 导致反序列化时会被作为 LinkedHashMap 无法转换成原始对象，因此这里做了特殊处理
     *
     * @param rpcRequest: rpc 请求
     * @param type:       反序列化的目标类型
     * @return T: 反序列化后的对象
     * @author yangyibufeng
     * @date 2024/3/17 19:03
     */
    private <T> T handleRequest(RpcRequest rpcRequest, Class<T> type) throws IOException {
        // 获取参数类型
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        // 获取具体参数
        Object[] args = rpcRequest.getArgs();

        // 循环处理每个参数的类型
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> clazz = parameterTypes[i];
            // 如果类型不同，就重新处理一下类型
            // 如果获取到的参数的类型与预期的类型不同
            if (!clazz.isAssignableFrom(args[i].getClass())) {
                // 将不匹配的参数序列化为字节流
                byte[] argsBytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                // 再根据原始的参数类型（clazz）反序列化回正确的Java对象
                args[i] = OBJECT_MAPPER.readValue(argsBytes, clazz);
            }
        }
        // 将整个RpcRequest对象强制转换为调用者期望的类型T
        return type.cast(rpcRequest);
    }

    /**
     * 由于 Object 的原始对象会被擦除，
     * 导致反序列化时会被作为 LinkedHashMap 无法转换成原始对象，因此这里做了特殊处理
     *
     * @param rpcResponse: rpc 响应
     * @param type:        反序列化的目标类型
     * @return T: 反序列化后的对象
     * @author yangyibufeng
     * @date 2024/3/17 19:09
     */
    private <T> T handleResponse(RpcResponse rpcResponse, Class<T> type) throws IOException {
        // 处理响应数据
        // 将响应对象中的数据（rpcResponse.getData()）序列化为字节流
        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(rpcResponse.getData());
        // 根据响应数据的实际类型（rpcResponse.getDataType()），将序列化后的字节流反序列化回原始的Java对象。
        rpcResponse.setData(OBJECT_MAPPER.readValue(dataBytes, rpcResponse.getDataType()));
        // 将整个RpcRequest对象强制转换为调用者期望的类型T
        return type.cast(rpcResponse);
    }
}