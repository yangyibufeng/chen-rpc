package com.yybf.chenrpc.fault.tolerant;

import com.yybf.chenrpc.fault.retry.RetryStrategy;
import com.yybf.chenrpc.loadbalancer.LoadBalancer;
import com.yybf.chenrpc.model.RpcRequest;
import com.yybf.chenrpc.model.RpcResponse;
import com.yybf.chenrpc.model.ServiceMetaInfo;
import com.yybf.chenrpc.server.tcp.VertxTcpClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 转移服务
 * 在遇到异常之后，转移到其他服务节点
 *
 * @author yangyibufeng
 * @date 2024/4/18
 */
@Slf4j
public class FailOverTolerantStrategy implements TolerantStrategy {

    /**
     * 处理异常情况，并尝试通过调用其他服务节点来完成相应操作。
     *
     * @param context 包含执行上下文信息的Map，用于传递给其他服务节点以完成相应操作。
     * @param e 异常对象，表示在执行过程中遇到的错误或异常情况。
     * @return RpcResponse 对象，表示其他服务节点的响应结果。如果无法获取有效响应，则返回null。
     */
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.error("执行异常，尝试转移服务", e);

        LoadBalancer loadBalancer = getLoadBalancer(context);
        List<ServiceMetaInfo> serviceMetaInfoList = getServiceMetaInfoList(context);
        RpcResponse rpcResponse = null;

        if (serviceMetaInfoList != null) {
            log.info("服务列表：{}", serviceMetaInfoList.size()); // 避免直接输出服务详细信息
            Map<String, Object> requestParams = getRequestParams(context);
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
            if (selectedServiceMetaInfo != null) {
                log.info("调用其他服务：{}", selectedServiceMetaInfo.getServiceKey()); // 仅记录服务ID等非敏感信息
                rpcResponse = callOtherService(context, selectedServiceMetaInfo);
            }
        }

        return rpcResponse;
    }

    private LoadBalancer getLoadBalancer(Map<String, Object> context) {
        Object obj = context.get("loadBalancer");
        if (obj instanceof LoadBalancer) {
            return (LoadBalancer) obj;
        } else {
            log.error("LoadBalancer not found in context");
            throw new IllegalArgumentException("LoadBalancer not found in context");
        }
    }

    private List<ServiceMetaInfo> getServiceMetaInfoList(Map<String, Object> context) {
        Object obj = context.get("serviceMetaInfoList");
        if (obj instanceof List) {
            return (List<ServiceMetaInfo>) obj;
        } else {
            log.error("serviceMetaInfoList not found in context or is not a list");
            throw new IllegalArgumentException("serviceMetaInfoList not found in context or is not a list");
        }
    }

    private Map<String, Object> getRequestParams(Map<String, Object> context) {
        Object obj = context.get("requestParams");
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        } else {
            log.error("requestParams not found in context or is not a map");
            throw new IllegalArgumentException("requestParams not found in context or is not a map");
        }
    }

    private RpcResponse callOtherService(Map<String, Object> context, ServiceMetaInfo selectedServiceMetaInfo) {
        RpcRequest rpcRequest = getRpcRequest(context);
        RetryStrategy retryStrategy = getRetryStrategy(context);

        try {
            return retryStrategy.doRetry(() ->
                    VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
            );
        } catch (Exception ex) {
            log.error("转移服务失败", ex);
            // 这里可以根据业务需求返回一个错误的RpcResponse或者null
            return null;
        }
    }

    private RpcRequest getRpcRequest(Map<String, Object> context) {
        Object obj = context.get("rpcRequest");
        if (obj instanceof RpcRequest) {
            return (RpcRequest) obj;
        } else {
            log.error("rpcRequest not found in context");
            throw new IllegalArgumentException("rpcRequest not found in context");
        }
    }

    private RetryStrategy getRetryStrategy(Map<String, Object> context) {
        Object obj = context.get("retryStrategy");
        if (obj instanceof RetryStrategy) {
            return (RetryStrategy) obj;
        } else {
            log.error("retryStrategy not found in context");
            throw new IllegalArgumentException("retryStrategy not found in context");
        }
    }
}