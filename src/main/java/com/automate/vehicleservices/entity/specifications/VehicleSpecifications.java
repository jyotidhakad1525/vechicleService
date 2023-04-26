package com.automate.vehicleservices.entity.specifications;

import com.automate.vehicleservices.entity.ServiceVehicle;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class VehicleSpecifications extends AbstractSpecifications {

    public Specification<ServiceVehicle> vehicleRegNumberContains(String queryTerm) {
        return containsSpecification(queryTerm, "regNumber");
    }

    public Specification<ServiceVehicle> vinContains(String queryTerm) {
        return containsSpecification(queryTerm, "vin");
    }

    public Specification<ServiceVehicle> chassisNumberContains(String queryTerm) {
        return containsSpecification(queryTerm, "chassisNumber");
    }

    public Specification<ServiceVehicle> engineNumberContains(String queryTerm) {
        return containsSpecification(queryTerm, "engineNumber");
    }
}
