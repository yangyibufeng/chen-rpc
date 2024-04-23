package com.yybf.examplespringbootconsumer;

import com.yybf.chenrpc.springboot.starter.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRpc(needServer = false) // 标识启动 RPC 框架，但不启动服务器
public class ExampleSpringbootConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootConsumerApplication.class, args);
    }

}
