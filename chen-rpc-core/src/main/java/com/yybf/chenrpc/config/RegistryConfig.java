package com.yybf.chenrpc.config;

import lombok.Data;

/**
 * RPC框架注册中心配置
 *
 * @author yangyibufeng
 * @date 2024/3/20
 */
@Data
public class RegistryConfig {
    
    /**
     * 注册中心类别
     */
    private String registry = "etcd";
//    private String registry = "zookeeper"; -- Zookeeper

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";
//    private String address = "http://localhost:2181"; -- Zookeeper

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间（ms）
     */
    private Long timeout = 1000000L;

}