package com.yybf.chenrpc.fault.tolerant;

import com.yybf.chenrpc.fault.retry.RetryStrategy;
import com.yybf.chenrpc.loadbalancer.LoadBalancer;
import com.yybf.chenrpc.model.RpcResponse;
import com.yybf.chenrpc.model.ServiceMetaInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 降级
 * 在遇到异常之后降级成其他的服务
 *
 * @author yangyibufeng
 * @date 2024/4/18
 */
@Slf4j
public class FailBackTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // 调用其他服务
        log.error("调用服务失败，降级调用其他服务");



        return null;
    }
}