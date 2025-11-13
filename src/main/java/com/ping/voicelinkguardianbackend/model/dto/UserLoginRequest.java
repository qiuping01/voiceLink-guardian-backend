package com.ping.voicelinkguardianbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -2614306386845897724L;

    /**
     * 组名
     */
    private String groupName;

    /**
     * 密码
     */
    private String password;
}
