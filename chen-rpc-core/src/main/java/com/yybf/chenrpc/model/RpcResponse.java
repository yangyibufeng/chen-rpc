package com.yybf.chenrpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 封装调用方法得到的返回值
 *
 * @author yangyibufeng
 * @date 2024/3/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {
    // 响应数据
    private Object data;

    // 响应数据类型
    private Class<?> dataType;

    // 响应信息
    private String message;

    // 异常信息
    private Exception exception;


}