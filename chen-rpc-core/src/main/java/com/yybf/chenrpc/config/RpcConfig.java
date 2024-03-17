package com.yybf.chenrpc.config;

import com.yybf.chenrpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * 用于保存配置信息
 *
 * @author yangyibufeng
 * @date 2024/3/15
 */
@Data
public class RpcConfig {

    // 名称
    private String name = "chen-rpc";

    // 版本号
    private String version = "1.0";

    // 服务器主机名
    private String serverHost = "localHost";

    // 服务器端口号
    private Integer serverPort = 8080;

    // 是否开启模拟调用
    private boolean mock = false;

    // 指定序列化器
    private String serializer = SerializerKeys.JDK;
}