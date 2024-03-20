package com.yybf.chenrpc.registry;

import com.yybf.chenrpc.config.RegistryConfig;
import com.yybf.chenrpc.model.ServiceMetaInfo;
import com.yybf.chenrpc.utils.ConfigUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * 对EtcdRegistry实现类进行测试
 *
 * @author yangyibufeng
 * @Description
 * @date 2024/3/20-20:43
 */
public class RegistryTest {

    // 创建一个注册中心
    final Registry registry = new EtcdRegistry();

    @Before
    public void Init() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("http://localhost:2379");
        registry.init(registryConfig);
    }

    @Test
    public void Register() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("chenService");
        serviceMetaInfo.setServiceVersion("1.0.1");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1145);
        registry.register(serviceMetaInfo);
        serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("chenService");
        serviceMetaInfo.setServiceVersion("1.0.1");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1146);
        registry.register(serviceMetaInfo);
        serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("chenService");
        serviceMetaInfo.setServiceVersion("1.1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1145);
        registry.register(serviceMetaInfo);
    }

    @Test
    public void UnRegister() {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("chenService");
        serviceMetaInfo.setServiceVersion("1.0.1");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1145);
        registry.unRegister(serviceMetaInfo);
    }

    @Test
    public void ServiceDiscovery() {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("chenService");
        serviceMetaInfo.setServiceVersion("1.0.1");
        String serviceKey = serviceMetaInfo.getServiceKey();
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceKey);
        System.out.println(serviceMetaInfoList);
        Assert.assertNotNull(serviceMetaInfoList);

    }
}