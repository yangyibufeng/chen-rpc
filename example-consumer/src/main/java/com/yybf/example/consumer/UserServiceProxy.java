package com.yybf.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.yybf.chenrpc.model.RpcRequest;
import com.yybf.chenrpc.model.RpcResponse;
import com.yybf.chenrpc.serializer.JdkSerializer;
import com.yybf.chenrpc.serializer.Serializer;
import com.yybf.example.common.model.User;
import com.yybf.example.common.service.UserService;

import java.io.IOException;

/**
 * 实现静态代理
 *
 * @author yangyibufeng
 * @date 2024/3/13
 */
public class UserServiceProxy implements UserService {

    @Override
    public User getUser(User user) {
        // 指定序列化器
        Serializer serializer = new JdkSerializer();

        /* 发请求 */
        // 构建请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();

        try {
            // 将请求序列化
            byte[] bodyBytes = serializer.serializer(rpcRequest);
            byte[] result = null;
            // 发送请求
            try(HttpResponse httpResponse = HttpRequest.post("http://localhost:18080").body(bodyBytes).execute()){
                result = httpResponse.bodyBytes();
            }
            // 将响应反序列化
            RpcResponse rpcResponse = serializer.deserializer(result, RpcResponse.class);
            // 将获取到的结果返回
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}