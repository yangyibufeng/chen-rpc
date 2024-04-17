package com.yybf.chenrpc.fault.retry;

import com.yybf.chenrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略
 *
 * @author yangyibufeng
 * @date 2024/4/17
 */
public interface RetryStrategy {
    /**
     * 重试
     *
     * @param callable: 代表需要进行重试的任务
     * @return com.yybf.chenrpc.model.RpcResponse:
     * @author yangyibufeng
     * @date 2024/4/17 16:30
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}