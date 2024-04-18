package com.yybf.chenrpc.fault.tolerant;

import com.yybf.chenrpc.model.RpcResponse;

import java.util.Map;

/**
 * RPC框架容错策略
 *
 * @author yangyibufeng
 * @date 2024/4/18
 */
public interface TolerantStrategy {
    /**
     * 容错
     *
     * @param context: 上下文，用于传递数据
     * @param e: 接收到的 报错 或 异常
     * @return com.yybf.chenrpc.model.RpcResponse:
     * @author yangyibufeng
     * @date 2024/4/18 17:00
     */
    RpcResponse doTolerant(Map<String, Object> context, Exception e);
}