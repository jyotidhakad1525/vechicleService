package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.VehicleServiceHistory;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleServiceHistoryRepository extends CrudRepository<VehicleServiceHistory, Integer> {

    List<VehicleServiceHistory> findByServiceVehicle_RegNumberAndMdServiceType_MdTenant_TenantIdentifierAndMdServiceType_MdServiceCategory_ServiceGroupOrderByServiceDateDesc(
            final String vehicleRegNumber, final String tenant, final ServiceGroup categoryName);

    List<VehicleServiceHistory> findByServiceVehicle_RegNumberOrderByServiceDateDesc(String vehicleRegNumber);
}
