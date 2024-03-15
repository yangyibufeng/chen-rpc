package com.yybf.chenrpc;

import com.yybf.chenrpc.config.RpcConfig;
import com.yybf.chenrpc.constant.RpcConstant;
import com.yybf.chenrpc.utils.ConfigUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * RPC 框架应用
 * 相当于 holder，存放了项目全局用到的变量。双检锁单例模式实现
 * 作为RPC项目的启动入口，维护全局用到的变量
 *
 * @author yangyibufeng
 * @date 2024/3/15
 */
@Slf4j
public class RpcApplication {

    // volatile 确保了多线程下对 rpcConfig 的可见性
    private static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化，支持传入自定义配置
     * 
     * @param newRpcConfig: 配置类
     * @return void:
     * @author yangyibufeng
     * @date 2024/3/15 18:32
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init,config = {}", newRpcConfig.toString());
    }

    /**
     * 重载了init方法，使得可以从配置文件获得默认前缀的配置
     *
     * @return void:
     * @author yangyibufeng
     * @date 2024/3/15 18:37
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtil.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // 默认前缀配置获取失败，使用默认值
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }
    
    /**
     * 
     * 外界从对象中获取已经从文件中读取好的配置类
     * 这里实现了双重检查锁定
     * @author yangyibufeng 
     * @date 2024/3/15 18:41
     
     * @return com.yybf.chenrpc.config.RpcConfig:
     */
    public static RpcConfig getRpcConfig(){
        if(rpcConfig == null){
            synchronized (RpcApplication.class){
                if(rpcConfig == null){
                    init();
                }
            }
        }
        
        return rpcConfig;
    }

}