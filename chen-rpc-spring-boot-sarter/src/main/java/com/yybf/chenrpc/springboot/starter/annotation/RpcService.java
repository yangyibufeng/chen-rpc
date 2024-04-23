package com.yybf.chenrpc.springboot.starter.annotation;

import com.yybf.chenrpc.constant.RpcConstant;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yangyibufeng
 * @Description 服务提供者注解（主要用于注册服务）
 * @date 2024/4/20-11:36
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {
    /**
     * 服务接口类
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 服务版本
     */
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;
}
