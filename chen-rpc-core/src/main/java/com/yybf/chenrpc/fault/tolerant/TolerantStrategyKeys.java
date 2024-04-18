package com.yybf.chenrpc.fault.tolerant;

/**
 * RPC框架容错策略键名常量
 *
 * @author yangyibufeng
 * @date 2024/4/18
 */
public interface TolerantStrategyKeys {
    /**
     * 故障恢复
     */
    String FAIL_BACK = "failBack";

    /**
     * 快速失败
     */
    String FAIL_FAST = "failFast";

    /**
     * 故障转移
     */
    String FAIL_OVER = "failOver";

    /**
     * 静默处理
     */
    String FAIL_SAFE = "failSafe";
}