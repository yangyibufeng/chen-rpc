package com.yybf.chenrpc.loadbalancer;

/**
 * 负载均衡器键名常量
 *
 * @author yangyibufeng
 * @date 2024/4/15
 */
public interface LoadBalancerKeys {
    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";
    /**
     * 随机
     */
    String RANDOM = "random";
    /**
     * 一致性Hash
     */
    String CONSISTENT_HASH = "consistentHash";
}
