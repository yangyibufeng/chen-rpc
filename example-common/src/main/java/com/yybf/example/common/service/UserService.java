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

    /**
     * 获取一个固定数字
     * 用来区分是否调用Mock接口
     *
     * @return int:
     * @author yangyibufeng
     * @date 2024/3/15 21:27
     */
    default int getNumber() {
        return 114514;
    }
}
