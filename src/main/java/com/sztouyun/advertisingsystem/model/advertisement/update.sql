alter table contract add COLUMN contract_period INTEGER(11) not Null DEFAULT 0;
alter table contract add COLUMN used_contract_period INTEGER(11) not Null DEFAULT 0;

alter table advertisement add COLUMN advertisement_period INTEGER(11) not Null DEFAULT 0;
alter table advertisement add COLUMN effective_end_time datetime ;
alter table advertisement add COLUMN effective_start_time  datetime ;
alter table advertisement add COLUMN expected_due_day  datetime ;
alter table advertisement add COLUMN delivery_operator_id  VARCHAR(36) DEFAULT NULL;


update contract set contract_period=DATEDIFF(end_time,start_time);
update  advertisement set advertisement_period=DATEDIFF(end_time,start_time);
