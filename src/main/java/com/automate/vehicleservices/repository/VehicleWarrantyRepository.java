package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.VehicleInsurance;
import com.automate.vehicleservices.entity.VehicleWarranty;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VehicleWarrantyRepository extends CrudRepository<VehicleWarranty, Integer>,
        JpaSpecificationExecutor<VehicleInsurance> {
    void deleteByIdAndServiceVehicle_RegNumber(int warrantyId, String vehicleRegNumber);

    VehicleWarranty findByIdAndServiceVehicle_RegNumber(int warrantyId, String vehicleRegNumber);

    List<VehicleWarranty> findByServiceVehicle_RegNumberAndExpiryDateGreaterThanEqual(String vehicleRegNumber,
                                                                                      LocalDate date);
}
