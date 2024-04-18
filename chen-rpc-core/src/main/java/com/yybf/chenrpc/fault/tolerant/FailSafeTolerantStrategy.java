package com.yybf.chenrpc.fault.tolerant;

import com.yybf.chenrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 静默处理
 * 在遇到异常之后记录日志，然后正常返回响应
 *
 * @author yangyibufeng
 * @date 2024/4/18
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.error("静默处理异常 {}", context, e);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setException(new RuntimeException("fuck you vert.x!!!"));

        return rpcResponse;
    }
}