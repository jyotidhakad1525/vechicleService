package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.md.MdServiceTypeCriteriaRequest;
import com.automate.vehicleservices.api.md.MdServiceTypeCriteriaResponse;
import com.automate.vehicleservices.entity.MdServiceTypeCriteria;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.builder.MdServiceTypeCriteriaBuilder;
import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.MdRequest;
import com.automate.vehicleservices.framework.api.MdResponse;
import com.automate.vehicleservices.framework.common.DateHelper;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.framework.service.CrudService;
import com.automate.vehicleservices.repository.MdServiceTypeCriteriaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Chandrashekar V
 */
@Component
@Slf4j
public class MdServiceTypeCriteriaService extends AbstractService implements MasterDataService {

    private final MdServiceTypeCriteriaRepository mdServiceTypeCriteriaRepository;
    private final DateHelper dateHelper;
    private final CrudService crudService;
    private final MdServiceTypeService serviceTypeService;

    @Autowired
    public MdServiceTypeCriteriaService(MdServiceTypeCriteriaRepository mdServiceTypeCriteriaRepository,
                                        DateHelper dateHelper, CrudService crudService,
                                        MdServiceTypeService serviceTypeService) {
        this.mdServiceTypeCriteriaRepository = mdServiceTypeCriteriaRepository;
        this.dateHelper = dateHelper;
        this.crudService = crudService;
        this.serviceTypeService = serviceTypeService;
    }

    /**
     * Identifies the eligible service type based on the vehicle KM reading and vehicle age.
     *
     * @param purchaseDate
     * @param kmReading
     * @param tenant
     * @return
     */
    public int identifyServiceType(final LocalDate purchaseDate, final int kmReading, final int tenant) {

        // Adding plus to today, as the second argument is exclusive of the given date.
        long days = dateHelper.differenceInDays(purchaseDate, LocalDate.now());
        return mdServiceTypeCriteriaRepository.serviceTypeCriteria(days, kmReading, tenant);

    }

    /**
     * Interacts with repository that executes the service type identification logic.
     *
     * @param vehicleAge
     * @param kmReading
     * @param tenant
     * @return
     */
    public MdServiceTypeCriteria identifyServiceTypeCriteria(long vehicleAge, double kmReading, int tenant) {
        final var i = mdServiceTypeCriteriaRepository.serviceTypeCriteria(vehicleAge, kmReading, tenant);
        return crudService.findById(i, MdServiceTypeCriteria.class).orElse(null);
    }

    public List<MdServiceTypeCriteria> activeServiceTypeCriteriaByTenant(MdTenant tenant) {
        return mdServiceTypeCriteriaRepository.findByMdTenantOrderByDurationDaysFromAsc(tenant);
    }

    /**
     * Finds next criteria based on the current criteria range.
     *
     * @param matchingCriteria
     * @param tenant
     * @return
     */
    public MdServiceTypeCriteria nextCriteria(MdServiceTypeCriteria matchingCriteria, MdTenant tenant) {
        final int serviceTypeCriteria =
                mdServiceTypeCriteriaRepository.serviceTypeCriteria(matchingCriteria.getDurationDaysTo() + 1,
                        matchingCriteria.getMileageTo() + 1, tenant.getId());

        return mdServiceTypeCriteriaRepository.findById(serviceTypeCriteria).orElse(null);
    }

    @Transactional
    @Override
    public MdResponse save(MdRequest mdRequest, int tenantId) {
        MdServiceTypeCriteriaRequest mdServiceTypeCriteriaRequest = (MdServiceTypeCriteriaRequest) mdRequest;
        final var serviceType = serviceTypeService.getMdServiceType(mdServiceTypeCriteriaRequest.getServiceTypeId(),
                tenantId);
        if (Objects.isNull(serviceType))
            throw new VehicleServicesException("Service type doesn't exists");

        validateBeforeCreate(mdServiceTypeCriteriaRequest, tenantId);
        final var mdTenantOptional = crudService.findById(tenantId, MdTenant.class);
        final var mdServiceTypeCriteria = MdServiceTypeCriteriaBuilder.aMdServiceTypeCriteria()
                .withMdServiceType(serviceType)
                .withDescription(mdServiceTypeCriteriaRequest.getDescription())
                .withDurationDaysFrom(mdServiceTypeCriteriaRequest.getDurationDaysFrom())
                .withDurationDaysTo(mdServiceTypeCriteriaRequest.getDurationDaysTo())
                .withMileageFrom(mdServiceTypeCriteriaRequest.getMileageFrom())
                .withMileageTo(mdServiceTypeCriteriaRequest.getMileageTo())
                .withMdTenant(mdTenantOptional.get())
                .build();
        final var save = crudService.save(mdServiceTypeCriteria);
        return new MdServiceTypeCriteriaResponse(save);
    }


    private void validateBeforeCreate(MdServiceTypeCriteriaRequest mdServiceTypeCriteriaRequest, final int tenantId) {
        final var durationDaysFrom = mdServiceTypeCriteriaRequest.getDurationDaysFrom();
        final var durationDaysTo = mdServiceTypeCriteriaRequest.getDurationDaysTo();
        if (durationDaysFrom >= durationDaysTo)
            throw new VehicleServicesException("Invalid Vehicle Age range");
        final var mileageFrom = mdServiceTypeCriteriaRequest.getMileageFrom();
        final var mileageTo = mdServiceTypeCriteriaRequest.getMileageTo();
        if (mileageFrom >= mileageTo)
            throw new VehicleServicesException("Invalid Vehicle Mileage range");

        if (mdServiceTypeCriteriaRepository.
                existsByMdTenant_IdAndDurationDaysFromBetweenOrDurationDaysToBetweenOrMileageFromBetweenOrMileageToBetween(
                        tenantId, durationDaysFrom, durationDaysTo, durationDaysFrom, durationDaysTo,
                        mileageFrom, mileageTo, mileageFrom, mileageTo))
            throw new VehicleServicesException("Overlapping criteria detected");

    }

    @Transactional
    @Override
    public boolean delete(int id, int tenantId) {
        if (!mdServiceTypeCriteriaRepository.existsByIdAndMdTenant_Id(id, tenantId))
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Service criteria not found to delete.");
        mdServiceTypeCriteriaRepository.deleteByIdAndMdTenant_Id(id, tenantId);
        return true;
    }

    @Transactional
    @Override
    public List<? extends MdResponse> all(int tenantId) {
        final var serviceTypeCriteriaList = mdServiceTypeCriteriaRepository.findAllByMdTenant_Id(tenantId);

        if (CollectionUtils.isEmpty(serviceTypeCriteriaList))
            return Collections.emptyList();

        return serviceTypeCriteriaList.stream().map(MdServiceTypeCriteriaResponse::new).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MdResponse findById(int id, int tenantId) {
        final MdServiceTypeCriteria mdServiceTypeCriteria = getMdServiceTypeCriteria(id, tenantId);
        return new MdServiceTypeCriteriaResponse(mdServiceTypeCriteria);
    }

    @Transactional
    public MdServiceTypeCriteria getMdServiceTypeCriteria(int id, int tenantId) {
        return mdServiceTypeCriteriaRepository.findByIdAndMdTenant_Id(id, tenantId);
    }
}
