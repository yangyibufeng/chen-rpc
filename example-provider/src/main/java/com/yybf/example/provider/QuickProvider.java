package com.yybf.example.provider;

import com.yybf.chenrpc.bootstrap.ProviderBootstrap;
import com.yybf.chenrpc.bootstrap.ServiceRegisterInfo;
import com.yybf.example.common.service.TicketService;
import com.yybf.example.common.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * 利用封装好的服务提供者启动类快速启动
 *
 * @author yangyibufeng
 * @date 2024/4/18
 */
public class QuickProvider {
    public static void main(String[] args) {
        // 需要注册的服务
        List<ServiceRegisterInfo> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);
        ServiceRegisterInfo serviceRegisterInfo1 = new ServiceRegisterInfo(TicketService.class.getName(), TicketServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);
        serviceRegisterInfoList.add(serviceRegisterInfo1);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}