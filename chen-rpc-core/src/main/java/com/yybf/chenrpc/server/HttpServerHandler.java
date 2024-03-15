package com.yybf.chenrpc.server;

import com.yybf.chenrpc.model.RpcRequest;
import com.yybf.chenrpc.model.RpcResponse;
import com.yybf.chenrpc.registry.LocalRegistry;
import com.yybf.chenrpc.serializer.JdkSerializer;
import com.yybf.chenrpc.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * HTTP请求处理
 *
 * @author yangyibufeng
 * @date 2024/3/12
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {
        // 指定序列化器
        final Serializer serializer = new JdkSerializer();

        // 记录日志
        System.out.println("Received request:" + request.method() + " /*-*/ " + request.uri());

        // 异步处理HTTP请求
        request.handler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                // 将请求反序列化为请求类
                rpcRequest = serializer.deserializer(bytes, RpcRequest.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();

            // 如果请求为null，直接返回
            if (request == null) {
                rpcResponse.setMessage("rpcRequest is null");
                // 处理响应
                doResponse(request,rpcResponse,serializer);
                return;
            }

            try {
                // 通过反射，获取要调用的服务的实现类
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                // 获得指定的方法
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                // 第一个参数：创建一个服务对象的实例；第二个参数：传入执行方法所需要的参数
                // 最后调用上面获得的方法
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());

                //封装响应返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("OK");
            } catch (Exception e) {
                e.printStackTrace();

                rpcResponse.setException(e);
                rpcResponse.setMessage(e.getMessage());
            }

            //  处理响应
            doResponse(request,rpcResponse,serializer);
        });
    }

    /**
     * 处理响应
     *
     * @param request:
     * @param rpcResponse:
     * @param serializer:
     * @return void:
     * @author yangyibufeng
     * @description
     * @date 2024/3/13 14:42
     */
    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        // 从request里面获取response保证了一致性
        // 设置HTTP响应头，content-type 指明了响应体中数据的媒体类型，application/json 这个响应包含的是JSON格式的数据
        HttpServerResponse httpServerResponse = request.response().putHeader("content-type", "application/json");

        try {
            // 将返回的信息进行序列化
            byte[] serialized = serializer.serializer(rpcResponse);
            // 将序列化好的二进制数据打包到Buffer里面，然后通过HTTP响应对象发送给客户端，最后结束这次HTTP响应
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }


}