package com.automate.vehicleservices.service.facade;

import com.automate.vehicleservices.entity.VehicleInsurance;
import com.automate.vehicleservices.entity.specifications.AbstractSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class VehicleInsuranceSpecifications extends AbstractSpecifications {

    public Specification<VehicleInsurance> policyNumberContains(String queryTerm) {
        return containsSpecification(queryTerm, "insuranceIdentifier");
    }

}
