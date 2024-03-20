package com.yybf.chenrpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * 服务元信息（注册信息）
 *
 * @author yangyibufeng
 * @date 2024/3/20
 */
@Data
public class ServiceMetaInfo {
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本号
     */
    private String serviceVersion = "1.0";

    /**
     * 服务域名
     */
    private String serviceHost;

    /**
     * 服务端口号
     */
    private Integer servicePort;

    /**
     * 服务分组（待实现）
     */
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
        return String.format("%s/%s:%s", getServiceKey(), serviceHost, servicePort);
    }

    /**
     * 获取完整的服务地址
     *
     * @return java.lang.String:
     * @author yangyibufeng
     * @date 2024/3/20 20:19
     */
    public String getServiceAddress() {
        if (StrUtil.contains("http", serviceHost)) {
            return String.format("http://%s:%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }
}