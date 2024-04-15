package com.yybf.chenrpc.loadbalancer;

import com.yybf.chenrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 随机负载均衡器
 *
 * @author yangyibufeng
 * @date 2024/4/10
 */
public class RandomLoadBalancer implements LoadBalancer{
    private final Random random = new Random();

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

        // 从总长度中取出一个随机数（范围【0，size））
        return serviceMetaInfoList.get(random.nextInt(size));
    }
}