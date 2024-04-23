package com.yybf.chenrpc.springboot.starter.bootstrap;

import com.yybf.chenrpc.proxy.ServiceProxyFactory;
import com.yybf.chenrpc.springboot.starter.annotation.RpcReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * Rpc 消费者启动
 *
 * @author yangyibufeng
 * @date 2024/4/20
 */
@Slf4j
public class RpcConsumerBootStrap implements BeanPostProcessor {

    /**
     * Bean初始化后执行，注册服务
     * 它会检查被初始化的 Bean 中的属性上面是否带有 @RpcReference 注解，
     * 如果有的话，就会将该属性生成代理对象
     * 有点类似于spring boot里面的 @Resource
     *
     * @param bean:     被初始化的 bean 的名称
     * @param beanName: 该 bean 在 spring 容器中的名称
     * @return java.lang.Object:
     * @author yangyibufeng
     * @date 2024/4/22 11:30
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);

            if (rpcReference != null) {
                log.info("开始注入 " + field);
                // 为属性生成代理对象
                Class<?> interfaceClass = rpcReference.interfaceClass();
                if (interfaceClass == void.class) {
                    interfaceClass = field.getType();
                    log.error("类型：" + field.getType());
                }
                log.error("生成的对象：" + interfaceClass);

                // 将属性设置为可访问，以便后续设置代理对象。
                field.setAccessible(true);
                Object proxy = ServiceProxyFactory.getProxy(interfaceClass);

                try {
                    // 为属性注入代理对象
                    field.set(bean, proxy);
//                    log.warn("已经注入代理对象 ：" + field + "\n" + bean + "\n" + proxy);
                    // 将属性设置为不可访问，以保证安全性。
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("为字段注入代理对象失败" + e);
                }
            }
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}