package com.yybf.example.common.model;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 票据实体类
 *
 * 主要用来检测Rpc的功能而虚构出来的一种了类
 * @author yangyibufeng
 * @date 2024/4/20
 */
@Data
public class Ticket implements Serializable {
    private long id;

    private Date date;
}