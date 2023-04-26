package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.DepartmentRequest;
import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.common.VehicleServiceConstants;
import com.automate.vehicleservices.entity.DepartmentDetail;
import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.DepartmentRepository;
import com.automate.vehicleservices.service.dto.DepartmentDTO;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
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
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public DepartmentDTO saveAndUpdateDepartment(DepartmentRequest departmentRequest, MdOrganization organization) {
        Date date = Calendar.getInstance().getTime();
        DepartmentDetail departmentDetail;
        if (Objects.nonNull(departmentRequest.getId())) {
            departmentDetail = getDepartmentDetail(departmentRequest.getId(), organization.getId()).get();
        } else {
            departmentDetail = new DepartmentDetail();
            departmentDetail.setCreatedDatetime(date);
            departmentDetail.setOrganization(organization);
        }
        departmentDetail.setDepartmentName(departmentRequest.getDepartmentName());
        departmentDetail.setDepartmentValue(departmentRequest.getDepartmentValue());
        departmentDetail.setStatus(departmentRequest.getStatus());
        departmentDetail.setUpdatedDatetime(date);
        final var result = departmentRepository.save(departmentDetail);
        return new DepartmentDTO(result);
    }

    @Transactional
    public PaginatedSearchResponse<DepartmentDTO> fetchAllDepartment(Pagination page, int orgId) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize(), Sort.by(VehicleServiceConstants.CREATED_DATE_TIME).descending());
        final var pageResult = departmentRepository.findAllByOrganizationId(orgId, pageable);
        final var list = pageResult.getContent().stream().map(data -> new DepartmentDTO(data)).collect(Collectors.toList());
        return new PaginatedSearchResponse<>(pageResult, list);
    }

    @Transactional
    public DepartmentDTO fetchDepartmentBasedOnId(Integer id, int orgId) {
        return new DepartmentDTO(getDepartmentDetail(id, orgId).get());
    }

    private Optional<DepartmentDetail> getDepartmentDetail(Integer id, int orgId) {
        Optional<DepartmentDetail> departmentDetail = departmentRepository.findByIdAndOrganizationId(id, orgId);
        if (!departmentDetail.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Department details not found with given id " + id);
        return departmentDetail;
    }

    @Transactional
    public DepartmentDetail fetchDepartmentEntityBasedOnId(Integer id, int orgId) {
        return getDepartmentDetail(id, orgId).get();
    }

    @Transactional
    public void deleteDepartmentBasedOnId(Integer id, int orgId) {
        DepartmentDetail departmentDetail = getDepartmentDetail(id, orgId).get();
        if (departmentDetail.getDesignationDetail().size() > 0 && departmentDetail.getTargetConfigurationDetails().size() > 0)
            throw new VehicleServicesException(HttpStatus.CONFLICT, "Department Already in use");
        departmentRepository.delete(departmentDetail);
    }

    @Transactional
    public PaginatedSearchResponse<DepartmentDTO> search(DepartmentRequest departmentRequest, Pagination page, int orgId) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize(), Sort.by("created_datetime").descending());
        final var pageResult = departmentRepository.search(departmentRequest.getDepartmentName(), departmentRequest.getDepartmentValue(), departmentRequest.getStatus(), orgId, pageable);
        final var list = pageResult.getContent().stream().map(data -> new DepartmentDTO(data)).collect(Collectors.toList());
        return new PaginatedSearchResponse<>(pageResult, list);
    }
}
