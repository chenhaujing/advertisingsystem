

SET autocommit = 0;

START TRANSACTION;

--  更新合同总价格
UPDATE contract c SET c.total_cost = (SELECT  ROUND( (medium_cost + product_cost) * IFNULL(discount,1) , 2 ) total_cost FROM contract_extension ce WHERE ce.id = c.id);


-- 更新合同投放总周期,单位(天)
UPDATE contract c SET c.contract_period = (SELECT  TIMESTAMPDIFF(DAY,ce.start_time, ce.end_time) FROM contract_extension ce WHERE ce.id = c.id);


-- 插入展示次数配置
INSERT INTO `advertisement_display_times_config` (`id`, `created_time`, `creator_id`, `updated_time`, `display_times`, `time_unit`) VALUES
	('193235AD979D11E795D72908B0B36C35','2017-09-12 09:31:04','1','2017-09-13 07:48:51','80','1'),
	('95B14FD8979B11E795D7CFA56470D819','2017-09-12 09:20:14','1','2017-09-13 07:32:44','1','1');


-- 插入广告时长配置
INSERT INTO `advertisement_duration_config` (`id`, `created_time`, `creator_id`, `updated_time`, `duration`, `duration_unit`) VALUES
	('15218F66979D11E795D753D7B173269B','2017-09-12 09:30:57','1','2017-09-12 09:30:57','15','3'),
	('91B25CD3979B11E795D7AF8F0225B600','2017-09-12 09:20:07','1','2017-09-12 09:37:11','1','1');


-- 插入广告尺寸配置
INSERT INTO `advertisement_size_config` (`id`, `created_time`, `creator_id`, `updated_time`, `high_ratio`, `horizontal_resolution`, `size_name`, `vertical_resolution`, `width_ratio`) VALUES
('E0C7C796986411E7BA6659CF7F93FD29','2017-09-13 09:21:09','1','2017-09-14 18:37:41','1024','1821','待机全屏广告','1024','1363'),
('F444CB39986411E7BA6661750A339811','2017-09-13 09:21:42','1','2017-09-13 09:21:42','3','1363','扫描支付页面','1024','4');


COMMIT;



DELIMITER //
CREATE PROCEDURE contract_config_pro()
BEGIN
    DECLARE contract_id VARCHAR(36);
    DECLARE _display_times INT;
    DECLARE _duration INT;
    DECLARE _duration_unit INT;
    DECLARE _high_ratio INT;
    DECLARE _horizontal_resolution INT;
    DECLARE _size_name VARCHAR(50);
    DECLARE _time_unit INT;
    DECLARE _vertical_resolution INT;
    DECLARE _width_ratio INT;
    DECLARE advertisement_display_times_config_id VARCHAR(36);
    DECLARE advertisement_duration_config_id VARCHAR(36);
    DECLARE advertisement_size_config_id VARCHAR(36);
    
    DECLARE cursor_pointer CURSOR FOR SELECT id FROM contract;
    
    DECLARE EXIT HANDLER FOR NOT FOUND CLOSE cursor_pointer;

    OPEN cursor_pointer;

    SELECT id, display_times, time_unit FROM advertisement_display_times_config WHERE id = '193235AD979D11E795D72908B0B36C35' INTO advertisement_display_times_config_id, _display_times, _time_unit;
    SELECT id, duration, duration_unit FROM advertisement_duration_config WHERE id = '15218F66979D11E795D753D7B173269B' INTO advertisement_duration_config_id, _duration, _duration_unit;
    SELECT id, high_ratio, horizontal_resolution, size_name, vertical_resolution, width_ratio FROM advertisement_size_config WHERE id = 'E0C7C796986411E7BA6659CF7F93FD29' INTO advertisement_size_config_id, _high_ratio, _horizontal_resolution, _size_name, _vertical_resolution, _width_ratio;
    
    ins: LOOP
        FETCH cursor_pointer INTO contract_id;
        INSERT INTO `contract_advertisement_config` (
	  `id`,
	  `display_times`,
	  `duration`,
	  `duration_unit`,
	  `high_ratio`,
	  `horizontal_resolution`,
	  `size_name`,
	  `time_unit`,
	  `vertical_resolution`,
	  `width_ratio`,
	  `advertisement_display_times_config_id`,
	  `advertisement_duration_config_id`,
	  `advertisement_size_config_id`
	) 
	VALUES
	  (
	    contract_id,
	    _display_times,
	    _duration,
	    _duration_unit,
	    _high_ratio,
	    _horizontal_resolution,
	    _size_name,
	    _time_unit,
	    _vertical_resolution,
	    _width_ratio,
	    advertisement_display_times_config_id,
	    advertisement_duration_config_id,
	    advertisement_size_config_id
	  );
    END LOOP ins;  
END;
//
DELIMITER ;


START TRANSACTION;

call contract_config_pro();


COMMIT;

DROP PROCEDURE contract_config_pro;

SET autocommit = 1;

-- 去除广告配置不必要的字段
ALTER TABLE contract_extension DROP COLUMN play_duration;

ALTER TABLE contract_extension DROP COLUMN play_time;

ALTER TABLE contract_extension DROP COLUMN vision_length;

ALTER TABLE contract_extension DROP COLUMN vision_width;
