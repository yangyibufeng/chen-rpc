package com.yybf.chenrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.yybf.chenrpc.RpcApplication;
import com.yybf.chenrpc.config.RpcConfig;
import com.yybf.chenrpc.constant.RpcConstant;
import com.yybf.chenrpc.loadbalancer.LoadBalancer;
import com.yybf.chenrpc.loadbalancer.LoadBalancerFactory;
import com.yybf.chenrpc.model.RpcRequest;
import com.yybf.chenrpc.model.RpcResponse;
import com.yybf.chenrpc.model.ServiceMetaInfo;
import com.yybf.chenrpc.protocol.*;
import com.yybf.chenrpc.registry.Registry;
import com.yybf.chenrpc.registry.RegistryFactory;
import com.yybf.chenrpc.serializer.Serializer;
import com.yybf.chenrpc.serializer.SerializerFactory;
import com.yybf.chenrpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import org.objenesis.ObjenesisException;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 动态服务代理（JDK动态代理）
 * 使用自定义tcp协议进行重构
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
        // 读取配置列表
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        // 指定序列化器
        // 修改为使用工厂 + 读取配置来指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(rpcConfig.getSerializer());
        System.out.println("ServiceProxy：序列化器为：" + serializer.toString());


        /* 发请求 */
        // 构建请求
        // 服务名称
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .serviceVersion(RpcConstant.DEFAULT_SERVICE_VERSION)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            // 发送请求
            /* 通过使用 注册中心 和 服务发现（SPI）机制 来发送请求 */
            // 获取指定的注册中心实例
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            // 开始构建服务信息用于查找对应的服务
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            // 从serviceMetaInfo中获取服务键名，然后从Etcd中查找对应的服务
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            // 如果未获取到值
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无对应服务地址!");
            }

            // 通过使用指定的负载均衡器实现负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 将调用的 方法名 作为负载均衡参数
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName",rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams,serviceMetaInfoList);

            // 发送TCP请求
            RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo);

            System.out.println("Response：" + rpcResponse);

            return rpcResponse.getData();
            /*// 发送Tcp请求
            Vertx vertx = Vertx.vertx();
            NetClient netClient = vertx.createNetClient();
            // 使异步式的vertx通过阻塞变为同步
            CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
            netClient.connect(selectServiceMetaInfo.getServicePort(), selectServiceMetaInfo.getServiceHost(),
                    result -> {
                        if (result.succeeded()) {
                            System.out.println("Connected to TCP server!");
                            NetSocket socket = result.result();
                            // 发送数据
                            // 构造消息
                            ProtocolMessage<Object> protocolMessage = new ProtocolMessage<>();
                            ProtocolMessage.Header header = new ProtocolMessage.Header();
                            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(rpcConfig.getSerializer()).getKey());
                            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                            // 生成全局请求ID
                            header.setRequestId(IdUtil.getSnowflakeNextId());
                            protocolMessage.setHeader(header);
                            protocolMessage.setBody(rpcRequest);

                            // 编码请求
                            try {
                                Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
                                socket.write(encode);
                            } catch (Exception e) {
                                throw new RuntimeException("协议消息编码失败！ exception ：" + e);
                            }

                            // 接收响应
                            socket.handler(buffer -> {
                                try {
                                    ProtocolMessage<RpcResponse> decode = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                    responseFuture.complete(decode.getBody());
                                } catch (Exception e) {
                                    throw new RuntimeException("协议消息解码失败！ exception ：" + e);
                                }
                            });
                        } else {
                            System.out.println("Failed to connect to TCP server!");
                        }
                    });

            RpcResponse rpcResponse = responseFuture.get();
            // 关闭链接
            netClient.close();
            return rpcResponse.getData();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}