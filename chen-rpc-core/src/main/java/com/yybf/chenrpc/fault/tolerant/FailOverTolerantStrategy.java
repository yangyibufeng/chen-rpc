package com.yybf.chenrpc.fault.tolerant;

import com.yybf.chenrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 转移服务
 * 在遇到异常之后，转移到其他服务节点
 *
 * @author yangyibufeng
 * @date 2024/4/18
 */
@Slf4j
public class FailOverTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // 获取其他服务节点，并进行调用
        return null;
    }
}