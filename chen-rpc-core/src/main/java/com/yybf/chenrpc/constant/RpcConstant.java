package com.yybf.chenrpc.constant;

/**
 * 用来配置RPC相关的常量
 *
 * @author yangyibufeng
 * @date 2024/3/15
 */
public interface RpcConstant {
    //默认配置文件加载前缀：表示只有类似 ‘rpc.xxx = xxx’ 的配置才能被加载
    String DEFAULT_CONFIG_PREFIX = "rpc";

    // 默认服务版本号
    String DEFAULT_SERVICE_VERSION = "1.0";
}