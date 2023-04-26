package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.md.MdServiceSchedulingConfigRequest;
import com.automate.vehicleservices.api.md.MdServiceSchedulingConfigResponse;
import com.automate.vehicleservices.entity.MdServiceCategory;
import com.automate.vehicleservices.entity.MdServiceSchedulingConfig;
import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.builder.MdServiceSchedulingConfigBuilder;
import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.MdRequest;
import com.automate.vehicleservices.framework.api.MdResponse;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.MdServiceSchedulingConfigRepository;
import com.automate.vehicleservices.service.dto.MdServiceSchedulingConfigDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class MdServiceSchedulingConfigService extends AbstractService implements MasterDataService {

    private final MdServiceSchedulingConfigRepository schedulingConfigRepository;

    public MdServiceSchedulingConfigService(
            MdServiceSchedulingConfigRepository schedulingConfigRepository) {
        this.schedulingConfigRepository = schedulingConfigRepository;
    }


    @Transactional
    public Map<String, MdServiceSchedulingConfigDTO> allActiveTenantsConfiguration() {
        final var serviceSchedulingConfigs = schedulingConfigRepository.findByMdTenant_IsActiveTrue();

        return serviceSchedulingConfigs.stream()
                .map(this::toDTO)
                .collect(Collectors.toMap(MdServiceSchedulingConfigDTO::getTenantIdentifier, Function.identity()));
    }

    private MdServiceSchedulingConfigDTO toDTO(MdServiceSchedulingConfig mdServiceSchedulingConfig) {
        return new MdServiceSchedulingConfigDTO(mdServiceSchedulingConfig);
    }

    @Override
    public MdResponse save(MdRequest mdRequest, int tenantId) {
        MdServiceSchedulingConfigRequest configRequest =
                (MdServiceSchedulingConfigRequest) mdRequest;

        if (schedulingConfigRepository.existsByMdTenant_Id(tenantId))
            throw new VehicleServicesException("Scheduling config already exists. only one config allowed.");
        final var MdServiceSchedulingConfig = MdServiceSchedulingConfigBuilder.aMdServiceSchedulingConfig()
                .withFirstFreeServiceDueDaysFromPurchaseDate(configRequest.getFirstFreeServiceDueDaysFromPurchaseDate())
                .withFirstFreeServiceType(getFirstFreeServiceTypeOrThrow(configRequest))
                .withFreeServiceCategory(getInvalidFreeServiceCategoryOrThrow(configRequest))
                .withPmcServiceCategory(getInvalidPmcServiceCategoryOrThrow(configRequest))
                .withPmcIntervalKm(configRequest.getPmcIntervalKm())
                .withPmcIntervalMonths(configRequest.getPmcIntervalMonths())
                .withPmcServiceType(getPmcServiceTypeOrThrow(configRequest))
                .withPmsServiceCategory(getInvalidPmsServiceCategoryOrThrow(configRequest))
                .withPmsIntervalMonths(configRequest.getPmsIntervalMonths())
                .withPmsIntervalKm(configRequest.getPmsIntervalKm())
                .withRemindersEnabled(configRequest.getEnableReminders())
                .withMdTenant(crudService.findById(tenantId, MdTenant.class).get())
                .build();

        final var save = crudService.save(MdServiceSchedulingConfig);
        return new MdServiceSchedulingConfigResponse(save);
    }

    private MdServiceCategory getInvalidPmsServiceCategoryOrThrow(MdServiceSchedulingConfigRequest configRequest) {
        return crudService.findById(configRequest.getPmsServiceCategoryId(), MdServiceCategory.class)
                .orElseThrow(() -> new VehicleServicesException("Invalid PMS Service Category"));
    }

    private MdServiceType getPmcServiceTypeOrThrow(MdServiceSchedulingConfigRequest configRequest) {
        return crudService.findById(configRequest.getPmcServiceTypeId(),
                MdServiceType.class).orElseThrow(() -> new VehicleServicesException("Invalid PMC Service " +
                "Type"));
    }

    private MdServiceCategory getInvalidPmcServiceCategoryOrThrow(MdServiceSchedulingConfigRequest configRequest) {
        return crudService.findById(configRequest.getPmcServiceCategoryId(),
                MdServiceCategory.class).orElseThrow(() -> new VehicleServicesException("Invalid PMC Service " +
                "Category"));
    }

    private MdServiceCategory getInvalidFreeServiceCategoryOrThrow(MdServiceSchedulingConfigRequest configRequest) {
        return crudService.findById(configRequest.getFreeServiceCategoryId(),
                MdServiceCategory.class).orElseThrow(() -> new VehicleServicesException("Invalid Free Service " +
                "Category"));
    }

    private MdServiceType getFirstFreeServiceTypeOrThrow(MdServiceSchedulingConfigRequest configRequest) {
        return crudService.findById(configRequest.getFirstFreeServiceTypeId(),
                MdServiceType.class).orElseThrow(() -> new VehicleServicesException("Invalid First Free Service Type"));
    }

    @Override
    public boolean delete(int id, int tenantId) {
        schedulingConfigRepository.deleteByIdAndMdTenant_Id(id, tenantId);
        return true;
    }

    @Override
    public List<? extends MdResponse> all(int tenantId) {
        final var mdServiceSlotList = schedulingConfigRepository.findAllByMdTenant_Id(tenantId);

        if (CollectionUtils.isEmpty(mdServiceSlotList))
            return Collections.emptyList();

        return mdServiceSlotList.stream().map(MdServiceSchedulingConfigResponse::new).collect(Collectors.toList());
    }

    @Override
    public MdResponse findById(int id, int tenantId) {
        final MdServiceSchedulingConfig mdServiceSchedulingConfig = getMdServiceSchedulingConfig(id, tenantId);
        return new MdServiceSchedulingConfigResponse(mdServiceSchedulingConfig);
    }

    @Transactional
    public MdServiceSchedulingConfig getMdServiceSchedulingConfig(int id, int tenantId) {
        return schedulingConfigRepository.findByIdAndMdTenant_Id(id, tenantId);
    }
}
