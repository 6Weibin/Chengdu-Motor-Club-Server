-- 车友会后台菜单初始化脚本
-- by AI.Coding

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
select '车友会后台', 0, 8, '#', 'M', '0', '', 'fa fa-motorcycle', 'admin', sysdate(), '车友会后台目录'
where not exists (select 1 from sys_menu where menu_name = '车友会后台' and parent_id = 0);

select @motorclubRootId := menu_id from sys_menu where menu_name = '车友会后台' and parent_id = 0 limit 1;

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
select '会员用户', @motorclubRootId, 1, '/motorclub/user', 'C', '0', 'motorclub:user:view', '#', 'admin', sysdate(), '车友会会员用户菜单'
where not exists (select 1 from sys_menu where menu_name = '会员用户' and parent_id = @motorclubRootId);
select @motorclubUserId := menu_id from sys_menu where menu_name = '会员用户' and parent_id = @motorclubRootId limit 1;
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '会员用户查询', @motorclubUserId, 1, '#', 'F', '0', 'motorclub:user:list', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubUserId and perms = 'motorclub:user:list');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '会员用户编辑', @motorclubUserId, 2, '#', 'F', '0', 'motorclub:user:edit', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubUserId and perms = 'motorclub:user:edit');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '会员用户停用', @motorclubUserId, 3, '#', 'F', '0', 'motorclub:user:remove', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubUserId and perms = 'motorclub:user:remove');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '会员用户权限', @motorclubUserId, 4, '#', 'F', '0', 'motorclub:user:permission', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubUserId and perms = 'motorclub:user:permission');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
select '入会申请', @motorclubRootId, 2, '/motorclub/application', 'C', '0', 'motorclub:application:view', '#', 'admin', sysdate(), '车友会入会申请菜单'
where not exists (select 1 from sys_menu where menu_name = '入会申请' and parent_id = @motorclubRootId);
select @motorclubApplicationId := menu_id from sys_menu where menu_name = '入会申请' and parent_id = @motorclubRootId limit 1;
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '入会申请查询', @motorclubApplicationId, 1, '#', 'F', '0', 'motorclub:application:list', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubApplicationId and perms = 'motorclub:application:list');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '入会申请审核', @motorclubApplicationId, 2, '#', 'F', '0', 'motorclub:application:review', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubApplicationId and perms = 'motorclub:application:review');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
select '活动管理', @motorclubRootId, 3, '/motorclub/activity', 'C', '0', 'motorclub:activity:view', '#', 'admin', sysdate(), '车友会活动菜单'
where not exists (select 1 from sys_menu where menu_name = '活动管理' and parent_id = @motorclubRootId);
select @motorclubActivityId := menu_id from sys_menu where menu_name = '活动管理' and parent_id = @motorclubRootId limit 1;
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '活动查询', @motorclubActivityId, 1, '#', 'F', '0', 'motorclub:activity:list', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubActivityId and perms = 'motorclub:activity:list');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '活动新增', @motorclubActivityId, 2, '#', 'F', '0', 'motorclub:activity:add', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubActivityId and perms = 'motorclub:activity:add');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '活动编辑', @motorclubActivityId, 3, '#', 'F', '0', 'motorclub:activity:edit', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubActivityId and perms = 'motorclub:activity:edit');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '活动删除', @motorclubActivityId, 4, '#', 'F', '0', 'motorclub:activity:remove', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubActivityId and perms = 'motorclub:activity:remove');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
select '活动报名', @motorclubRootId, 4, '/motorclub/registration', 'C', '0', 'motorclub:registration:view', '#', 'admin', sysdate(), '车友会活动报名菜单'
where not exists (select 1 from sys_menu where menu_name = '活动报名' and parent_id = @motorclubRootId);
select @motorclubRegistrationId := menu_id from sys_menu where menu_name = '活动报名' and parent_id = @motorclubRootId limit 1;
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '活动报名查询', @motorclubRegistrationId, 1, '#', 'F', '0', 'motorclub:registration:list', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubRegistrationId and perms = 'motorclub:registration:list');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '活动报名审核', @motorclubRegistrationId, 2, '#', 'F', '0', 'motorclub:registration:review', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubRegistrationId and perms = 'motorclub:registration:review');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
select '签到管理', @motorclubRootId, 5, '/motorclub/checkin', 'C', '0', 'motorclub:checkin:view', '#', 'admin', sysdate(), '车友会签到管理菜单'
where not exists (select 1 from sys_menu where menu_name = '签到管理' and parent_id = @motorclubRootId);
select @motorclubCheckinId := menu_id from sys_menu where menu_name = '签到管理' and parent_id = @motorclubRootId limit 1;
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '签到活动查询', @motorclubCheckinId, 1, '#', 'F', '0', 'motorclub:checkin:list', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubCheckinId and perms = 'motorclub:checkin:list');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '签到码生成', @motorclubCheckinId, 2, '#', 'F', '0', 'motorclub:checkin:generate', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubCheckinId and perms = 'motorclub:checkin:generate');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
select 'Banner管理', @motorclubRootId, 6, '/motorclub/banner', 'C', '0', 'motorclub:banner:view', '#', 'admin', sysdate(), '车友会Banner菜单'
where not exists (select 1 from sys_menu where menu_name = 'Banner管理' and parent_id = @motorclubRootId);
select @motorclubBannerId := menu_id from sys_menu where menu_name = 'Banner管理' and parent_id = @motorclubRootId limit 1;
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select 'Banner查询', @motorclubBannerId, 1, '#', 'F', '0', 'motorclub:banner:list', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubBannerId and perms = 'motorclub:banner:list');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select 'Banner新增', @motorclubBannerId, 2, '#', 'F', '0', 'motorclub:banner:add', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubBannerId and perms = 'motorclub:banner:add');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select 'Banner编辑', @motorclubBannerId, 3, '#', 'F', '0', 'motorclub:banner:edit', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubBannerId and perms = 'motorclub:banner:edit');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select 'Banner删除', @motorclubBannerId, 4, '#', 'F', '0', 'motorclub:banner:remove', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubBannerId and perms = 'motorclub:banner:remove');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
select '权益管理', @motorclubRootId, 7, '/motorclub/benefit', 'C', '0', 'motorclub:benefit:view', '#', 'admin', sysdate(), '车友会权益菜单'
where not exists (select 1 from sys_menu where menu_name = '权益管理' and parent_id = @motorclubRootId);
select @motorclubBenefitId := menu_id from sys_menu where menu_name = '权益管理' and parent_id = @motorclubRootId limit 1;
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '权益查询', @motorclubBenefitId, 1, '#', 'F', '0', 'motorclub:benefit:list', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubBenefitId and perms = 'motorclub:benefit:list');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '权益新增', @motorclubBenefitId, 2, '#', 'F', '0', 'motorclub:benefit:add', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubBenefitId and perms = 'motorclub:benefit:add');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '权益编辑', @motorclubBenefitId, 3, '#', 'F', '0', 'motorclub:benefit:edit', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubBenefitId and perms = 'motorclub:benefit:edit');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '权益删除', @motorclubBenefitId, 4, '#', 'F', '0', 'motorclub:benefit:remove', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubBenefitId and perms = 'motorclub:benefit:remove');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
select '系统配置', @motorclubRootId, 8, '/motorclub/setting', 'C', '0', 'motorclub:setting:view', '#', 'admin', sysdate(), '车友会系统配置菜单'
where not exists (select 1 from sys_menu where menu_name = '系统配置' and parent_id = @motorclubRootId);
select @motorclubSettingId := menu_id from sys_menu where menu_name = '系统配置' and parent_id = @motorclubRootId limit 1;
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '系统配置查看', @motorclubSettingId, 1, '#', 'F', '0', 'motorclub:setting:list', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubSettingId and perms = 'motorclub:setting:list');
insert into sys_menu(menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time) select '系统配置保存', @motorclubSettingId, 2, '#', 'F', '0', 'motorclub:setting:edit', '#', 'admin', sysdate() where not exists (select 1 from sys_menu where parent_id = @motorclubSettingId and perms = 'motorclub:setting:edit');
