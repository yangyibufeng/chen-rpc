package com.yybf.example.consumer;

import com.yybf.chenrpc.bootstrap.ConsumerBootstrap;
import com.yybf.chenrpc.config.RpcConfig;
import com.yybf.chenrpc.proxy.ServiceProxyFactory;
import com.yybf.chenrpc.utils.ConfigUtil;
import com.yybf.example.common.model.User;
import com.yybf.example.common.service.UserService;

/**
 * 扩展RPC后示例消费者类，测试配置文件的读取
 *
 * @author yangyibufeng
 * @date 2024/4/8
 */
public class QuickConsumer {
    public static void main(String[] args) throws InterruptedException {
        // 服务提供者初始化 ？？？？
        ConsumerBootstrap.init();

        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("yybf");

        User newUser = userService.getUser(user);
        if(newUser != null){
            System.out.println(newUser.getName());
        }else {
            System.out.println("user == null");
        }
    }


}