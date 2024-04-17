package com.yybf.chenrpc.fault.retry;

import com.yybf.chenrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 不重试
 *
 * @author yangyibufeng
 * @date 2024/4/17
 */
public class NoRetryStrategy implements RetryStrategy{

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}