package com.yybf.chenrpc.registry;

import com.yybf.chenrpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心服务本地缓存（消费端）
 *
 * @author yangyibufeng
 * @date 2024/4/3
 */
public class RegistryServiceCache {
    /**
     * 服务缓存
     */
    List<ServiceMetaInfo> serviceCache;

    /**
     * 写缓存
     *
     * @param newServiceCache:
     * @return void:
     * @author yangyibufeng
     * @date 2024/4/3 15:12
     */
    void writeCache(List<ServiceMetaInfo> newServiceCache) {
        this.serviceCache = newServiceCache;
    }

    /**
     * 读缓存
     *
     * @return java.util.List<com.yybf.chenrpc.model.ServiceMetaInfo>:
     * @author yangyibufeng
     * @date 2024/4/3 15:13
     */
    List<ServiceMetaInfo> readCache() {
        return this.serviceCache;
    }

    /**
     * 清空缓存
     */
    void clearCache(){
        this.serviceCache = null;
    }
}