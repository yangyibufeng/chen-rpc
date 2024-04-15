package com.yybf.chenrpc.loadbalancer;

import com.yybf.chenrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性哈希负载均衡器
 *
 * @author yangyibufeng
 * @date 2024/4/10
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {

    /**
     * 一致性Hash环，用来存放虚拟节点
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();
    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        int size = serviceMetaInfoList.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }

        // 构建虚拟节点环
        // 每次调用方法都会重新构建一个节点环是为了可以及时处理节点的变化
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }

        // 获取调用请求的hash值
        int hash = getHash(requestParams);
        /*这里如果是同一个请求的话，因为requestParams传入的参数是调用的函数方法名（getMethodName），
          所以对于同一个方法这里计算出的hash值总是一样的，也就会造成调用相同的方法总是会请求到相同的服务节点上面
          可以通过修改hash算法 --》 gethash 来通过其他不同的特征计算出hash值，来实现不同节点间的负载均衡*/

        // 选择最接近且大于等于调用请求的hash值的虚拟节点，如果没有就返回环首部的节点
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if(entry == null){
            entry = virtualNodes.firstEntry();
        }

        return entry.getValue();
    }

    /**
     * 一种计算给定对象的hash值的方法
     * 这里可以实现不同的Hash算法
     *
     * @param key:
     * @return int:
     * @author yangyibufeng
     * @date 2024/4/10 16:23
     */
    private int getHash(Object key) {
        return key.hashCode();
    }
}