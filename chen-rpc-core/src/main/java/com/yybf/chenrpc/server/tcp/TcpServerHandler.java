package com.yybf.chenrpc.server.tcp;

import com.yybf.chenrpc.model.RpcRequest;
import com.yybf.chenrpc.model.RpcResponse;
import com.yybf.chenrpc.protocol.ProtocolMessage;
import com.yybf.chenrpc.protocol.ProtocolMessageDecoder;
import com.yybf.chenrpc.protocol.ProtocolMessageEncoder;
import com.yybf.chenrpc.protocol.ProtocolMessageTypeEnum;
import com.yybf.chenrpc.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.lang.reflect.Method;

/**
 * TCP 请求处理器
 *
 * @author yangyibufeng
 * @date 2024/3/12
 */
public class TcpServerHandler implements Handler<NetSocket> {
    @Override
    public void handle(NetSocket netSocket) {
        // 处理连接 （使用自己封装的处理器）
        TcpBufferHandleWrapper bufferHandleWrapper = new TcpBufferHandleWrapper(buffer -> {
            // 接受请求并解码
            ProtocolMessage<RpcRequest> protocolMessage;

            try {
                protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
            } catch (Exception e) {
                throw new RuntimeException("协议消息解码错误！ exception：" + e);
            }

            RpcRequest rpcRequest = protocolMessage.getBody();

            // 处理请求
            // 构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();

            try {
                // 获取需要调用的服务实现类，然后通过反射调用
                // 步骤：先通过请求中的服务名来从本地注册表中获取他的实现类，
                // 然后利用请求中的方法名和参数类型来找到需要具体执行的方法，
                // 最后通过反射将实现类的实例和具体参数传入方法中进行调用
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());

                // 封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("OK");

            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }

            // 发送响应，进行编码
            // todo：感觉这里构建协议消息头的时候好多参数没有填，有点问题
            ProtocolMessage.Header header = protocolMessage.getHeader();
            header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = new ProtocolMessage<>(header, rpcResponse);

            try {
                Buffer encode = ProtocolMessageEncoder.encode(rpcResponseProtocolMessage);
                netSocket.write(encode);
            } catch (Exception e) {
                throw new RuntimeException("协议消息编码错误！ exception：" + e);
            }

        });

    }

}