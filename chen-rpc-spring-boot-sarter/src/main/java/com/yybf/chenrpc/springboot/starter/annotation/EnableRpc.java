package com.yybf.chenrpc.springboot.starter.annotation;

import com.yybf.chenrpc.springboot.starter.bootstrap.RpcConsumerBootStrap;
import com.yybf.chenrpc.springboot.starter.bootstrap.RpcInitBootStrap;
import com.yybf.chenrpc.springboot.starter.bootstrap.RpcProviderBootStrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yangyibufeng
 * @Description 启用 RPC 注解
 * @date 2024/4/20-11:36
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootStrap.class, RpcProviderBootStrap.class, RpcConsumerBootStrap.class})
public @interface EnableRpc {
    /**
     * 需要启动 server
     *
     * @return boolean:
     * @author yangyibufeng
     * @date 2024/4/20 11:38
     */
    boolean needServer() default true;
}
