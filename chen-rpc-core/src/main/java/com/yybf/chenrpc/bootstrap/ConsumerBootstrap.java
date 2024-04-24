package com.yybf.chenrpc.bootstrap;

import com.yybf.chenrpc.RpcApplication;

/**
 * 服务消费者的启动类
 *
 * @author yangyibufeng
 * @date 2024/4/20
 */
public class ConsumerBootstrap {
    /**
     * 初始化
     */
    public static void init() {
        // RPC框架初始化（用于初始化配置和注册中心） -- 但是我感觉这一步没啥必要
        RpcApplication.init();
    }
}