package com.yybf.chenrpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * 读取配置文件并返回配置对象，简化调用
 *
 * @author yangyibufeng
 * @date 2024/3/15
 */
public class ConfigUtil {
    /**
     * 加载配置对象
     *
     * @param tClass: 配置信息映射的类型
     * @param prefix: 需要加载的配置的前缀 过滤只有特定前缀的属性
     * @return T:
     * @author yangyibufeng
     * @date 2024/3/15 17:48
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }

    /**
     * 通过方法的重载，实现对不同环境的兼容
     *
     * @param tClass: 配置信息映射的类型
     * @param prefix: 需要加载的配置的前缀 过滤只有特定前缀的属性
     * @param environment: 指定的环境，不指定就默认为空
     * @return T:
     * @author yangyibufeng
     * @date 2024/3/15 17:59
     */
    private static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");
        Props props = new Props(configFileBuilder.toString());
        return props.toBean(tClass, prefix); // 将指定前缀的配置加载到tClass中
    }
}