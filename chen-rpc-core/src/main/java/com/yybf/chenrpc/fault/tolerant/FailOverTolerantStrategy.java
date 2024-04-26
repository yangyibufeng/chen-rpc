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
        // 获取其他服务节点的调用方法，并尝试调用
        LoadBalancer loadBalancer = (LoadBalancer) context.get("loadBalancer");
        List<ServiceMetaInfo> serviceMetaInfoList = (List<ServiceMetaInfo>) context.get("serviceMetaInfoList");
        RpcResponse rpcResponse = null;
        if (serviceMetaInfoList != null) {
            log.info("服务列表：{}", serviceMetaInfoList);
            Map<String, Object> requestParams = (Map<String, Object>) context.get("requestParams");
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
            if (selectedServiceMetaInfo != null) {
                // 调用其他服务
                log.info("调用其他服务：{}", selectedServiceMetaInfo);
                RpcRequest rpcRequest = (RpcRequest) context.get("rpcRequest");
                RetryStrategy retryStrategy = (RetryStrategy) context.get("retryStrategy");
                try {
                    rpcResponse = retryStrategy.doRetry(() ->
                            VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
                    );
                } catch (Exception ex) {
                    throw new RuntimeException("转移服务失败" + ex);
                }
            }
        }
        return rpcResponse;
    }
}