package com.yybf.chenrpc.server.tcp;

import com.caucho.services.message.MessageSender;
import com.yybf.chenrpc.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;

/**
 * Vertx TCP 服务器实现
 *
 * @author yangyibufeng
 * @date 2024/4/5
 */
public class VertxTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData) {
        // 在这里编写处理请求的逻辑，根据requestData的构造响应数据并返回
        // 这里只是一个示例，实际逻辑需要根据集体的业务需求来实现
        return "hello client".getBytes();
    }

    @Override
    public void doStart(int port) {
        // 创建一个Vertx实例
        Vertx vertx = Vertx.vertx();

        // 创建一个TCP服务器
        NetServer server = vertx.createNetServer();

        //处理网络请求
        /*server.connectHandler(socket -> {
            // 处理连接
           socket.handler(buffer -> {
               // 将收到的请求转化成字节数组
               byte[] requestData = buffer.getBytes();
               System.out.println("Received request from server: " + buffer);
               // 使用上面自定义的函数来处理转化成的字节数组
               byte[] responseData = handleRequest(requestData);
               // 发送响应（发送的数据格式为Buffer）
               socket.write(Buffer.buffer(responseData));
           });
        });*/

        // 使用自定义的处理器
        server.connectHandler(new TcpServerHandler());

        // 启动 TCP 服务器，并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server started on port" + port);
            } else {
                System.out.println("Failed to start TCP server: " + result.cause());
            }
        });
    }

    public void testTCPServer(int port) {
        // 创建一个Vertx实例
        Vertx vertx = Vertx.vertx();

        // 创建一个TCP服务器
        NetServer server = vertx.createNetServer();

//        AtomicInteger half_pack_num = new AtomicInteger();
//        AtomicInteger sticky_pack_num = new AtomicInteger();

        //处理网络请求
        server.connectHandler(socket -> {
            String testMessage = "Hello server!Hello server!Hello server!";
            int messageLength = testMessage.getBytes().length;

            // 构造parser
            RecordParser parser = RecordParser.newFixed(messageLength);
            parser.setOutput(new Handler<Buffer>() {
                @Override
                public void handle(Buffer buffer) {
                    String str = new String(buffer.getBytes());
                    System.out.println(str);
                    if(testMessage.equals(str)){
                        System.out.println("good！");
                    }
                }
            });

            // 装配处理逻辑代码
            socket.handler(parser);
           /* // 处理连接
            socket.handler(buffer -> {

                String str = new String(buffer.getBytes());
                System.out.println("result ： " + str);

                int bufferLength = buffer.getBytes().length;
                if (bufferLength < messageLength) {
                    System.out.println("result ： 半包，length = " + bufferLength);
//                   half_pack_num.getAndIncrement();
                    return;
                } else if (bufferLength > messageLength) {
                    System.out.println("result ： 粘包，length = " + bufferLength);
//                   sticky_pack_num.getAndIncrement();
                    return;
                }

//               String str = new String(buffer.getBytes(0,messageLength));
//               System.out.println("result ： " + str);
                if (testMessage.equals(str)) {
                    System.out.println("good");
                }

//            });*/
        });

        // 启动 TCP 服务器，并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server started on port" + port);
            } else {
                System.out.println("Failed to start TCP server: " + result.cause());
            }
        });

//        int hpn = half_pack_num.intValue();
//        int spn = sticky_pack_num.intValue();

//        System.out.println("半包数为：" + hpn + "，比率为：" + (hpn / 1000 * 100) + "%");
//        System.out.println("粘包数为：" + spn + "，比率为：" + (spn / 1000 * 100) + "%");

    }

    public static void main(String[] args) {
//        new VertxTcpServer().doStart(8888);
        new VertxTcpServer().testTCPServer(8888);
    }
}