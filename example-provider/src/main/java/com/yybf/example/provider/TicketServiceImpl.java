package com.yybf.example.provider;

import com.yybf.example.common.model.Ticket;
import com.yybf.example.common.service.TicketService;

/**
 * 用户服务实现类
 *
 * @author yangyibufeng
 * @date 2024/3/11
 */
public class TicketServiceImpl implements TicketService {

    @Override
    public Ticket getTicket(Ticket ticket) {
        System.out.println("id：" + ticket.getId() + "; date:" + ticket.getDate());
        return ticket;
    }
}