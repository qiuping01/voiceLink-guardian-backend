package com.ping.voicelinkguardianbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 第四关 写入答案请求
 */
@Data
public class FourthLevelRequest implements Serializable {

    private static final long serialVersionUID = 820254526274174036L;

    /**
     * 小组选择的场景是
     */
    private String fourthScene;

    /**
     * 实现功能
     */
    private String fourthAchieve;

    /**
     * 我们还添加了功能
     */
    private String fourthExtra;

    /**
     * 在闯关过程中，我们遇到最难的一个"Boss"（难题）是:
     */
    private String fourthProblem;

    /**
     * 解决方法
     */
    private String fourthSolution;
}

