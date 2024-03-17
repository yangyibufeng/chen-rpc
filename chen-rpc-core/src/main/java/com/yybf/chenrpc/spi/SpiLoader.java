package com.yybf.chenrpc.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.yybf.chenrpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI 加载器（支持键值对映射）
 *
 * @author yangyibufeng
 * @date 2024/3/17
 */
@Slf4j
public class SpiLoader {

    // 存储已加载的类：接口名 => （key =》 实现类）
    public static Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    // 对象实例缓存（避免重复new），类路径 =》 对象单例 （单例模式）
    public static Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    // 系统SPI目录
    public static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    // 用户自定义Spi目录
    public static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    // 扫描路径
    public static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    // 动态加载的类列表
    public static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    /**
     * 加载所有类型
     *
     * @return void:
     * @author yangyibufeng
     * @date 2024/3/17 21:52
     */
    public static void loadAll() {
        log.info("加载所有SPI");
        for (Class<?> tClass : LOAD_CLASS_LIST) {
            load(tClass);
        }
    }

    /**
     * 获取指定接口的实例
     *
     * @param tClass: 想要获取的接口
     * @param key:    类型（自定义）
     * @return T: 该接口的实现类
     * @author yangyibufeng
     * @date 2024/3/17 22:11
     */
    public static <T> T getInstance(Class<?> tClass, String key) {
        // 获取类名称
        String tClassName = tClass.getName();
        // 根据接口名获取已加载的类
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader未加载%s类型", tClass));
        }
        // .containsKey()判断Map里面是否包含指定键名-key
        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader 的 %s 不存在 key=%s 的类型", tClassName, key)))
        }
        // 获取到要加载的实现类型
        Class<?> implClass = keyClassMap.get(key);
        // 从实例缓存中加载指定类型的实例
        String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClassName)) {
            // 如果缓存中没有这个实例
            try {
                instanceCache.put(implClassName, implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                String errorMsg = String.format("%s 类实例化失败", implClassName)
                throw new RuntimeException(errorMsg, e);
            }
        }

        return (T) instanceCache.get(implClassName);

    }

    /**
     * 加载指定类型
     *
     * @param loadClass: 需要加载的类型
     * @return java.util.Map<java.lang.String, java.lang.Class < ?>>:
     * @author yangyibufeng
     * @date 2024/3/17 22:14
     */
    public static Map<String, Class<?>> load(Class<?> loadClass) {
        log.info("加载类型为 {} 的 SPI", loadClass.getName());
        /* 扫描路径，用户自定义的SPI优先级高于系统SPI */
        Map<String, Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir : SCAN_DIRS) {
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());
            // 读取每个资源文件
            for (URL resource : resources) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    // 逐行读取文件内容
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] strArray = line.split("=");
                        if(strArray.length > 1){
                            String key = strArray[0];
                            String className = strArray[1];
                            // Class.forName(className) 通过类的全限定名 className 实例化一个类 ， 等同于 new
                            keyClassMap.put(key,Class.forName(className));
                        }
                    }
                } catch (Exception e) {
                    log.error("spi resource load error", e);
                }
            }
        }
        loaderMap.put(loadClass.getName(),keyClassMap);
        return keyClassMap;
    }

}