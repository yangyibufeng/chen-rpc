package com.yybf.chenrpc.bootstrap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务注册信息类
 *
 * @author yangyibufeng
 * @date 2024/4/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRegisterInfo <T>{
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 对应的实现类
     */
    private Class<? extends T> implClass;
}