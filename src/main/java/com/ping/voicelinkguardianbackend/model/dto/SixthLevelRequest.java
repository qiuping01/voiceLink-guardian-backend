package com.ping.voicelinkguardianbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 第六关 写入答案请求
 */
@Data
public class SixthLevelRequest implements Serializable {


    private static final long serialVersionUID = -2774382403470405230L;
    /**
     * 教学内容设计(5颗星)
     */
    private String sixthEduComment1;

    /**
     * 教学方法与指导(5颗星)
     */
    private String sixthEduComment2;

    /**
     * 实践环节安排(5颗星)
     */
    private String sixthEduComment3;

    /**
     * 您认为本课程的教学有哪些优点？
     */
    private String sixthFeedback1;

    /**
     * 您认为本课程的教学有哪些可以改进的地方？
     */
    private String sixthFeedback2;
}

