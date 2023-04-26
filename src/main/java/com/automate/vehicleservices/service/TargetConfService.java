package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.api.model.TargetConfigurationRequest;
import com.automate.vehicleservices.common.VehicleServiceConstants;
import com.automate.vehicleservices.entity.DepartmentDetail;
import com.automate.vehicleservices.entity.DesignationDetail;
import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.entity.TargetConfigurationDetails;
import com.automate.vehicleservices.entity.TargetConfigurationParameterDetails;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.TargetConfParameterRepository;
import com.automate.vehicleservices.repository.TargetConfRepository;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
import com.automate.vehicleservices.service.dto.TargetConfDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TargetConfService {

    private final TargetConfRepository targetConfRepository;
    private final DepartmentService departmentService;
    private final DesignationService designationService;

    private final TargetConfParameterRepository targetConfParameterRepository;

    public TargetConfService(TargetConfRepository targetConfRepository, DepartmentService departmentService, DesignationService designationService, TargetConfParameterRepository targetConfParameterRepository) {
        this.targetConfRepository = targetConfRepository;
        this.departmentService = departmentService;
        this.designationService = designationService;
        this.targetConfParameterRepository = targetConfParameterRepository;
    }


    @Transactional
    public TargetConfDTO addOrUpdateTargetConf(TargetConfigurationRequest targetConfigurationRequest, MdOrganization mdOrganization) {
        Date date = Calendar.getInstance().getTime();
        TargetConfigurationDetails targetConfigurationDetails;
        if (Objects.nonNull(targetConfigurationRequest.getId())) {
            // Update Call
            targetConfigurationDetails = getTargetConfigurationDetails(targetConfigurationRequest.getId(), mdOrganization.getId());
            targetConfParameterRepository.deleteByTargetConfigurationDetailsId(targetConfigurationDetails.getId());
        } else {
            // Insert Call
            targetConfigurationDetails = new TargetConfigurationDetails();
            targetConfigurationDetails.setCreatedDatetime(date);
            targetConfigurationDetails.setOrganization(mdOrganization);
        }
        // verify department detail and set
        DepartmentDetail departmentDetail = departmentService.fetchDepartmentEntityBasedOnId(targetConfigurationRequest.getDepartmentId(), mdOrganization.getId());
        targetConfigurationDetails.setDepartmentDetail(departmentDetail);

        // verify designation detail and set
        DesignationDetail designationDetail = designationService.fetchDesignationEntityBasedOnIdAndDepartmentId(targetConfigurationRequest.getDesignationId(),departmentDetail.getId(), mdOrganization.getId());
        targetConfigurationDetails.setDesignationDetail(designationDetail);

        targetConfigurationDetails.setDealerId(targetConfigurationRequest.getDealerId());
        targetConfigurationDetails.setUpdatedDatetime(date);

        List<TargetConfigurationParameterDetails> targetConfigurationParameterDetailsList = new ArrayList<>();
        targetConfigurationRequest.getTargetConfParameter().stream().forEach(targetConfParameter -> {
            TargetConfigurationParameterDetails parameterDetails = new TargetConfigurationParameterDetails();
            parameterDetails.setParameterName(targetConfParameter.getParameterName());
            parameterDetails.setTarget(targetConfParameter.getTarget());
            parameterDetails.setUnit(targetConfParameter.getUnit());
            parameterDetails.setTargetConfigurationDetails(targetConfigurationDetails);
            targetConfigurationParameterDetailsList.add(parameterDetails);
        });

        targetConfigurationDetails.setTargetConfigurationParameterDetails(targetConfigurationParameterDetailsList);
        final var result = targetConfRepository.save(targetConfigurationDetails);
        return new TargetConfDTO(result);
    }


    private TargetConfigurationDetails getTargetConfigurationDetails(Integer id, Integer orgId) {
        Optional<TargetConfigurationDetails> targetConfigurationDetailsOptional = targetConfRepository.findByIdAndOrganizationId(id, orgId);
        if (!targetConfigurationDetailsOptional.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Target Configuration details not found with given id " + id);

        return targetConfigurationDetailsOptional.get();
    }

    @Transactional
    public PaginatedSearchResponse<TargetConfDTO> fetchAllTargetConf(Pagination page, int orgId) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize(), Sort.by(VehicleServiceConstants.CREATED_DATE_TIME).descending());
        final var pageResult = targetConfRepository.findAllByOrganizationId(orgId, pageable);
        final var list = pageResult.getContent().stream().map(data -> new TargetConfDTO(data)).collect(Collectors.toList());
        return new PaginatedSearchResponse<>(pageResult, list);
    }

    @Transactional
    public TargetConfDTO fetchTargetConfBasedOnId(Integer id, int orgId) {
        return new TargetConfDTO(getTargetConfigurationDetails(id, orgId));
    }

    @Transactional
    public List<TargetConfDTO> fetchAllTargetConf(int orgId) {
        final var pageResult = targetConfRepository.findAllByOrganizationId(orgId);
        final var list = pageResult.stream().map(data -> new TargetConfDTO(data)).collect(Collectors.toList());
        return list;
    }

    @Transactional
    public TargetConfDTO fetchTargetConfBasedDealerCode(String dealerCode, int orgId) {
        Optional<TargetConfigurationDetails> targetConfigurationDetailsOptional = targetConfRepository.findByDealerIdAndOrganizationId(dealerCode, orgId);
        if (!targetConfigurationDetailsOptional.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Target Configuration details not found with given dealerCode " + dealerCode);
        return new TargetConfDTO(targetConfigurationDetailsOptional.get());
    }

    @Transactional
    public void deleteTargetConfBasedOnId(Integer id, int orgId) {
        TargetConfigurationDetails targetConfigurationDetails = getTargetConfigurationDetails(id, orgId);
        targetConfRepository.delete(targetConfigurationDetails);
    }
}
