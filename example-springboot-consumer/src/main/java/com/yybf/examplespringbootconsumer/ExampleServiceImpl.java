package com.yybf.examplespringbootconsumer;

import com.yybf.chenrpc.springboot.starter.annotation.RpcReference;
import com.yybf.example.common.model.User;
import com.yybf.example.common.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * $END$
 *
 * @author yangyibufeng
 * @date 2024/4/22
 */
@Service
public class ExampleServiceImpl {

    @RpcReference
    private UserService userService;

    public void test(){
        User user = new User();
        user.setName("杨毅不逢");

        User serviceUser = userService.getUser(user);
        System.out.println("ExampleServiceImpl - test"+serviceUser.getName());
    }
}