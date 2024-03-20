package com.yybf.chenrpc.model;

/**
 * 服务元信息（注册信息）
 *
 * @author yangyibufeng
 * @date 2024/3/20
 */
public class ServiceMetaInfo {
    // 服务名称
    private String serviceName;

    // 服务版本号
    private String serviceVersion = "1.0";

    // 服务地址
    private String serviceAddress;

    // 服务分组（待实现）
    private String serviceGroup = "default";

    /**
     * 获取服务键名
     * 格式：服务名：版本号（：服务组）
     *
     * @return java.lang.String:
     * @author yangyibufeng
     * @date 2024/3/20 16:48
     */
    public String getServiceKey() {
        // 后续可添加分组
        // return String.format("%s:%s:%s",serviceName,serviceVersion,serviceGroup);
        return String.format("%s:%s", serviceName, serviceVersion);
    }

    /**
     * 获取服务注册节点键名
     * 格式：键名/注册地址
     *
     * @return java.lang.String:
     * @author yangyibufeng
     * @date 2024/3/20 16:49
     */
    public String getServiceNodeKey() {
        return String.format("%s/%s", getServiceKey(), serviceAddress);
    }
}