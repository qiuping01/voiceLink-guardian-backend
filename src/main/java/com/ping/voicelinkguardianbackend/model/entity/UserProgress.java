package com.ping.voicelinkguardianbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户进度
 * @TableName user_progress
 */
@TableName(value ="user_progress")
@Data
public class UserProgress implements Serializable {
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
     * 当前关卡
     */
    private Integer currentLevel;

    /**
     * 总关卡数
     */
    private Integer totalLevels;

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