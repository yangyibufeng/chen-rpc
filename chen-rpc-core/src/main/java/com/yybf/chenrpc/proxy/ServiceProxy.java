package com.yybf.chenrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.yybf.chenrpc.RpcApplication;
import com.yybf.chenrpc.config.RpcConfig;
import com.yybf.chenrpc.constant.RpcConstant;
import com.yybf.chenrpc.model.RpcRequest;
import com.yybf.chenrpc.model.RpcResponse;
import com.yybf.chenrpc.model.ServiceMetaInfo;
import com.yybf.chenrpc.registry.Registry;
import com.yybf.chenrpc.registry.RegistryFactory;
import com.yybf.chenrpc.serializer.Serializer;
import com.yybf.chenrpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 动态服务代理（JDK动态代理）
 *
 * @author yangyibufeng
 * @date 2024/3/13
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理 重写了invoke方法
     *
     * @param proxy:
     * @param method:
     * @param args:
     * @return java.lang.Object:
     * @author yangyibufeng
     * @date 2024/3/13 16:01
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
//        Serializer serializer = new JdkSerializer();
        // 修改为使用工厂 + 读取配置来指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        System.out.println("序列化器为：" + serializer.toString());


        /* 发请求 */
        // 构建请求
        // 服务名称
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            // 将请求序列化
            byte[] bodyBytes = serializer.serializer(rpcRequest);
            // 发送请求
            /**
            // 这里是硬编码，应该使用注册中心和服务发现机制去解决

             try(HttpResponse httpResponse = HttpRequest.post("http://localhost:" + RpcApplication.getRpcConfig().getServerPort()).body(bodyBytes).execute()){
                result = httpResponse.bodyBytes();
            }
            // 将响应反序列化
            RpcResponse rpcResponse = serializer.deserializer(result, RpcResponse.class);
            // 将获取到的结果返回
            return rpcResponse.getData();
             */

            /* 通过使用注册中心和服务发现机制来发送请求 */
            // 获取指定的注册中心实例
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            // 开始构建服务信息用于查找对应的服务
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            // 从serviceMetaInfo中获取服务键名，然后从Etcd中查找对应的服务
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            // 如果未获取到值
            if(CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("暂无对应服务地址!");
            }

            // todo 暂时先取第一个mateInfo
            ServiceMetaInfo selectServiceMetaInfo = serviceMetaInfoList.get(0);

            // 发送请求
            try(HttpResponse httpResponse = HttpRequest.post(selectServiceMetaInfo.getServiceAddress())
                    .body(bodyBytes)
                    .execute()){
                byte[] result = httpResponse.bodyBytes();
                // 反序列化
                RpcResponse rpcResponse = serializer.deserializer(result,RpcResponse.class);
                return rpcResponse.getData();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}