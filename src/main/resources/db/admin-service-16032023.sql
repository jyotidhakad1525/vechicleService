-- 16-03-2023[Department Table MYSQL]

CREATE TABLE `department_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `department_name` varchar(255) NOT NULL,
  `department_value` int NOT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
`created_datetime` datetime DEFAULT NULL,
`updated_datetime` datetime DEFAULT NULL,
`org_id` int NOT NULL,
PRIMARY KEY (`id`) USING BTREE,
KEY `fk_organization` (`org_id`),
KEY `department_dn_dv_s` (`department_name`,`department_value`,`status`),
CONSTRAINT `fk_organization` FOREIGN KEY (`org_id`) REFERENCES `md_organization` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;


--  16-03-2023 [Designation Table MYSQL]

CREATE TABLE `designation_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `designation_name` varchar(255) NOT NULL,
  `designation_value` int NOT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
`created_datetime` datetime DEFAULT NULL,
`updated_datetime` datetime DEFAULT NULL,
`department_id` int NOT NULL,
`org_id` int NOT NULL,
PRIMARY KEY (`id`) USING BTREE,
KEY `fk_department` (`department_id`),
KEY `fk_organization_key1` (`org_id`),
KEY `designation_dn_dv_s` (`designation_name`,`designation_value`,`status`),
CONSTRAINT `fk_department` FOREIGN KEY (`department_id`) REFERENCES `department_detail` (`id`),
CONSTRAINT `fk_organization_key1` FOREIGN KEY (`org_id`) REFERENCES `md_organization` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;


--  16-03-2023 [Message Template Table MYSQL]

CREATE TABLE `pre_define_template` (
  `id` int NOT NULL AUTO_INCREMENT,
`template_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
`template` text NOT NULL,
`template_type` int NOT NULL,
`status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
`created_datetime` datetime DEFAULT NULL,
`updated_datetime` datetime DEFAULT NULL,
`org_id` int NOT NULL,
PRIMARY KEY (`id`) USING BTREE,
KEY `fk_organization_pre_define_template` (`org_id`),
KEY `pre_define_template_tn_s` (`template_name`,`status`,`template_type`),
KEY `pre_define_template_tt` (`template_type`),
CONSTRAINT `fk_organization_pre_define_template` FOREIGN KEY (`org_id`) REFERENCES `md_organization` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb3;

--  16-03-2023 [Target Configuration Table MYSQL]

CREATE TABLE `target_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `dealer_id` int NOT NULL,
  `department_id` int NOT NULL,
  `designation_id` int NOT NULL,
  `created_datetime` datetime DEFAULT NULL,
  `updated_datetime` datetime DEFAULT NULL,
  `org_id` int NOT NULL,
PRIMARY KEY (`id`) USING BTREE,
KEY `fk_target_details_department` (`department_id`),
KEY `fk_target_details_organization_key` (`org_id`),
KEY `fk_target_details_designation_key` (`designation_id`),
CONSTRAINT `fk_target_details_department` FOREIGN KEY (`department_id`) REFERENCES `department_detail` (`id`),
CONSTRAINT `fk_target_details_designation_key` FOREIGN KEY (`designation_id`) REFERENCES `designation_detail` (`id`),
CONSTRAINT `fk_target_details_organization_key` FOREIGN KEY (`org_id`) REFERENCES `md_organization` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `target_parameter_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `target` int NOT NULL,
  `unit` int NOT NULL,
  `target_id` int NOT NULL,
PRIMARY KEY (`id`) USING BTREE,
KEY `fk_target_parameter_details_target_details_key` (`target_id`),
CONSTRAINT `fk_target_parameter_details_target_details_key` FOREIGN KEY (`target_id`) REFERENCES `target_details` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;

--  16-03-2023 [Service Logic Configuration Table MYSQL]

CREATE TABLE `service_logic_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `service_type` int NOT NULL,
  `sub_service_type` int NOT NULL,
  `start_day` int NOT NULL,
  `end_day` int NOT NULL,
  `km_start` int NOT NULL,
  `km_end` int NOT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
`created_datetime` datetime DEFAULT NULL,
`updated_datetime` datetime DEFAULT NULL,
`org_id` int NOT NULL,
PRIMARY KEY (`id`) USING BTREE,
KEY `fk_service_logic_details_organization_key` (`org_id`),
KEY `fk_service_logic_details_service_type_key` (`service_type`),
KEY `fk_service_logic_details_sub_service_type_key` (`sub_service_type`),
KEY `service_logic_details_st_s` (`service_type`,`status`),
CONSTRAINT `fk_service_logic_details_sub_service_type_key` FOREIGN KEY (`sub_service_type`) REFERENCES `md_service_type` (`ID`),
CONSTRAINT `fk_service_logic_details_service_type_key` FOREIGN KEY (`service_type`) REFERENCES `md_service_category` (`ID`),
CONSTRAINT `fk_service_logic_details_organization_key` FOREIGN KEY (`org_id`) REFERENCES `md_organization` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;



--  16-03-2023 [Service Logic Reminder Table MYSQL]

CREATE TABLE `service_logic_reminder` (
  `id` int NOT NULL AUTO_INCREMENT,
  `service_type` int NOT NULL,
  `sub_service_type` int NOT NULL,
  `reminder_days` int NOT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
`created_datetime` datetime DEFAULT NULL,
`updated_datetime` datetime DEFAULT NULL,
`org_id` int NOT NULL,
PRIMARY KEY (`id`) USING BTREE,
KEY `fk_service_logic_reminder_service_type_key` (`service_type`),
KEY `fk_service_logic_reminder_sub_service_type_key` (`sub_service_type`),
KEY `fk_service_logic_reminder_organization_key` (`org_id`),
KEY `service_logic_reminder_st_s_rd` (`service_type`,`status`,`reminder_days`),
CONSTRAINT `fk_service_logic_reminder_sub_service_type_key` FOREIGN KEY (`sub_service_type`) REFERENCES `md_service_type` (`ID`),
CONSTRAINT `fk_service_logic_reminder_service_type_key` FOREIGN KEY (`service_type`) REFERENCES `md_service_category` (`ID`),
CONSTRAINT `fk_service_logic_reminder_organization_key` FOREIGN KEY (`org_id`) REFERENCES `md_organization` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;

--  16-03-2023 [Service Follow-Up Reason]

CREATE TABLE `service_follow_up_reason` (
  `id` int NOT NULL AUTO_INCREMENT,
  `service_type` int NOT NULL,
  `sub_service_type` int NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
`created_datetime` datetime DEFAULT NULL,
`updated_datetime` datetime DEFAULT NULL,
`org_id` int NOT NULL,
PRIMARY KEY (`id`) USING BTREE,
KEY `fk_service_follow_up_reason_service_type_key` (`service_type`),
KEY `fk_service_follow_up_reason_sub_service_type_key` (`sub_service_type`),
KEY `fk_service_follow_up_reason_organization_key` (`org_id`),
KEY `service_follow_up_reason_st_s_r` (`service_type`,`status`,`reason`),
CONSTRAINT `fk_service_follow_up_reason_sub_service_type_key` FOREIGN KEY (`sub_service_type`) REFERENCES `md_service_type` (`ID`),
CONSTRAINT `fk_service_follow_up_reason_service_type_key` FOREIGN KEY (`service_type`) REFERENCES `md_service_category` (`ID`),
CONSTRAINT `fk_service_follow_up_reason_organization_key` FOREIGN KEY (`org_id`) REFERENCES `md_organization` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;