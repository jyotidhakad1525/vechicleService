-- Which is contain only org allocation type
CREATE TABLE `org_data_allocation_strategy_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `allocation_type` enum('ROUND_ROBIN','MANUAL') DEFAULT NULL,
`status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
`created_datetime` datetime DEFAULT NULL,
`updated_datetime` datetime DEFAULT NULL,
`org_id` int NOT NULL,
PRIMARY KEY (`id`) USING BTREE,
KEY `fk_organization_org_data_allocation_strategy_type` (`org_id`),
CONSTRAINT `fk_organization_org_data_allocation_strategy_type` FOREIGN KEY (`org_id`) REFERENCES `md_organization` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;

CREATE INDEX index_status_org_Id_org_data_allocation_strategy_type ON org_data_allocation_strategy_type (status,org_id)

-- which store round robin allocation data cre wise
CREATE TABLE `round_robin_data_allocation_strategy` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cre_id` int NOT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
`is_number` tinyint(0) DEFAULT '0',
`created_datetime` datetime DEFAULT NULL,
`updated_datetime` datetime DEFAULT NULL,
`org_id` int NOT NULL,
PRIMARY KEY (`id`) USING BTREE,
KEY `fk_organization_round_robin_data_allocation_strategy` (`org_id`),
KEY `fk_employee_round_robin_data_allocation_strategy` (`cre_id`),
CONSTRAINT `fk_organization_round_robin_data_allocation_strategy` FOREIGN KEY (`org_id`) REFERENCES `md_organization` (`ID`),
CONSTRAINT `fk_employee_round_robin_data_allocation_strategy` FOREIGN KEY (`cre_id`) REFERENCES `employee_details` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;



-- which store cre based service type ,ratio details in feature we need to store location so also add here
CREATE TABLE `service_type_allocation_ratio` (
  `id` int NOT NULL AUTO_INCREMENT,
  `service_type_id` int NOT NULL,
  `ratio` int NOT NULL,
  `round_robin_allocation_id` int NOT NULL,
PRIMARY KEY (`id`) USING BTREE,
KEY `fk_service_type_service_type_allocation_ratio` (`service_type_id`),
KEY `fk_round_robin_data_service_type_allocation_ratio` (`round_robin_allocation_id`),
CONSTRAINT `fk_service_type_service_type_allocation_ratio` FOREIGN KEY (`service_type_id`) REFERENCES `md_service_type` (`ID`),
CONSTRAINT `fk_round_robin_data_service_type_allocation_ratio` FOREIGN KEY (`round_robin_allocation_id`) REFERENCES `round_robin_data_allocation_strategy` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;
