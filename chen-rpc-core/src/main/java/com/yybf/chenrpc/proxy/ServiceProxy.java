package com.yybf.chenrpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.yybf.chenrpc.RpcApplication;
import com.yybf.chenrpc.model.RpcRequest;
import com.yybf.chenrpc.model.RpcResponse;
import com.yybf.chenrpc.serializer.JdkSerializer;
import com.yybf.chenrpc.serializer.Serializer;
import com.yybf.chenrpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

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
        //
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            // 将请求序列化
            byte[] bodyBytes = serializer.serializer(rpcRequest);
            byte[] result = null;
            // 发送请求
            // todo 这里是硬编码，应该使用注册中心和服务发现机制去解决
            try(HttpResponse httpResponse = HttpRequest.post("http://localhost:" + RpcApplication.getRpcConfig().getServerPort()).body(bodyBytes).execute()){
                result = httpResponse.bodyBytes();
            }
            // 将响应反序列化
            RpcResponse rpcResponse = serializer.deserializer(result, RpcResponse.class);
            // 将获取到的结果返回
            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}