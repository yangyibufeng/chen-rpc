package com.yybf.chenrpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 封装调用所需要的信息
 *
 * @author yangyibufeng
 * @date 2024/3/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {

    // 服务名称
    private String serviceName;

    // 方法名称
    private String methodName;

    // 参数类型列表
    private Class<?>[] parameterTypes;

    // 参数列表
    private Object[] args;



}