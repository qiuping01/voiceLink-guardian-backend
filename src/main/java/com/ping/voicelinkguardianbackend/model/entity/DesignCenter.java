package com.ping.voicelinkguardianbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 蓝图设计中心
 *
 * @TableName design_center
 */
@TableName(value = "design_center")
@Data
public class DesignCenter implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}