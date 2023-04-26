package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.VehicleInsuranceUpdateRequest;
import com.automate.vehicleservices.entity.VehicleInsurance;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.VehicleInsuranceRepository;
import com.automate.vehicleservices.service.dto.VehicleInsuranceDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class VehicleInsuranceService {

    private final VehicleInsuranceRepository vehicleInsuranceRepository;

    public VehicleInsuranceService(VehicleInsuranceRepository vehicleInsuranceRepository) {
        this.vehicleInsuranceRepository = vehicleInsuranceRepository;
    }

    @Transactional
    public VehicleInsurance findByVehiclePolicyNumber(final String policyNumber) {
        return vehicleInsuranceRepository.findByInsuranceIdentifier(policyNumber);

    }

    @Transactional
    public void deleteVehicleInsurance(String vehicleRegNumber, int insuranceId) {
        vehicleInsuranceRepository.deleteByIdAndServiceVehicle_RegNumber(insuranceId, vehicleRegNumber);
    }


    @Transactional
    public VehicleInsuranceDTO updateVehicleInsurance(VehicleInsuranceUpdateRequest vehicleInsuranceUpdateRequest,
                                                      String vehicleRegNumber, int insuranceId) {

        final var vehicleInsurance =
                vehicleInsuranceRepository.findByIdAndServiceVehicle_RegNumber(insuranceId, vehicleRegNumber);
        if (Objects.isNull(vehicleInsurance))
            throw new VehicleServicesException(String.format("Insurance details not found for the given Vehicle %s " +
                    "and Insurance id: %d", vehicleRegNumber, insuranceId));

        vehicleInsurance.setEndDate(vehicleInsuranceUpdateRequest.getEndDate());
        vehicleInsurance.setStartDate(vehicleInsuranceUpdateRequest.getStartDate());
        vehicleInsurance.setInsuranceAmount(vehicleInsuranceUpdateRequest.getInsuranceAmount());
        vehicleInsurance.setInsuranceIdentifier(vehicleInsuranceUpdateRequest.getInsuranceIdentifier());
        vehicleInsurance.setProvider(vehicleInsuranceUpdateRequest.getVendor());

        final var save = vehicleInsuranceRepository.save(vehicleInsurance);

        return new VehicleInsuranceDTO(save);

    }
}
