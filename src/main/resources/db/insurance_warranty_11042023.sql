ALTER TABLE `vehicle-services`.vehicle_insurance MODIFY COLUMN INSURANCE_ID varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL;
ALTER TABLE `vehicle-services`.vehicle_insurance MODIFY COLUMN INSURANCE_END_DATE datetime NULL;
ALTER TABLE `vehicle-services`.vehicle_insurance MODIFY COLUMN PROVIDER varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL;


ALTER TABLE `vehicle-services`.vehicle_warranty MODIFY COLUMN WARRANTY_TYPE enum('OEM','EW','OTHER','MCP') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL;
ALTER TABLE `vehicle-services`.vehicle_warranty MODIFY COLUMN STATUS enum('ACTIVE','EXPIRED','CANCELLED') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'ACTIVE' NULL;
ALTER TABLE `vehicle-services`.vehicle_warranty MODIFY COLUMN EXPIRY_DATE datetime NULL;
ALTER TABLE `vehicle-services`.vehicle_warranty MODIFY COLUMN START_DATE datetime NULL;
