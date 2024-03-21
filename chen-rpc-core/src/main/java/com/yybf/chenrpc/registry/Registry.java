package com.yybf.chenrpc.registry;

import com.yybf.chenrpc.config.RegistryConfig;
import com.yybf.chenrpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心接口
 *
 * @author yangyibufeng
 * @date 2024/3/20-18:22
 */
public interface Registry {

    /**
     * 初始化
     *
     * @param registryConfig:
     * @return void:
     * @author yangyibufeng
     * @date 2024/3/20 18:24
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务（服务端）
     *
     * @param serviceMetaInfo:
     * @return void:
     * @author yangyibufeng
     * @date 2024/3/20 18:25
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务（服务端）
     *
     * @param serviceMetaInfo:
     * @return void:
     * @author yangyibufeng
     * @date 2024/3/20 18:26
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现（获取某服务的所有节点，消费端）
     *
     * @param serviceKey:
     * @return java.util.List<com.yybf.chenrpc.model.ServiceMetaInfo>:
     * @author yangyibufeng
     * @date 2024/3/20 18:27
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 服务销毁
     *
     * @return void:
     * @author yangyibufeng
     * @date 2024/3/20 18:27
     */
    void destroy();

    /**
     * 心跳检测（服务端）
     *
     * @return void:
     * @author yangyibufeng
     * @date 2024/3/21 21:03
     */
    void heartBeat();


}
