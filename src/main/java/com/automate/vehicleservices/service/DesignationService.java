package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.DesignationRequest;
import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.common.VehicleServiceConstants;
import com.automate.vehicleservices.entity.DepartmentDetail;
import com.automate.vehicleservices.entity.DesignationDetail;
import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.DesignationRepository;
import com.automate.vehicleservices.service.dto.CommonIdNameDTO;
import com.automate.vehicleservices.service.dto.DesignationDTO;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DesignationService {

    private final ModelMapper mapper;

    private final DesignationRepository designationRepository;

    private final DepartmentService departmentService;

    public DesignationService(ModelMapper mapper, DesignationRepository designationRepository, DepartmentService departmentService) {
        this.mapper = mapper;
        this.designationRepository = designationRepository;
        this.departmentService = departmentService;
    }

    @Transactional
    public DesignationDTO saveAndUpdateDepartment(DesignationRequest designationRequest, MdOrganization mdOrganization) {
        Date date = Calendar.getInstance().getTime();
        DesignationDetail designationDetail;
        if (Objects.nonNull(designationRequest.getId())) {
            designationDetail = getDesignationDetail(designationRequest.getId(), mdOrganization.getId());
        } else {
            designationDetail = new DesignationDetail();
            designationDetail.setCreatedDatetime(date);
            designationDetail.setOrganization(mdOrganization);
        }
        DepartmentDetail departmentDetail = departmentService.fetchDepartmentEntityBasedOnId(designationRequest.getDepartmentId(), mdOrganization.getId());
        designationDetail.setDesignationName(designationRequest.getDesignationName());
        designationDetail.setDesignationValue(designationRequest.getDesignationValue());
        designationDetail.setStatus(designationRequest.getStatus());
        designationDetail.setUpdatedDatetime(date);
        designationDetail.setDepartmentDetail(departmentDetail);
        final var result = designationRepository.save(designationDetail);
        return new DesignationDTO(result);
    }

    private DesignationDetail getDesignationDetail(Integer id, int orgId) {
        Optional<DesignationDetail> designationDetailOptional = designationRepository.findByIdAndOrganizationId(id, orgId);
        if (!designationDetailOptional.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Designation details not found with given id " + id);
        return designationDetailOptional.get();
    }

    @Transactional
    public PaginatedSearchResponse<DesignationDTO> fetchAllDesignation(Pagination page, int orgId) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize(), Sort.by(VehicleServiceConstants.CREATED_DATE_TIME).descending());
        final var pageResult = designationRepository.findAllByOrganizationId(orgId, pageable);
        final var list = pageResult.getContent().stream().map(data -> new DesignationDTO(data)).collect(Collectors.toList());
        return new PaginatedSearchResponse<>(pageResult, list);
    }

    @Transactional
    public DesignationDTO fetchDesignationBasedOnId(Integer id, int orgId) {
        return new DesignationDTO(getDesignationDetail(id, orgId));
    }

    @Transactional
    public DesignationDetail fetchDesignationEntityBasedOnIdAndDepartmentId(Integer id, Integer departmentId, int orgId) {
        Optional<DesignationDetail> designationDetailOptional = designationRepository.findByIdAndOrganizationIdAndDepartmentDetailId(id, orgId, departmentId);
        if (!designationDetailOptional.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Designation details not found with given designation id " + id + " And department id = " + departmentId);
        return designationDetailOptional.get();
    }

    @Transactional
    public void deleteDesignationBasedOnId(Integer id, int orgId) {
        DesignationDetail designationDetail = getDesignationDetail(id, orgId);
        designationRepository.delete(designationDetail);
    }

    @Transactional
    public PaginatedSearchResponse<DesignationDTO> search(DesignationRequest designationRequest, Pagination page, int orgId) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize(), Sort.by("created_datetime").descending());
        final var pageResult = designationRepository.search(designationRequest.getDesignationName(), designationRequest.getDesignationValue(), designationRequest.getStatus(), designationRequest.getDepartmentId(), orgId, pageable);
        final var list = pageResult.getContent().stream().map(data -> new DesignationDTO(data)).collect(Collectors.toList());
        return new PaginatedSearchResponse<>(pageResult, list);
    }

    @Transactional
    public Object fetchDesignationBasedOnDepartmentId(Integer departmentId, int orgId) {

        final var result = designationRepository.findByDepartmentDetailIdAndOrganizationId(departmentId, orgId);

        return result.stream().map(data -> new CommonIdNameDTO(data.getId(), data.getDesignationName())).collect(Collectors.toList());
    }

}
