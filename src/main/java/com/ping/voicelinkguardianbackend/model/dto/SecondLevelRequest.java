package com.ping.voicelinkguardianbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 第二关 写入答案请求
 */
@Data
public class SecondLevelRequest implements Serializable {

    private static final long serialVersionUID = 5103977805914208151L;

    /**
     * 小组重点关注的场景
     */
    private String secondScene;

    /**
     * 在该场景下，老人最可能遇到的风险
     */
    private String secondRisk;

    /**
     * 老人在这种紧急情况下，最可能喊出的语音指令（指令1）
     */
    private String secondDesign1;

    /**
     * 老人在这种紧急情况下，最可能喊出的语音指令（指令2）
     */
    private String secondDesign2;

    /**
     * 行空板收到求救信息后，发出什么语音安抚老人
     */
    private String secondDesign3;

    /**
     * 发送什么信息给家人或云平台
     */
    private String secondDesign4;

}

