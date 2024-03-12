package com.yybf.example.common.service;

import com.yybf.example.common.model.User;

/**
 * @author yangyibufeng
 * @Description 用户服务
 * @date 2024/3/11-13:30
 */
public interface UserService {
    /**
     * 获取用户
     *
     * @param user:
     * @return com.yybf.example.common.model.User:
     * @author yangyibufeng
     * @date 2024/3/11 13:37
     */  
    User getUser(User user);
}
