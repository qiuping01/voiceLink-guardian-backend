package com.ping.voicelinkguardianbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 第五关 写入答案请求
 */
@Data
public class FifthLevelRequest implements Serializable {

    private static final long serialVersionUID = 7934597375817613352L;

    /**
     * 通过本项目，我对人工智能语音识别与物联网技术的理解有了明显加深
     */
    private String fifthComment1;

    /**
     * 我能够清晰地描述"声联守护"系统中感知层、网络层、应用层各自的作用
     */
    private String fifthComment2;

    /**
     * 在完成本项目后，我掌握或提升了以下哪些技能？（可多选）
     */
    private String fifthComment3;

    /**
     * 你认为课程中哪个环节挑战最大？
     */
    private String fifthComment4;

    /**
     * 如果可以为"声联守护"系统增加一项新功能，你希望是什么？
     */
    private String fifthComment5;

    /**
     * 通过本节课，你对"技术服务于人"的理解有哪些新的收获？
     */
    private String fifthComment6;
}

