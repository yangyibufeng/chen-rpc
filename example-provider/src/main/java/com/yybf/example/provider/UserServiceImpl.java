package com.yybf.example.provider;

import com.yybf.example.common.model.User;
import com.yybf.example.common.service.UserService;

/**
 *
 * 用户服务实现类
 * @author yangyibufeng
 * @date 2024/3/11
 */
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}