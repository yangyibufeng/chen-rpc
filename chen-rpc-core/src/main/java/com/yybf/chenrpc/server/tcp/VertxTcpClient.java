package com.yybf.chenrpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.yybf.chenrpc.RpcApplication;
import com.yybf.chenrpc.model.RpcRequest;
import com.yybf.chenrpc.model.RpcResponse;
import com.yybf.chenrpc.model.ServiceMetaInfo;
import com.yybf.chenrpc.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Vertx TCP 客户端实现
 *
 * @author yangyibufeng
 * @date 2024/4/5
 */
public class VertxTcpClient {
    /**
     * 真正的客户端处理逻辑（使用的是ServiceProxy中的处理逻辑）
     * 发送请求
     *
     * @param rpcRequest:
     * @param serviceMetaInfo:
     * @return com.yybf.chenrpc.model.RpcResponse:
     * @author yangyibufeng
     * @date 2024/4/7 23:28
     */
    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        // 发送Tcp请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        // 使异步式的vertx通过阻塞变为同步
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();

        netClient.connect(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost(),
                result -> {
                    if (!result.succeeded()) {
                        System.err.println("Failed to connect to TCP server!");
                        return;
                    }

                    System.out.println("Connected to TCP server!");
                    // socket代表了与服务端建立的 TCP 连接的套接字，作用是与服务端进行 TCP 连接并进行数据交换
                    NetSocket socket = result.result();
                    // 发送数据
                    // 构造消息
                    ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                    ProtocolMessage.Header header = new ProtocolMessage.Header();
                    header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                    header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                    header.setSerializer((byte) ProtocolMessageSerializerEnum
                            .getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                    header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                    // 生成全局请求ID
                    header.setRequestId(IdUtil.getSnowflakeNextId());
                    protocolMessage.setHeader(header);
                    protocolMessage.setBody(rpcRequest);

                    // 编码请求
                    try {
                        Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                        socket.write(encodeBuffer);
                    } catch (Exception e) {
                        throw new RuntimeException("协议消息编码失败！ exception ：" + e);
                    }

                    // 接收响应
                    TcpBufferHandleWrapper tcpBufferHandleWrapper = new TcpBufferHandleWrapper(
                            buffer -> {
                                try {
                                    ProtocolMessage<RpcResponse> rpcResponseProtocolMessage =
                                            (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                    responseFuture.complete(rpcResponseProtocolMessage.getBody());
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            });

                    // 将响应处理器装配到socket中
                    socket.handler(tcpBufferHandleWrapper);
                });

        RpcResponse rpcResponse = responseFuture.get();
        // 关闭链接
        netClient.close();
        return rpcResponse;
    }


    public void start() {
        // 创建 Vertx 实例
        Vertx vertx = Vertx.vertx();

        vertx.createNetClient().connect(8888, "localhost", result -> {
            if (result.succeeded()) {
                System.out.println("Connect to TCP server");
                NetSocket socket = result.result();
                // 发送数据
                socket.write("hello server!");
                // 接收响应
                socket.handler(buffer -> {
                    System.out.println("Received response from server: " + buffer.toString());
                });
            } else {
                System.out.println("Failed to connect to TCP server");
            }
        });
    }

    /**
     * 通过发送多组信息（1000次）来触发粘包和半包的问题
     *
     * @return void:
     * @author yangyibufeng
     * @date 2024/4/7 21:59
     */
    public void testTCPClient() {
        Vertx vertx = Vertx.vertx();
        vertx.createNetClient().connect(8888, "localhost", result -> {
            if (result.succeeded()) {
                System.out.println("Connect to TCP server");
                NetSocket socket = result.result();

                for (int i = 0; i < 1000; i++) {
                    // 发送数据
                    Buffer buffer = Buffer.buffer();
                    String testMessage = "This is " + (i + 1) + " times;Hello server!Hello server!Hello server!";
                    buffer.appendInt(0);
                    buffer.appendInt(testMessage.length());
                    buffer.appendBytes(testMessage.getBytes());
                    socket.write(buffer);
                }

                //接收数据
                socket.handler(buffer -> {
                    System.out.println("Received response from server: " + buffer.toString());
                });

            } else {
                System.out.println("Failed to connect to TCP server");
            }
        });
    }

    public static RpcResponse testTolerantStrategy(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo){
        throw new RuntimeException("fuck you vert.x");
    }

    public static void main(String[] args) {
//        new VertxTcpClient().start();
//        new VertxTcpClient().testTCPClient();
    }
}