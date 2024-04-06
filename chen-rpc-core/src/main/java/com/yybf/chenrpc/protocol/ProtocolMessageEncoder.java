package com.yybf.chenrpc.protocol;

import com.yybf.chenrpc.serializer.Serializer;
import com.yybf.chenrpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

/**
 * 消息编码器
 * 用于向Buffer缓冲区写于消息对象的字段
 *
 * @author yangyibufeng
 * @date 2024/4/6
 */
public class ProtocolMessageEncoder {

    /**
     * 编码
     * 将发送的Java对象编码为Buffer
     *
     * @author yangyibufeng
     * @date 2024/4/6 11:48
     * @param protocolMessage:
     * @return io.vertx.core.buffer.Buffer:
     */
    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws Exception{
        // 如果 消息本身 或者 消息的消息头 为空
        if(protocolMessage == null || protocolMessage.getHeader() == null){
            return Buffer.buffer();
        }
        ProtocolMessage.Header header = protocolMessage.getHeader();

        // 依次向Buffer缓冲区中写入字节
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());

        // 获取序列化器（这里是类型的自动转换）
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if(serializerEnum == null){
            throw new RuntimeException("协议指定的序列化协议不存在！");
        }

        //调用工厂类创建序列化器实例，并序列化对象为字节数组
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        byte[] bodyBytes = serializer.serializer(protocolMessage.getBody());

        // 写入body长度和数据
        buffer.appendInt(bodyBytes.length);
        buffer.appendBytes(bodyBytes);

        return buffer;
    }
}