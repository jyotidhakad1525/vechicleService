package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.VehicleInsurance;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleInsuranceRepository extends CrudRepository<VehicleInsurance, Integer>,
        JpaSpecificationExecutor<VehicleInsurance> {

    VehicleInsurance findByInsuranceIdentifier(final String insuranceIdentifier);

    void deleteByIdAndServiceVehicle_RegNumber(int insuranceId, final String regNumber);

    VehicleInsurance findByIdAndServiceVehicle_RegNumber(int insuranceId, String vehicleRegNumber);
}
