ALTER TABLE `vehicle-services`.customer ADD SALUTATION ENUM('Mr','Mrs','Ms','Dr','M_SLASH_S') NULL;
ALTER TABLE `vehicle-services`.customer ADD RELATION_NAME varchar(100) NULL;
ALTER TABLE `vehicle-services`.customer ADD AGE INT NULL;



ALTER TABLE `vehicle-services`.customer_address ADD HOUSE_NO varchar(100) NULL;
ALTER TABLE `vehicle-services`.customer_address ADD STREET varchar(100) NULL;
ALTER TABLE `vehicle-services`.customer_address ADD VILLAGE_OR_TOWN varchar(100) NULL;
ALTER TABLE `vehicle-services`.customer_address ADD MANDAL_OR_TAHASIL varchar(100) NULL;
ALTER TABLE `vehicle-services`.customer_address ADD IS_URBAN BOOL NULL;


ALTER TABLE `vehicle-services`.service_vehicle ADD CURRENT_KM_READING INT NULL;
ALTER TABLE `vehicle-services`.service_vehicle ADD MAKING_MONTH varchar(100) NULL;
ALTER TABLE `vehicle-services`.service_vehicle ADD MAKING_YEAR INT NULL;
ALTER TABLE `vehicle-services`.service_vehicle ADD IS_FASTAG BOOL NULL;



ALTER TABLE `vehicle-services`.service_vehicle ADD on_going_service_due_date datetime DEFAULT NULL;
ALTER TABLE `vehicle-services`.service_vehicle ADD on_going_service int DEFAULT NULL;
ALTER TABLE `vehicle-services`.service_vehicle ADD CONSTRAINT FK_ON_GOING_SERVICE_ID_SERVICE_VEHICLE FOREIGN KEY (on_going_service) REFERENCES md_service_type(ID);

ALTER TABLE `vehicle-services`.vehicle_warranty ADD `number` varchar(100) NULL;
