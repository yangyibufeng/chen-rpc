package com.yybf.chenrpc.protocol;

import lombok.Getter;

/**
 * 协议消息状态枚举类
 *
 * @author yangyibufeng
 * @date 2024/4/5
 */
@Getter
public enum ProtocolMessageStatusEnum {

    OK("ok", 20), // 成功
    BAD_REQUEST("badRequest", 40), // 请求失败
    BAD_RESPONSE("badResponse", 50); // 响应失败

    private final String text;

    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取枚举值
     *
     * @param value:
     * @return com.yybf.chenrpc.protocol.ProtocolMessageStatusEnum:
     * @author yangyibufeng
     * @date 2024/4/5 22:25
     */
    public static ProtocolMessageStatusEnum getEnumByValue(int value) {
        for (ProtocolMessageStatusEnum anEnum : ProtocolMessageStatusEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }
}
