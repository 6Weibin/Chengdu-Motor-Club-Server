-- 车友会业务表初始化脚本
-- by AI.Coding

drop table if exists mc_admin_permission;
drop table if exists mc_activity_checkin_code;
drop table if exists mc_activity_registration;
drop table if exists mc_activity;
drop table if exists mc_membership_application;
drop table if exists mc_user_token;
drop table if exists mc_banner;
drop table if exists mc_benefit;
drop table if exists mc_user;

create table mc_user (
    user_id bigint(20) not null auto_increment comment '用户主键',
    openid varchar(64) not null comment '微信openid',
    unionid varchar(64) default null comment '微信unionid',
    nick_name varchar(100) not null comment '昵称',
    avatar_url varchar(500) default null comment '头像地址',
    phone varchar(20) default null comment '手机号',
    real_name varchar(50) default null comment '真实姓名',
    id_number_cipher varchar(256) default null comment '证件号密文',
    id_number_masked varchar(32) default null comment '证件号脱敏值',
    car_model varchar(100) default null comment '车型',
    member_status varchar(20) not null default 'visitor' comment '会员状态',
    member_card_no varchar(50) default null comment '会员卡号',
    join_time datetime default null comment '入会时间',
    app_admin_type varchar(20) not null default 'none' comment '小程序后台管理员类型',
    status char(1) not null default '0' comment '状态（0正常 1停用）',
    last_login_ip varchar(128) default null comment '最近登录IP',
    last_login_time datetime default null comment '最近登录时间',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (user_id),
    unique key uk_mc_user_openid (openid),
    key idx_mc_user_phone (phone),
    key idx_mc_user_member_status (member_status)
) engine=innodb comment='车友会小程序用户表';

create table mc_user_token (
    token_id bigint(20) not null auto_increment comment 'Token主键',
    user_id bigint(20) not null comment '用户主键',
    token_value varchar(128) not null comment 'Token值',
    expired_at datetime not null comment '失效时间',
    last_access_time datetime default null comment '最后访问时间',
    status char(1) not null default '0' comment '状态（0有效 1失效）',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (token_id),
    unique key uk_mc_user_token_value (token_value),
    key idx_mc_user_token_user_id (user_id)
) engine=innodb comment='车友会小程序Token表';

create table mc_membership_application (
    application_id bigint(20) not null auto_increment comment '申请主键',
    user_id bigint(20) not null comment '用户主键',
    phone varchar(20) default null comment '手机号快照',
    real_name varchar(50) default null comment '真实姓名快照',
    car_model varchar(100) default null comment '车型快照',
    id_number_masked varchar(32) default null comment '证件号脱敏值',
    status varchar(20) not null default 'pending' comment '申请状态',
    reject_reason varchar(500) default null comment '驳回原因',
    review_by varchar(64) default null comment '审核人',
    review_time datetime default null comment '审核时间',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (application_id),
    key idx_mc_app_user_id (user_id),
    key idx_mc_app_status (status)
) engine=innodb comment='车友会入会申请表';

create table mc_activity (
    activity_id bigint(20) not null auto_increment comment '活动主键',
    title varchar(200) not null comment '活动标题',
    description varchar(2000) not null comment '活动描述',
    cover_image_url varchar(500) default null comment '封面图片',
    activity_date datetime not null comment '活动日期',
    start_time varchar(20) not null comment '开始时间',
    end_time varchar(20) not null comment '结束时间',
    location varchar(255) not null comment '活动地点',
    capacity int(11) not null default 0 comment '人数上限',
    current_participants int(11) not null default 0 comment '当前参与人数',
    status varchar(20) not null default 'upcoming' comment '活动状态',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (activity_id),
    key idx_mc_activity_status (status, activity_date)
) engine=innodb comment='车友会活动表';

create table mc_activity_registration (
    registration_id bigint(20) not null auto_increment comment '报名主键',
    activity_id bigint(20) not null comment '活动主键',
    user_id bigint(20) not null comment '用户主键',
    status varchar(20) not null default 'pending' comment '报名状态',
    reject_reason varchar(500) default null comment '驳回原因',
    checked_in char(1) not null default '0' comment '是否已签到',
    checked_in_at datetime default null comment '签到时间',
    review_by varchar(64) default null comment '审核人',
    review_time datetime default null comment '审核时间',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (registration_id),
    unique key uk_mc_reg_activity_user (activity_id, user_id),
    key idx_mc_reg_status (status),
    key idx_mc_reg_user_id (user_id)
) engine=innodb comment='车友会活动报名表';

create table mc_activity_checkin_code (
    code_id bigint(20) not null auto_increment comment '签到码主键',
    activity_id bigint(20) not null comment '活动主键',
    token varchar(64) not null comment '签到令牌',
    code_value varchar(255) not null comment '二维码原始值',
    generated_time datetime not null comment '生成时间',
    generated_by varchar(64) not null comment '生成者',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (code_id),
    unique key uk_mc_checkin_activity (activity_id),
    unique key uk_mc_checkin_token (token)
) engine=innodb comment='车友会活动签到码表';

create table mc_banner (
    banner_id bigint(20) not null auto_increment comment 'Banner主键',
    title varchar(200) not null comment '标题',
    image_url varchar(500) not null comment '图片地址',
    link_url varchar(500) default null comment '跳转链接',
    sort int(11) not null default 0 comment '排序值',
    status char(1) not null default '0' comment '状态（0启用 1停用）',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (banner_id)
) engine=innodb comment='车友会Banner表';

create table mc_benefit (
    benefit_id bigint(20) not null auto_increment comment '权益主键',
    title varchar(200) not null comment '标题',
    summary varchar(500) default null comment '摘要',
    content text comment '正文',
    image_url varchar(500) default null comment '图片地址',
    sort int(11) not null default 0 comment '排序值',
    status char(1) not null default '0' comment '状态（0启用 1停用）',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (benefit_id)
) engine=innodb comment='车友会权益表';

create table mc_admin_permission (
    permission_id bigint(20) not null auto_increment comment '权限主键',
    user_id bigint(20) not null comment '用户主键',
    member_manage char(1) not null default '0' comment '成员管理权限',
    activity_manage char(1) not null default '0' comment '活动管理权限',
    banner_manage char(1) not null default '0' comment 'Banner管理权限',
    benefit_manage char(1) not null default '0' comment '权益管理权限',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (permission_id),
    unique key uk_mc_admin_permission_user (user_id)
) engine=innodb comment='车友会业务管理员权限表';

insert into sys_config(config_name, config_key, config_value, config_type, create_by, create_time, remark)
select '车友会-是否允许用户编辑资料', 'motorclub.allow-profile-edit', 'true', 'N', 'admin', sysdate(), '车友会小程序资料编辑开关'
where not exists (select 1 from sys_config where config_key = 'motorclub.allow-profile-edit');

insert into sys_config(config_name, config_key, config_value, config_type, create_by, create_time, remark)
select '车友会-入会是否需要审核', 'motorclub.join-need-review', 'true', 'N', 'admin', sysdate(), '车友会入会审核开关'
where not exists (select 1 from sys_config where config_key = 'motorclub.join-need-review');

insert into sys_config(config_name, config_key, config_value, config_type, create_by, create_time, remark)
select '车友会-入会是否要求完整实名资料', 'motorclub.join-require-detailed-profile', 'true', 'N', 'admin', sysdate(), '车友会入会实名资料开关'
where not exists (select 1 from sys_config where config_key = 'motorclub.join-require-detailed-profile');
