package com.ping.voicelinkguardianbackend.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户设计请求
 */
@Data
public class DesignCenterVO implements Serializable {

    private static final long serialVersionUID = 4457099409814039852L;

    /**
     * 组名id
     */
    private Long userId;

    /**
     * 组名
     */
    private String groupName;

    /**
     * 核心场景
     */
    private String coreScene;

    /**
     * 预设语音指令
     */
    private String presetVoiceCommand;

    /**
     * 系统响应逻辑
     */
    private String systemResponseLogic;
}
