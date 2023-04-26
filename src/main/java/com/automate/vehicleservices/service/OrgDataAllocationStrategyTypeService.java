package com.automate.vehicleservices.service;

import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.entity.OrgDataAllocationStrategyType;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import com.automate.vehicleservices.entity.enums.AllocationType;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.OrgDataAllocationStrategyTypeRepository;
import com.automate.vehicleservices.service.dto.OrgDataAllocationStrategyTypeDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrgDataAllocationStrategyTypeService {

    private final OrgDataAllocationStrategyTypeRepository repository;

    @Transactional
    public void changeAllocation(AllocationType allocationType, MdOrganization organization) {
        List<OrgDataAllocationStrategyType> orgDataAllocationStrategyTypeList = repository.findAllByOrganizationId(organization.getId());
        final var updatedList = orgDataAllocationStrategyTypeList.stream().map(data -> {
            if (data.getAllocationType().equals(allocationType)) {
                data.setStatus(ActiveInActiveStatus.ACTIVE);
            } else {
                data.setStatus(ActiveInActiveStatus.INACTIVE);
            }
            data.setUpdatedDatetime(Calendar.getInstance().getTime());
            return data;
        }).collect(Collectors.toList());
        repository.saveAll(updatedList);
    }

    @Transactional
    public OrgDataAllocationStrategyTypeDTO getActiveAllocation(MdOrganization organization) {
        Optional<OrgDataAllocationStrategyType> orgDataAllocationStrategyTypeOptional = repository.findAllByOrganizationIdAndStatus(organization.getId(), ActiveInActiveStatus.ACTIVE);
        if (!orgDataAllocationStrategyTypeOptional.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "active allocation type not found with given org id = " + organization.getId());
        OrgDataAllocationStrategyType orgDataAllocationStrategyType = orgDataAllocationStrategyTypeOptional.get();
        return new OrgDataAllocationStrategyTypeDTO(orgDataAllocationStrategyType.getId(), orgDataAllocationStrategyType.getAllocationType());
    }
}
