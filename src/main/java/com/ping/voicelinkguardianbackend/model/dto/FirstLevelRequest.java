package com.ping.voicelinkguardianbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 第一关 写入答案请求
 */
@Data
public class FirstLevelRequest implements Serializable {

    private static final long serialVersionUID = -9105191087112929972L;

    /**
     * 视频中老人面临的最大困境是什么？
     */
    private String firstDilemma;
}

