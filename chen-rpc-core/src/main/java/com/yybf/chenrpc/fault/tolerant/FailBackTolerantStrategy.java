package com.yybf.chenrpc.fault.tolerant;

import com.yybf.chenrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 降级
 * 在遇到异常之后降级成其他的服务
 *
 * @author yangyibufeng
 * @date 2024/4/18
 */
@Slf4j
public class FailBackTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // 调用其他服务
        return null;
    }
}