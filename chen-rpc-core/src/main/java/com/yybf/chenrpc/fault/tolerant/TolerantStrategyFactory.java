package com.yybf.chenrpc.fault.tolerant;

import com.yybf.chenrpc.spi.SpiLoader;

/**
 * 容错策略工厂（用于获取容错策略对象实例）
 *
 * @author yangyibufeng
 * @date 2024/4/18
 */
public class TolerantStrategyFactory {
    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    /**
     * 默认容错策略 -- 静默
     */
    private static final TolerantStrategy DEFAULT_TOLERANT_STRATEGY = new FailSafeTolerantStrategy();

    /**
     * 获取指定的容错策略对象的实例
     *
     * @param key: 容错策略的键名
     * @return com.yybf.chenrpc.fault.tolerant.TolerantStrategy:
     * @author yangyibufeng
     * @date 2024/4/18 17:21
     */
    public static TolerantStrategy getInstance(String key) {
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }

}