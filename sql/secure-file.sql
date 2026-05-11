-- 安全文件模块初始化脚本
-- by AI.Coding

create table if not exists sf_file (
    file_id bigint(20) not null auto_increment comment '文件ID',
    original_name varchar(255) not null comment '原始文件名',
    file_ext varchar(32) not null comment '文件后缀',
    file_size bigint(20) not null comment '文件大小',
    content_type varchar(128) not null comment 'MIME类型',
    biz_type varchar(64) not null comment '业务类型',
    public_access char(1) not null default 'N' comment '是否公开访问（Y是 N否）',
    stored_name varchar(128) not null comment '密文文件名',
    storage_path varchar(255) not null comment '内部存储相对路径',
    sha256 varchar(64) not null comment '原始文件SHA-256',
    encrypt_alg varchar(32) not null comment '加密算法',
    encrypt_iv varchar(64) not null comment '加密IV',
    status char(1) not null default '0' comment '状态（0正常 1禁用 2删除）',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (file_id),
    unique key uk_sf_file_stored_name (stored_name),
    key idx_sf_file_name (original_name),
    key idx_sf_file_biz_type (biz_type),
    key idx_sf_file_public (public_access),
    key idx_sf_file_status (status),
    key idx_sf_file_create_time (create_time)
) engine=innodb auto_increment=1 comment='安全文件记录表';

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
select '文件中心', 0, 9, '#', 'M', '0', '', 'fa fa-folder-open', 'admin', sysdate(), '安全文件中心目录'
where not exists (select 1 from sys_menu where menu_name = '文件中心' and parent_id = 0);

select @secureFileRootId := menu_id from sys_menu where menu_name = '文件中心' and parent_id = 0 limit 1;

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
select '文件管理', @secureFileRootId, 1, '/secure-file/file', 'C', '0', 'securefile:file:view', '#', 'admin', sysdate(), '安全文件管理菜单'
where not exists (select 1 from sys_menu where menu_name = '文件管理' and parent_id = @secureFileRootId);

select @secureFileMenuId := menu_id from sys_menu where menu_name = '文件管理' and parent_id = @secureFileRootId limit 1;

insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
select '文件查询', @secureFileMenuId, 1, '#', 'F', '0', 'securefile:file:list', '#', 'admin', sysdate()
where not exists (select 1 from sys_menu where parent_id = @secureFileMenuId and perms = 'securefile:file:list');

insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
select '文件上传', @secureFileMenuId, 2, '#', 'F', '0', 'securefile:file:upload', '#', 'admin', sysdate()
where not exists (select 1 from sys_menu where parent_id = @secureFileMenuId and perms = 'securefile:file:upload');

insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
select '文件下载', @secureFileMenuId, 3, '#', 'F', '0', 'securefile:file:download', '#', 'admin', sysdate()
where not exists (select 1 from sys_menu where parent_id = @secureFileMenuId and perms = 'securefile:file:download');

insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
select '文件状态', @secureFileMenuId, 4, '#', 'F', '0', 'securefile:file:edit', '#', 'admin', sysdate()
where not exists (select 1 from sys_menu where parent_id = @secureFileMenuId and perms = 'securefile:file:edit');

insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
select '文件删除', @secureFileMenuId, 5, '#', 'F', '0', 'securefile:file:remove', '#', 'admin', sysdate()
where not exists (select 1 from sys_menu where parent_id = @secureFileMenuId and perms = 'securefile:file:remove');
