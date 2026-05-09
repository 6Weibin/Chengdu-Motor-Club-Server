-- 车友会新闻模块数据库变更脚本
-- by AI.Coding

create table if not exists mc_news (
    news_id bigint(20) not null auto_increment comment '新闻主键',
    title varchar(100) not null comment '新闻标题',
    tag_name varchar(30) not null comment '门户展示标签',
    summary varchar(255) not null comment '新闻摘要',
    cover_image_url varchar(500) not null comment '封面图地址',
    content longtext not null comment '富文本正文',
    publish_time datetime not null comment '发布时间',
    status char(1) not null default '0' comment '状态（0上线 1下线）',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (news_id),
    key idx_mc_news_status_publish_time (status, publish_time)
) engine=innodb comment='车友会新闻表';

select @motorclubRootId := menu_id from sys_menu where menu_name = '车友会后台' and parent_id = 0 limit 1;

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
select '新闻管理', @motorclubRootId, 9, '/motorclub/news', 'C', '0', 'motorclub:news:view', 'fa fa-newspaper-o', 'admin', sysdate(), '车友会新闻菜单'
where @motorclubRootId is not null
  and not exists (select 1 from sys_menu where menu_name = '新闻管理' and parent_id = @motorclubRootId);

select @motorclubNewsId := menu_id from sys_menu where menu_name = '新闻管理' and parent_id = @motorclubRootId limit 1;
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
select '新闻查询', @motorclubNewsId, 1, '#', 'F', '0', 'motorclub:news:list', '#', 'admin', sysdate()
where @motorclubNewsId is not null
  and not exists (select 1 from sys_menu where parent_id = @motorclubNewsId and perms = 'motorclub:news:list');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
select '新闻新增', @motorclubNewsId, 2, '#', 'F', '0', 'motorclub:news:add', '#', 'admin', sysdate()
where @motorclubNewsId is not null
  and not exists (select 1 from sys_menu where parent_id = @motorclubNewsId and perms = 'motorclub:news:add');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
select '新闻编辑', @motorclubNewsId, 3, '#', 'F', '0', 'motorclub:news:edit', '#', 'admin', sysdate()
where @motorclubNewsId is not null
  and not exists (select 1 from sys_menu where parent_id = @motorclubNewsId and perms = 'motorclub:news:edit');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
select '新闻删除', @motorclubNewsId, 4, '#', 'F', '0', 'motorclub:news:remove', '#', 'admin', sysdate()
where @motorclubNewsId is not null
  and not exists (select 1 from sys_menu where parent_id = @motorclubNewsId and perms = 'motorclub:news:remove');
