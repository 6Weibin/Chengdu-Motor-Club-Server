-- 扩展新闻状态字段以支持逻辑删除状态
-- by AI.Coding

alter table mc_news
    modify column status varchar(20) not null default '0' comment '状态（0上线 1下线 deleted已删除）';
