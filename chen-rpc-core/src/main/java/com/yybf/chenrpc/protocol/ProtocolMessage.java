package com.yybf.chenrpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义协议消息结构
 *
 * @author yangyibufeng
 * @date 2024/4/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {
    /**
     * 消息头
     */
    private Header header;

    /**
     * 请求体
     */
    private T body;

    /**
     * 协议消息头
     */
    @Data
    public static class Header {
        /**
         * 魔数，用来保证安全性
         */
        private byte magic;

        /**
         * 协议版本号
         */
        private byte version;

        /**
         * 序列化器
         */
        private byte serializer;

        /**
         * 消息类型（请求/响应）
         */
        private byte type;

        /**
         * 状态
         */
        private byte status;

        /**
         * 请求id
         */
        private long requestId;

        /**
         * 消息体长度
         */
        private int bodyLength;
    }

}