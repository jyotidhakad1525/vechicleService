package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.VehicleWarrantyRequest;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.VehicleWarrantyRepository;
import com.automate.vehicleservices.service.dto.VehicleWarrantyDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VehicleWarrantyService {

    private final VehicleWarrantyRepository warrantyRepository;

    public VehicleWarrantyService(VehicleWarrantyRepository warrantyRepository) {
        this.warrantyRepository = warrantyRepository;
    }

    @Transactional
    public void deleteVehicleWarranty(String vehicleRegNumber, int warrantyId) {
        warrantyRepository.deleteByIdAndServiceVehicle_RegNumber(warrantyId, vehicleRegNumber);
    }


    @Transactional
    public VehicleWarrantyDTO updateVehicleWarranty(VehicleWarrantyRequest vehicleWarrantyRequest,
                                                    String vehicleRegNumber, int warrantyId) {

        final var vehicleWarranty = warrantyRepository.findByIdAndServiceVehicle_RegNumber(warrantyId,
                vehicleRegNumber);
        if (Objects.isNull(vehicleWarranty))
            throw new VehicleServicesException(String.format("Warranty details not found for the given Vehicle %s " +
                    "and Warranty id: %d", vehicleRegNumber, warrantyId));

        vehicleWarranty.setExpiryDate(vehicleWarrantyRequest.getExpiryDate());
        vehicleWarranty.setStartDate(vehicleWarrantyRequest.getStartDate());
        vehicleWarranty.setAmountPaid(vehicleWarrantyRequest.getAmountPaid());
        vehicleWarranty.setWarrantyTpe(vehicleWarrantyRequest.getWarrantyType());
        final var save = warrantyRepository.save(vehicleWarranty);

        return new VehicleWarrantyDTO(save);
    }

    @Transactional
    public List<VehicleWarrantyDTO> fetchWarrantiesByVehicleRegNumber(String vehicleRegNumber) {

        final var vehicleWarranties =
                warrantyRepository.findByServiceVehicle_RegNumberAndExpiryDateGreaterThanEqual(vehicleRegNumber,
                        LocalDate.now());

        if (CollectionUtils.isEmpty(vehicleWarranties))
            return Collections.emptyList();

        return vehicleWarranties.stream().map(VehicleWarrantyDTO::new).collect(Collectors.toList());
    }
}
