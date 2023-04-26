package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.md.MdServiceReminderPrefRequest;
import com.automate.vehicleservices.api.md.MdServiceReminderPrefResponse;
import com.automate.vehicleservices.entity.MdServiceReminderPref;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.builder.MdServiceReminderPrefBuilder;
import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.MdRequest;
import com.automate.vehicleservices.framework.api.MdResponse;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.MdServiceReminderPrefRepository;
import com.automate.vehicleservices.repository.dtoprojection.ServiceReminderPrefDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MdServiceReminderPrefService extends AbstractService implements MasterDataService {

    private final MdServiceReminderPrefRepository mdServiceReminderPrefRepository;
    private final MdServiceTypeService serviceTypeService;

    private final MdCommunicationModeService mdCommunicationModeService;

    public MdServiceReminderPrefService(MdServiceReminderPrefRepository mdServiceReminderPrefRepository,
                                        MdServiceTypeService serviceTypeService,
                                        MdCommunicationModeService mdCommunicationModeService) {
        this.mdServiceReminderPrefRepository = mdServiceReminderPrefRepository;
        this.serviceTypeService = serviceTypeService;
        this.mdCommunicationModeService = mdCommunicationModeService;
    }

    /**
     * Fetches active service reminder preferences for the given service type.
     *
     * @param serviceType
     * @return
     */
    public List<ServiceReminderPrefDTO> fetchActiveReminderPreferencesByServiceType(int serviceType) {

        final var reminderPrefs =
                mdServiceReminderPrefRepository.findByIsActiveTrueAndMdServiceType_Id(serviceType);
        return reminderPrefs.stream().map(ServiceReminderPrefDTO::new).collect(Collectors.toList());
    }

    @Override
    public MdResponse save(MdRequest mdRequest, int tenantId) {
        MdServiceReminderPrefRequest mdServiceReminderPrefRequest = (MdServiceReminderPrefRequest) mdRequest;
        final var mdServiceType = serviceTypeService.getMdServiceType(mdServiceReminderPrefRequest.getServiceType(),
                tenantId);
        if (Objects.isNull(mdServiceType))
            throw new VehicleServicesException("Service type not matching");

        final var mdCommunicationMode =
                mdCommunicationModeService.getMdCommunicationMode(mdServiceReminderPrefRequest.getCommunicationMode()
                        , tenantId);
        if (Objects.isNull(mdCommunicationMode))
            throw new VehicleServicesException("Invalid communication mode");

        final var mdServiceSlot = MdServiceReminderPrefBuilder.aMdServiceReminderPref()
                .withMdServiceType(mdServiceType)
                .withCommunicationMode(mdCommunicationMode)
                .withDurationValue(mdServiceReminderPrefRequest.getDurationValue())
                .withTimeFrame(mdServiceReminderPrefRequest.getTimeFrame())
                .withMdTenant(crudService.findById(tenantId, MdTenant.class).get())
                .withIsActive(mdServiceReminderPrefRequest.getIsActive())
                .build();

        final var save = mdServiceReminderPrefRepository.save(mdServiceSlot);

        return new MdServiceReminderPrefResponse(save);
    }

    @Transactional
    @Override
    public boolean delete(int id, int tenantId) {
        mdServiceReminderPrefRepository.deleteByIdAndMdTenant_Id(id, tenantId);
        return true;
    }

    @Transactional
    @Override
    public List<? extends MdResponse> all(int tenantId) {
        final var mdServiceReminderPrefs = mdServiceReminderPrefRepository.findAllByMdTenant_Id(tenantId);

        if (CollectionUtils.isEmpty(mdServiceReminderPrefs))
            return Collections.emptyList();

        return mdServiceReminderPrefs.stream().map(MdServiceReminderPrefResponse::new).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MdResponse findById(int id, int tenantId) {
        final MdServiceReminderPref mdServiceReminderPref = getMdServiceReminderPref(id, tenantId);
        return new MdServiceReminderPrefResponse(mdServiceReminderPref);
    }

    @Transactional
    public MdServiceReminderPref getMdServiceReminderPref(int id, int tenantId) {
        return mdServiceReminderPrefRepository.findByIdAndMdTenant_Id(id, tenantId);
    }
}
