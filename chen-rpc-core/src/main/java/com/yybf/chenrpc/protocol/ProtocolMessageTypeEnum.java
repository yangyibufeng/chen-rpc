package com.yybf.chenrpc.protocol;

import lombok.Getter;

/**
 * 协议消息的类型枚举
 * 包括请求、响应、心跳、其他。
 *
 * @author yangyibufeng
 * @date 2024/4/5
 */
@Getter
public enum ProtocolMessageTypeEnum {

    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHERS(3);


    private final int key;

    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }

    /**
     * 根据value获取枚举值
     *
     * @param key:
     * @return com.yybf.chenrpc.protocol.ProtocolMessageTypeEnum:
     * @author yangyibufeng
     * @date 2024/4/5 22:25
     */
    public static ProtocolMessageTypeEnum getEnumValue(int key) {
        for (ProtocolMessageTypeEnum anEnum : ProtocolMessageTypeEnum.values()) {
            if (anEnum.key == key) {
                return anEnum;
            }
        }
        return null;
    }

}
