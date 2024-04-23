package com.yybf.examplespringbootprovider;

import com.yybf.chenrpc.springboot.starter.annotation.RpcService;
import com.yybf.example.common.model.User;
import com.yybf.example.common.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 *
 * @author yangyibufeng
 * @date 2024/4/22
 */
@Service
@RpcService
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("用户名为：" + user.getName());
        return user;
    }
}