package com.yybf.chenrpc.protocol;

import com.yybf.chenrpc.model.RpcRequest;
import com.yybf.chenrpc.model.RpcResponse;
import com.yybf.chenrpc.serializer.Serializer;
import com.yybf.chenrpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

/**
 * 协议消息解码器
 * 将发送来的字节数组给解码成对应的消息类
 *
 * @author yangyibufeng
 * @date 2024/4/6
 */
public class ProtocolMessageDecoder {
    /**
     * 解码，将根据协议发送来的字节数组类型的消息数据给解码为对应的类型
     *
     * @param buffer: 发送来的字节数组类型的消息数据
     * @return com.yybf.chenrpc.protocol.ProtocolMessage<?>: 指定的协议消息包装类
     * @author yangyibufeng
     * @date 2024/4/6 13:45
     */
    public static ProtocolMessage<?> decode(Buffer buffer) throws Exception {
        // 分别从指定位置读取Buffer
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        // 获取魔数来校验消息完整性
        byte magic = buffer.getByte(0);
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            throw new RuntimeException("消息magic非法！");
        }

        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));

        // 解决粘包问题，只读取指定长度的数据
        // 17 指的是消息头的长度为17个字节
        byte[] bodyBytes = buffer.getBytes(17, 17 + header.getBodyLength());
        // 解析消息体
        // 序列化协议
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if (serializerEnum == null) {
            throw new RuntimeException("序列化消息的协议不存在！");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());

        // 消息类型
        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getEnumByKey(header.getType());
        if (messageTypeEnum == null) {
            throw new RuntimeException("序列化消息的类型不存在！");
        }
        switch (messageTypeEnum) {
            case REQUEST:
                RpcRequest request = serializer.deserializer(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header, request);
            case RESPONSE:
                RpcResponse response = serializer.deserializer(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header, response);
            case HEART_BEAT:
            case OTHERS:
            default:
                throw new RuntimeException("暂不支持该消息类型！");
        }
    }
}