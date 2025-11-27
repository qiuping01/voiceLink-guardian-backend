package com.ping.voicelinkguardianbackend.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 关卡答案
 */
@Data
public class LevelAnswerVO implements Serializable {

    private static final long serialVersionUID = -3494861826715130427L;

    /**
     * 组名id
     */
    private Long userId;

    /**
     * 组名
     */
    private String groupName;

    /**
     * 视频中老人面临的最大困境是什么？
     */
    private String firstDilemma;

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

    /**
     * 进阶挑战一 (温湿度传感器火灾报警)
     * 进阶挑战二 (语音控制开关灯)
     * 进阶挑战三 (检测加速度值摔倒报警)
     */
    private String thirdChallenge;

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