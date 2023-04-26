package com.automate.vehicleservices.service.md;

import com.automate.vehicleservices.api.md.MdLeadSourceHierarchyResponse;
import com.automate.vehicleservices.api.md.MdLeadSourceResponse;
import com.automate.vehicleservices.entity.MdLeadSource;
import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.MdRequest;
import com.automate.vehicleservices.framework.api.MdResponse;
import com.automate.vehicleservices.repository.MdLeadSourceRepository;
import com.automate.vehicleservices.repository.dtoprojection.MdLeadSourceHierarchyDTO;
import com.automate.vehicleservices.service.AbstractService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MdLeadSourceService extends AbstractService implements MasterDataService {


    private final MdLeadSourceRepository mdLeadSourceRepository;

    public MdLeadSourceService(MdLeadSourceRepository mdLeadSourceRepository) {
        this.mdLeadSourceRepository = mdLeadSourceRepository;
    }

    @Override
    public MdResponse save(MdRequest mdRequest, int tenantId) {
        // TODO :
        return null;
    }

    @Override
    public boolean delete(int id, int tenantId) {
        crudService.deleteById(id, MdLeadSource.class);
        return true;
    }

    @Override
    public List<? extends MdResponse> all(int tenantId) {
        final var leadSources = mdLeadSourceRepository.findAllByMdTenant_Id(tenantId);
        if (CollectionUtils.isEmpty(leadSources))
            return Collections.emptyList();

        return leadSources.stream().map(MdLeadSourceResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public Collection<MdLeadSourceHierarchyResponse> allByHierarchy(int tenantId) {
        final var leadSources = mdLeadSourceRepository.fetchHierarchy(tenantId);
        if (CollectionUtils.isEmpty(leadSources))
            return Collections.emptyList();

        Queue<MdLeadSourceHierarchyDTO> dtoQueue = new ArrayDeque<>();
        dtoQueue.addAll(leadSources);
        Map<Integer, MdLeadSourceHierarchyResponse> resultMap = new HashMap<>();

        while (dtoQueue.size() > 0) {
            final var hierarchyDTO = dtoQueue.poll();
            final var parentId = hierarchyDTO.getParentId();
            final var id = hierarchyDTO.getId();
            final var type = hierarchyDTO.getType();
            final var isActive = hierarchyDTO.getIsActive();
            if (Objects.isNull(parentId)) {
                resultMap.put(id, new MdLeadSourceHierarchyResponse(id, type, isActive));
            } else {
                if (resultMap.containsKey(parentId)) {
                    resultMap.get(parentId)
                            .addSubType(new MdLeadSourceHierarchyResponse(id, type, isActive));
                    continue;
                }
                boolean subtypeExistsAsParent = false;
                for (MdLeadSourceHierarchyResponse mdLeadSourceHierarchyResponse : resultMap.values()) {
                    MdLeadSourceHierarchyResponse subtypeAsParent =
                            subtypeExistsAsParent(mdLeadSourceHierarchyResponse, parentId);
                    if (Objects.nonNull(subtypeAsParent)) {
                        subtypeAsParent.addSubType(new MdLeadSourceHierarchyResponse(id, type, isActive));
                        subtypeExistsAsParent = true;
                        break;
                    }
                }
                if (!subtypeExistsAsParent)
                    dtoQueue.add(hierarchyDTO);

            }
        }

        return resultMap.values();

    }

    private MdLeadSourceHierarchyResponse subtypeExistsAsParent(MdLeadSourceHierarchyResponse mdLeadSourceHierarchyResponse, Integer parentId) {
        if (mdLeadSourceHierarchyResponse == null)
            return null;
        if (mdLeadSourceHierarchyResponse.getId() == parentId)
            return mdLeadSourceHierarchyResponse;
        if (CollectionUtils.isEmpty(mdLeadSourceHierarchyResponse.getSubtypeMap().values()))
            return null;
        else
            for (MdLeadSourceHierarchyResponse subtype : mdLeadSourceHierarchyResponse.getSubtypeMap().values()) {
                final var mdLeadSourceHierarchyResponse1 = subtypeExistsAsParent(subtype, parentId);
                if (null != mdLeadSourceHierarchyResponse1)
                    return mdLeadSourceHierarchyResponse1;
            }

        return null;
    }

    @Override
    public MdResponse findById(int id, int tenantId) {
        final var leadSource = getMdCommunicationMode(id, tenantId);
        return new MdLeadSourceResponse(leadSource);
    }

    @Transactional
    public MdLeadSource getMdCommunicationMode(int id, int tenantId) {
        return mdLeadSourceRepository.findByIdAndMdTenant_Id(id, tenantId);
    }


}
