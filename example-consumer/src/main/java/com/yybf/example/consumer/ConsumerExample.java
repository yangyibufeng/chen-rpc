package com.yybf.example.consumer;

import cn.hutool.core.collection.SpliteratorUtil;
import com.yybf.chenrpc.config.RpcConfig;
import com.yybf.chenrpc.utils.ConfigUtil;

/**
 * 扩展RPC后示例消费者类，测试配置文件的读取
 *
 * @author yangyibufeng
 * @date 2024/3/15
 */
public class ConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpcConfig = ConfigUtil.loadConfig(RpcConfig.class,"rpc");
        System.out.println(rpcConfig);
    }
}