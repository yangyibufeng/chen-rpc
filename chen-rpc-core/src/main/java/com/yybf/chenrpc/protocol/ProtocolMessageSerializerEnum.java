package com.yybf.chenrpc.protocol;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 协议消息的序列化器枚举类
 *
 * @author yangyibufeng
 * @date 2024/4/5
 */
@Getter
public enum ProtocolMessageSerializerEnum {

    JDK(0, "jdk"),
    JSON(1, "json"),
    KRYO(2, "kryo"),
    HESSIAN(3, "hessian");

    private final int key;

    private final String value;

    ProtocolMessageSerializerEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 从Enum自带的values（）方法中获取到指定的元素（这里是要获取到value）
     * 因为Enum自带的values（）方法是只能获取到enum数组，获取里面的元素并不方便
     *
     * @return java.util.List<java.lang.String>:
     * @author yangyibufeng
     * @date 2024/4/5 22:59
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }


    /**
     * 根据key获取枚举值
     *
     * @param key:
     * @return com.yybf.chenrpc.protocol.ProtocolMessageStatusEnum:
     * @author yangyibufeng
     * @date 2024/4/5 22:25
     */
    public static ProtocolMessageSerializerEnum getEnumByKey(int key) {
        for (ProtocolMessageSerializerEnum anEnum : ProtocolMessageSerializerEnum.values()) {
            if (anEnum.key == key) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据value获取枚举值
     *
     * @param value:
     * @return com.yybf.chenrpc.protocol.ProtocolMessageStatusEnum:
     * @author yangyibufeng
     * @date 2024/4/5 22:25
     */
    public static ProtocolMessageSerializerEnum getEnumByValue(String value) {
        for (ProtocolMessageSerializerEnum anEnum : ProtocolMessageSerializerEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
