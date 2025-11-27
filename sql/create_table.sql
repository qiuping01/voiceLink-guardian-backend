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

-- 蓝图设计中心表
create table design_center
(
    id                  bigint auto_increment comment 'id' primary key,
    userId              bigint                             not null comment '组名id',
    groupName           varchar(256)                       not null comment '组名',
    coreScene           varchar(512)                       not null comment '核心场景',
    presetVoiceCommand  varchar(512)                       not null comment '预设语音指令',
    systemResponseLogic varchar(512)                       not null comment '系统响应逻辑',
    createTime          datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime          datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete            tinyint  default 0                 not null comment '是否删除'
) comment '蓝图设计中心';

create table level_answer
(
    id               bigint auto_increment comment 'id'
        primary key,
    userId           bigint                             not null comment '组名id',
    groupName        varchar(256)                       not null comment '组名',
    firstDilemma     varchar(512)                       not null comment '视频中老人面临的最大困境是什么？',
    secondScene      varchar(512)                       not null comment '小组重点关注的场景',
    secondRisk       varchar(512)                       not null comment '在该场景下，老人最可能遇到的风险',
    secondDesign1    varchar(512)                       not null comment '老人在这种紧急情况下，最可能喊出的语音指令（指令1）',
    secondDesign2    varchar(512)                       not null comment '老人在这种紧急情况下，最可能喊出的语音指令（指令2）',
    secondDesign3    varchar(512)                       not null comment '行空板收到求救信息后，发出什么语音安抚老人',
    secondDesign4    varchar(512)                       not null comment '发送什么信息给家人或云平台',
    thirdChallenge   varchar(512)                       not null comment '进阶挑战一 (温湿度传感器火灾报警)
进阶挑战二 (语音控制开关灯)
进阶挑战三 (检测加速度值摔倒报警)',
    fourthScene      varchar(512)                       not null comment '小组选择的场景是',
    fourthAchieve    varchar(512)                       not null comment '实现功能',
    fourthExtra      varchar(512)                       not null comment '我们还添加了功能',
    fourthProblem    varchar(512)                       not null comment '在闯关过程中，我们遇到最难的一个"Boss"（难题）是:
',
    fourthSolution   varchar(512)                       not null comment '解决方法',
    fifthComment1    varchar(512)                       not null comment '通过本项目，我对人工智能语音识别与物联网技术的理解有了明显加深',
    fifthComment2    varchar(512)                       not null comment '我能够清晰地描述"声联守护"系统中感知层、网络层、应用层各自的作用',
    fifthComment3    varchar(1024)                      not null comment '在完成本项目后，我掌握或提升了以下哪些技能？（可多选）',
    fifthComment4    varchar(512)                       not null comment '你认为课程中哪个环节挑战最大？',
    fifthComment5    varchar(512)                       not null comment '如果可以为"声联守护"系统增加一项新功能，你希望是什么？',
    fifthComment6    varchar(1024)                      not null comment '通过本节课，你对"技术服务于人"的理解有哪些新的收获？',
    sixthEduComment1 varchar(256)                       not null comment '教学内容设计(5颗星)',
    sixthEduComment2 varchar(256)                       not null comment '教学方法与指导(5颗星)',
    sixthEduComment3 varchar(256)                       not null comment '实践环节安排(5颗星)',
    sixthFeedback1   varchar(1024)                      not null comment '您认为本课程的教学有哪些优点？',
    sixthFeedback2   varchar(1024)                      not null comment '您认为本课程的教学有哪些可以改进的地方？',
    createTime       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete         tinyint  default 0                 not null comment '是否删除'
)
    comment '关卡答案';

ALTER TABLE level_answer
    ADD COLUMN sixthEduComment1 varchar(256) NOT NULL DEFAULT '' COMMENT '教学内容设计(5颗星)',
    ADD COLUMN sixthEduComment2 varchar(256) NOT NULL DEFAULT '' COMMENT '教学方法与指导(5颗星)',
    ADD COLUMN sixthEduComment3 varchar(256) NOT NULL DEFAULT '' COMMENT '实践环节安排(5颗星)',
    ADD COLUMN sixthFeedback1 varchar(1024) NOT NULL DEFAULT '' COMMENT '您认为本课程的教学有哪些优点？',
    ADD COLUMN sixthFeedback2 varchar(1024) NOT NULL DEFAULT '' COMMENT '您认为本课程的教学有哪些可以改进的地方？';