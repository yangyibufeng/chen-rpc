package com.yybf.chenrpc.loadbalancer;

import com.yybf.chenrpc.model.ServiceMetaInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负载均衡器测试
 *
 * @author yangyibufeng
 * @Description
 * @date 2024/4/15-17:52
 */
public class LoadBalancerTest {

    final LoadBalancer sameLoadBalancer = new ConsistentHashLoadBalancer();

    final LoadBalancer loadBalancer = new RoundRobinLoadBalancer();

    @Test
    public void select() {
        // 构建请求参数
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("methodName","ppap");

        // 服务列表
        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        serviceMetaInfo1.setServiceName("111");
        serviceMetaInfo1.setServiceVersion("1.0");
        serviceMetaInfo1.setServiceHost("localHost");
        serviceMetaInfo1.setServicePort(11451);
        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
        serviceMetaInfo2.setServiceName("222");
        serviceMetaInfo2.setServiceVersion("1.0");
        serviceMetaInfo2.setServiceHost("chen.com");
        serviceMetaInfo2.setServicePort(10241);
        ServiceMetaInfo serviceMetaInfo3 = new ServiceMetaInfo();
        serviceMetaInfo3.setServiceName("333");
        serviceMetaInfo3.setServiceVersion("1.0");
        serviceMetaInfo3.setServiceHost("baidu.com");
        serviceMetaInfo3.setServicePort(11344);

        List<ServiceMetaInfo> serviceMetaInfoList = Arrays.asList(serviceMetaInfo1,serviceMetaInfo2,serviceMetaInfo3);

        // 连续多次调用
        int times = 10;
        for (int i = 0; i < times; i++) {
            ServiceMetaInfo serviceMetaInfo = loadBalancer.select(requestParams,serviceMetaInfoList);
            System.out.println(serviceMetaInfo);
            Assert.assertNotNull(serviceMetaInfo);
        }
//        serviceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
//        System.out.println(serviceMetaInfo);
//        Assert.assertNotNull(serviceMetaInfo);
//        serviceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
//        System.out.println(serviceMetaInfo);
//        Assert.assertNotNull(serviceMetaInfo);
    }
}