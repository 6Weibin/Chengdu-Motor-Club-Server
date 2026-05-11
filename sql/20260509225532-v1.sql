-- 已删除活动管理菜单初始化脚本
-- by AI.Coding

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
select '车友会后台', 0, 8, '#', 'M', '0', '', 'fa fa-motorcycle', 'admin', sysdate(), '车友会后台目录'
where not exists (select 1 from sys_menu where menu_name = '车友会后台' and parent_id = 0);

select @motorclubRootId := menu_id from sys_menu where menu_name = '车友会后台' and parent_id = 0 limit 1;

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
select '已删除活动', @motorclubRootId, 9, '/motorclub/activity/deleted', 'C', '0', 'motorclub:activity:deleted:view', '#', 'admin', sysdate(), '车友会已删除活动管理菜单'
where not exists (select 1 from sys_menu where menu_name = '已删除活动' and parent_id = @motorclubRootId);

select @motorclubDeletedActivityId := menu_id from sys_menu where menu_name = '已删除活动' and parent_id = @motorclubRootId limit 1;

insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
select '已删除活动查询', @motorclubDeletedActivityId, 1, '#', 'F', '0', 'motorclub:activity:deleted:list', '#', 'admin', sysdate()
where not exists (select 1 from sys_menu where parent_id = @motorclubDeletedActivityId and perms = 'motorclub:activity:deleted:list');

insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
select '已删除活动恢复', @motorclubDeletedActivityId, 2, '#', 'F', '0', 'motorclub:activity:restore', '#', 'admin', sysdate()
where not exists (select 1 from sys_menu where parent_id = @motorclubDeletedActivityId and perms = 'motorclub:activity:restore');
