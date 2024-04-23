package com.yybf.chenrpc.springboot.starter.annotation;

import com.yybf.chenrpc.constant.RpcConstant;
import com.yybf.chenrpc.fault.retry.RetryStrategyKeys;
import com.yybf.chenrpc.fault.tolerant.TolerantStrategyKeys;
import com.yybf.chenrpc.loadbalancer.LoadBalancerKeys;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yangyibufeng
 * @Description 服务消费者注解（用于注入服务）
 * @date 2024/4/20-11:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Component
public @interface RpcReference {
    /**
     * 服务接口类
     */
    Class<?> interfaceClass() default void.class;
    /**
     * 服务版本
     */
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;
    /**
     * 服务负载均衡器
     */
    String loadBalancer() default LoadBalancerKeys.ROUND_ROBIN;
    /**
     * 服务重试策略
     */
    String retryStrategy() default RetryStrategyKeys.NO;
    /**
     * 服务容错策略
     */
    String tolerantStrategy() default TolerantStrategyKeys.FAIL_SAFE;
    /**
     * 模拟调用
     */
    boolean mock() default false;
}



