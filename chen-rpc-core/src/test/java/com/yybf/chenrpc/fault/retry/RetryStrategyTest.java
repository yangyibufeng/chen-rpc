package com.yybf.chenrpc.fault.retry;

import com.yybf.chenrpc.model.RpcResponse;
import org.junit.Test;

/**
 * 测试重试策略
 *
 * @author yangyibufeng
 * @Description
 * @date 2024/4/17-16:42
 */
public class RetryStrategyTest {

    RetryStrategy retryStrategy1 = new NoRetryStrategy();
    RetryStrategy retryStrategy2 = new FixedIntervalRetryStrategy();

    @Test
    public void doRetry() {
        try {
            RpcResponse rpcResponse = retryStrategy2.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("运行时异常，模拟重试失败");
            });
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("重试多次失败");
            e.printStackTrace();
        }
    }
}