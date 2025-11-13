-- 用户表
create table user
(
    id         bigint auto_increment comment 'id' primary key,
    groupName  varchar(256)                           not null comment '组名',
    password   varchar(256)                           not null,
    userRole   varchar(256) default 'user'            not null comment '用户角色：user/admin',
    createTime datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint      default 0                 not null comment '是否删除'
) comment '用户';

-- 用户进度表
create table user_progress
(
    id           bigint auto_increment comment 'id' primary key,
    userId       bigint                             not null comment '组名id',
    groupName    varchar(256)                       not null comment '组名',
    currentLevel int      default 0 comment '当前关卡',
    totalLevels  int      default 6 comment '总关卡数',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
) comment '用户进度';