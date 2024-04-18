package com.yybf.chenrpc.fault.tolerant;

import com.yybf.chenrpc.model.RpcResponse;

import java.util.Map;

/**
 * 快速失败 -- （在出现异常的时候立即通知外层调用方）
 * 直接抛异常
 *
 * @author yangyibufeng
 * @date 2024/4/18
 */
public class FailFastTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错", e);
    }
}