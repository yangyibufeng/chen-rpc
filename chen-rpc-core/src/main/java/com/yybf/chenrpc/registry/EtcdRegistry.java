package com.yybf.chenrpc.registry;

import cn.hutool.json.JSONUtil;
import com.yybf.chenrpc.config.RegistryConfig;
import com.yybf.chenrpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Etcd注册中心实现
 *
 * @author yangyibufeng
 * @date 2024/3/20
 */
public class EtcdRegistry implements Registry {

    private Client client;

    private KV kvClient;

    /**
     * 根节点 --  Etcd 键存储的根路径
     */
    public static final String ETCD_ROOT_PATH = "/rpc/";


    @Override
    public void init(RegistryConfig registryConfig) {
        // 创建一个端点
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout())) // 此方法以1毫秒的格式返回表示时间的Duration
                .build();
        // 创建一个kv客户端
        kvClient = client.getKVClient();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 创建一个Lease客户端
        Lease leaseClient = client.getLeaseClient();

        // 创建一个30秒的租约
        Long leaseId = leaseClient.grant(30).get().getID();

        // 设置要存储的键值对
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo),StandardCharsets.UTF_8);

        // 将键值对与租约关联起来，并设置过期时间
        PutOption putOption = PutOption.builder()
                .withLeaseId(leaseId)
                .build();
        kvClient.put(key,value,putOption).get();

    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        kvClient.delete(
                ByteSequence.from(
                        ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey()
                        ,StandardCharsets.UTF_8)
        );
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        // 根据服务名称作为前缀，从 Etcd 获取服务下的节点列表：
        // 前缀搜索，结尾要加 “ / ”
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";

        try {
            // 前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(
                    ByteSequence.from(searchPrefix,StandardCharsets.UTF_8)
                    ,getOption)
                    .get()
                    .getKvs();

            // 解析服务信息
            return keyValues.stream()
                    .map(keyValue -> {
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败", e);
        }
        return null;
    }

    @Override
    public void destroy() {
        System.out.println("当前节点下线");
        // 释放资源
        if(kvClient != null){
            kvClient.close();
        }
        if(client != null){
            client.close();
        }
    }
}