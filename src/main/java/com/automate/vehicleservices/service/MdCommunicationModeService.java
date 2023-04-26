package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.md.MdCommunicationModeResponse;
import com.automate.vehicleservices.entity.CommunicationMode;
import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.MdRequest;
import com.automate.vehicleservices.framework.api.MdResponse;
import com.automate.vehicleservices.repository.MdCommunicationModeRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MdCommunicationModeService extends AbstractService implements MasterDataService {

    final
    MdCommunicationModeRepository mdCommunicationModeRepository;

    public MdCommunicationModeService(MdCommunicationModeRepository mdCommunicationModeRepository) {
        this.mdCommunicationModeRepository = mdCommunicationModeRepository;
    }

    @Override
    public MdResponse save(MdRequest mdRequest, int tenantId) {
        // TODO : Support in future if detached from java enums
        return null;
    }

    @Override
    public boolean delete(int id, int tenantId) {
        crudService.deleteById(id, CommunicationMode.class);
        return true;
    }

    @Override
    public List<? extends MdResponse> all(int tenantId) {
        final var communicationModes = mdCommunicationModeRepository.findAllByMdTenant_Id(tenantId);
        if (CollectionUtils.isEmpty(communicationModes))
            return Collections.emptyList();

        return communicationModes.stream().map(MdCommunicationModeResponse::new).collect(Collectors.toList());
    }

    @Override
    public MdResponse findById(int id, int tenantId) {
        final var communicationMode = getMdCommunicationMode(id, tenantId);
        return new MdCommunicationModeResponse(communicationMode);
    }

    @Transactional
    public CommunicationMode getMdCommunicationMode(int id, int tenantId) {
        return mdCommunicationModeRepository.findByIdAndMdTenant_Id(id, tenantId);
    }
}
