package com.yybf.chenrpc.protocol;

import cn.hutool.core.util.IdUtil;
import com.yybf.chenrpc.constant.RpcConstant;
import com.yybf.chenrpc.model.RpcRequest;
import io.vertx.core.buffer.Buffer;
import org.junit.Assert;
import org.junit.Test;

/**
 * 单元测试类，测试自定义协议的相关功能
 *
 * @author yangyibufeng
 * @date 2024/4/6
 */
public class ProtocolMessageTest {

    @Test
    public void testEncodeAndDecode() throws Exception {
        // 构造消息
        ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer((byte) ProtocolMessageSerializerEnum.KRYO.getKey());
        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        header.setBodyLength(0);

        // 消息体
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName("testService");
        rpcRequest.setMethodName("testMethod");
        rpcRequest.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setArgs(new Object[]{"aaa", "bbb", "ccc", "zhc"});

        protocolMessage.setHeader(header);
        protocolMessage.setBody(rpcRequest);

        Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
        ProtocolMessage<?> message = ProtocolMessageDecoder.decode(encodeBuffer);
        Assert.assertNotNull(message);
        System.out.println(message);
    }
}