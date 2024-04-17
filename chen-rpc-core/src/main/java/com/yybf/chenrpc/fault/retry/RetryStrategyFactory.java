package com.yybf.chenrpc.fault.retry;

import com.yybf.chenrpc.spi.SpiLoader;

/**
 * 重试策略工厂模式（用于获取重试器对象）
 *
 * @author yangyibufeng
 * @date 2024/4/17
 */
public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 获取重试器实例
     *
     * @param key:
     * @return com.yybf.chenrpc.fault.retry.RetryStrategy:
     * @author yangyibufeng
     * @date 2024/4/17 17:17
     */
    public static RetryStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }
}