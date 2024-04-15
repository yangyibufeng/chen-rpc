package com.yybf.chenrpc.loadbalancer;

import com.yybf.chenrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载查询器
 *
 * @author yangyibufeng
 * @date 2024/4/10
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    /**
     * 当前轮询的服务下标
     * （使用 JUC 包的 AtomicInteger 实现原子计数器，防止并发冲突问题。）
     */
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        int size = serviceMetaInfoList.size();
        if(size == 0) {
            return null;
        }

        // 只有一个服务，无需轮询
        if(size == 1){
            return serviceMetaInfoList.get(0);
        }

        // 取模算法轮询 （getAndIncrement() 获取当前值，然后加一）
        int index = currentIndex.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);
    }
}