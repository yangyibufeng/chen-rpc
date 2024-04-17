package com.yybf.chenrpc.fault.retry;

import com.github.rholder.retry.*;
import com.yybf.chenrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 固定时间间隔的重试策略
 *
 * @author yangyibufeng
 * @date 2024/4/17
 */
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy {

    /**
     * 重试条件：使用 retryIfExceptionOfType 方法指定当出现 Exception 异常时重试.
     * 重试等待策略：使用 withWaitStrategy 方法指定策略，选择 fixedWait 固定时间间隔策略。
     * 重试停止策略：使用 withStopStrategy 方法指定策略，选择 stopAfterAttempt 超过最大重试次数停止。
     * 重试工作：使用 withRetryListener 监听重试，每次重试时，除了再次执行任务外，还能够打印当前的重试次数。
     *
     * @param callable:
     * @return com.yybf.chenrpc.model.RpcResponse:
     * @author yangyibufeng
     * @date 2024/4/17 16:41
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("重试次数为：{}", attempt.getAttemptNumber());
                    }
                })
                .build();
        return retryer.call(callable);
    }
}