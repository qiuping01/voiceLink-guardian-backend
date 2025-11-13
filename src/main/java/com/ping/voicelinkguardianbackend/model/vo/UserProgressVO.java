package com.ping.voicelinkguardianbackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserProgressVO implements Serializable {

    private static final long serialVersionUID = 944443316206611071L;

    /**
     * id
     */
    private Long userId;

    /**
     * 组名
     */
    private String groupName;

    /**
     * 当前关卡
     */
    private Integer currentLevel;

    /**
     * 总关卡数
     */
    private Integer totalLevels;
}
