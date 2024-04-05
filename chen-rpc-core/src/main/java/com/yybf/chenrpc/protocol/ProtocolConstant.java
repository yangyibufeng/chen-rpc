package com.yybf.chenrpc.protocol;

/**
 * 与协议有关的一些常量
 *
 * @author yangyibufeng
 * @date 2024/4/5
 */
public interface ProtocolConstant {
    /**
     * 消息头长度
     */
    int MESSAGE_HEADER_LENGTH = 17;

    /**
     * 协议魔数
     */
    byte PROTOCOL_MAGIC = 0x1;

    /**
     * 协议版本号
     */
    byte PROTOCOL_VERSION = 0x1;
}