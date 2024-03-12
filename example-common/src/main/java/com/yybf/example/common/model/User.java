package com.yybf.example.common.model;

import java.io.Serializable;

/**
 * 用户实体类
 *
 * 需要实现序列化接口，方便在网络中传输
 * @author yangyibufeng
 * @date 2024/3/11
 */
public class User implements Serializable {
    private String name;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}