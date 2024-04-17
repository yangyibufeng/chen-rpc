package com.yybf.example.consumer;

import com.yybf.chenrpc.proxy.ServiceProxyFactory;
import com.yybf.example.common.model.User;
import com.yybf.example.common.service.UserService;

/**
 * 简易服务消费者示例
 *
 * @author yangyibufeng
 * @date 2024/3/11
 */
public class EasyConsumerExample {
    public static void main(String[] args) throws InterruptedException {
        // todo 需要获取UserService的实现类对象
        // 调用静态代理
//        UserService userService = new UserServiceProxy();
        // 调用动态代理
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
    }
}