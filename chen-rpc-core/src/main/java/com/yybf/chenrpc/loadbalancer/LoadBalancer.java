package com.yybf.chenrpc.loadbalancer;

import com.yybf.chenrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器（消费端使用）
 *
 * @author yangyibufeng
 * @date 2024/4/10
 */
public interface LoadBalancer {
    /**
     * 选择服务调用
     * 将请求参数和可用服务列表传入，该方法通过特定的负载均衡方法，选择一个合适的服务进行请求
     *
     * @param requestParams: 请求参数
     * @param serviceMetaInfoList: 可用的服务列表
     * @return com.yybf.chenrpc.model.ServiceMetaInfo:
     * @author yangyibufeng
     * @date 2024/4/10 11:50
     */
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
