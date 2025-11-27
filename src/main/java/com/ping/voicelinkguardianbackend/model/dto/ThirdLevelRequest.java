package com.ping.voicelinkguardianbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 第三关 写入答案请求
 */
@Data
public class ThirdLevelRequest implements Serializable {

    private static final long serialVersionUID = -9105191087112929972L;

    /**
     * 进阶挑战一 (温湿度传感器火灾报警)
     * 进阶挑战二 (语音控制开关灯)
     * 进阶挑战三 (检测加速度值摔倒报警)
     */
    private String thirdChallenge;

}

