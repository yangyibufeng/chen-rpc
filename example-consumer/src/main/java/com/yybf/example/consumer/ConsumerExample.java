package com.yybf.example.consumer;

import cn.hutool.core.collection.SpliteratorUtil;
import com.yybf.chenrpc.config.RpcConfig;
import com.yybf.chenrpc.proxy.ServiceProxyFactory;
import com.yybf.chenrpc.utils.ConfigUtil;
import com.yybf.example.common.model.User;
import com.yybf.example.common.service.UserService;

/**
 * 扩展RPC后示例消费者类，测试配置文件的读取
 *
 * @author yangyibufeng
 * @date 2024/3/15
 */
public class ConsumerExample {
    public static void main(String[] args) {
        testReadConfig();
    }

    public static void testUsingMock() {
        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("yybf");

        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }

        // 验证是否使用Mock
        long num = userService.getNumber();
        System.out.println("num = " + num);
    }

    private static void testReadConfig() {
        RpcConfig rpcConfig = ConfigUtil.loadConfig(RpcConfig.class,"rpc");
        System.out.println(rpcConfig);
    }
}