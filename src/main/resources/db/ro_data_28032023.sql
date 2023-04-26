-- RO data Table MYSQL

CREATE TABLE `ro_data` (
  `id` int NOT NULL AUTO_INCREMENT,
  `service_center_location` varchar(255) NOT NULL,
  `service_center_code` varchar(255) NOT NULL,
  `bill_no` varchar(255) NOT NULL,
  `bill_date` varchar(255) NOT NULL,
  `bill_type` varchar(255) NOT NULL,
  `customer_name` varchar(255) NOT NULL,
  `mobile_number` varchar(255) NOT NULL,
  `vin` varchar(255) NOT NULL,
  `vehicle_reg_no` varchar(255) NOT NULL,
  `model` varchar(255) NOT NULL,
  `ro_number` varchar(255) NOT NULL,
  `ro_date` varchar(255) NOT NULL,
  `technician` varchar(255) NOT NULL,
  `service_advisor` varchar(255) NOT NULL,
  `service_type` varchar(255) NOT NULL,
  `total_bill_amount` double NOT NULL,
  `labour_amount` double NOT NULL,
  `labour_tax` double NOT NULL,
  `part_amount` double NOT NULL,
  `part_tax` double NOT NULL,
PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;


ALTER TABLE `vehicle-services`.service_follow_up_activity ADD FOLLOW_UP_STATUS  enum('ATTEMPTED','CONTACTED','BOOKED') DEFAULT NULL;
ALTER TABLE `vehicle-services`.service_follow_up_activity ADD ro_id int DEFAULT NULL;
ALTER TABLE `vehicle-services`.service_follow_up_activity ADD CONSTRAINT FK_RO_ID_SERVICE_FOLLOW_UP_ACTIVITY FOREIGN KEY (ro_id) REFERENCES ro_data(id);