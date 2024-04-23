package com.yybf.example.common.service;

import com.yybf.example.common.model.Ticket;

/**
 * @author yangyibufeng
 * @Description 票据服务
 * @date 2024/4/20
 */
public interface TicketService {
    /**
     * 获取票据
     *
     * @param ticket:
     * @return com.yybf.example.common.model.Ticket:
     * @author yangyibufeng
     * @date 2024/4/20 10:48
     */
    Ticket getTicket(Ticket ticket);
}
