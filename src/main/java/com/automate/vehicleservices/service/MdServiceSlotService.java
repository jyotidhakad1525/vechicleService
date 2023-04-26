package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.md.MdServiceSlotRequest;
import com.automate.vehicleservices.api.md.MdServiceSlotResponse;
import com.automate.vehicleservices.entity.MdServiceSlot;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.builder.MdServiceSlotBuilder;
import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.MdRequest;
import com.automate.vehicleservices.framework.api.MdResponse;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.MdServiceSlotRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Chandrashekar V
 */
@Component
public class MdServiceSlotService extends AbstractService implements MasterDataService {

    private final MdServiceSlotRepository mdServiceSlotRepository;

    @Autowired
    public MdServiceSlotService(MdServiceSlotRepository mdServiceSlotRepository) {
        this.mdServiceSlotRepository = mdServiceSlotRepository;
    }

    public Optional<MdServiceSlot> findById(final int id) {
        return mdServiceSlotRepository.findById(id);
    }


    @Transactional
    @Override
    public MdResponse save(MdRequest mdRequest, int tenantId) {
        MdServiceSlotRequest mdServiceSlotRequest = (MdServiceSlotRequest) mdRequest;
        validateBeforeCreate(mdServiceSlotRequest, tenantId);
        final var mdServiceSlot = MdServiceSlotBuilder.aMdServiceSlot()
                .withAvailability(mdServiceSlotRequest.getAvailability())
                .withDay(mdServiceSlotRequest.getDay())
                .withSlotTimeFrom(mdServiceSlotRequest.getSlotTimeFrom())
                .withSlotTimeTo(mdServiceSlotRequest.getSlotTimeTo())
                .withActive(true)
                .withMdTenant(crudService.findById(tenantId, MdTenant.class).get())
                .build();

        final var save = crudService.save(mdServiceSlot);
        return new MdServiceSlotResponse(save);
    }

    @Transactional
    public MdResponse update(MdRequest mdRequest, int tenantId, int id) {
        MdServiceSlotRequest mdServiceSlotRequest = (MdServiceSlotRequest) mdRequest;
        final var mdServiceSlot = getMdServiceSlot(id, tenantId);
        if (Objects.isNull(mdServiceSlot))
            throw new VehicleServicesException("Slot not found to update");

        validateBeforeUpdate(mdServiceSlotRequest, tenantId, id);
        mdServiceSlot.setAvailability(mdServiceSlotRequest.getAvailability());
        mdServiceSlot.setDay(mdServiceSlotRequest.getDay());
        mdServiceSlot.setSlotTimeFrom(mdServiceSlotRequest.getSlotTimeFrom());
        mdServiceSlot.setSlotTimeTo(mdServiceSlotRequest.getSlotTimeTo());
        final var save = crudService.save(mdServiceSlot);
        return new MdServiceSlotResponse(save);
    }

    private void validateBeforeCreate(MdServiceSlotRequest mdServiceSlotRequest, int tenantId) {
        final var exists =
                mdServiceSlotRepository.overlappingSlots
                        (mdServiceSlotRequest.getDay(), tenantId, mdServiceSlotRequest.getSlotTimeFrom(),
                                mdServiceSlotRequest.getSlotTimeTo());
        if (CollectionUtils.isNotEmpty(exists))
            throw new VehicleServicesException("Selected slot is overlapping with existing slots.");
    }

    private void validateBeforeUpdate(MdServiceSlotRequest mdServiceSlotRequest, int tenantId, int slotIdToUpdate) {
        final var exists =
                mdServiceSlotRepository.overlappingSlotsOtherThanGivenId
                        (mdServiceSlotRequest.getDay(), tenantId,
                                mdServiceSlotRequest.getSlotTimeFrom(),
                                mdServiceSlotRequest.getSlotTimeTo(), slotIdToUpdate);
        if (CollectionUtils.isNotEmpty(exists))
            throw new VehicleServicesException("Selected slot is overlapping with existing slots.");
    }

    @Transactional
    @Override
    public boolean delete(int id, int tenantId) {
        mdServiceSlotRepository.deleteByIdAndMdTenant_Id(id, tenantId);
        return true;
    }

    @Transactional
    @Override
    public List<? extends MdResponse> all(int tenantId) {
        final var mdServiceSlotList = mdServiceSlotRepository.findAllByMdTenant_IdAndActiveTrue(tenantId);

        if (CollectionUtils.isEmpty(mdServiceSlotList))
            return Collections.emptyList();

        return mdServiceSlotList.stream().map(MdServiceSlotResponse::new).collect(Collectors.toList());
    }


    @Transactional
    public Map<DayOfWeek, List<MdServiceSlotResponse>> slotsByDay(int tenantId) {
        final var mdServiceSlotList = mdServiceSlotRepository.findAllByMdTenant_IdAndActiveTrue(tenantId);

        Map<DayOfWeek, List<MdServiceSlotResponse>> slotsMap = new LinkedHashMap<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values())
            slotsMap.put(dayOfWeek, new ArrayList<>());

        if (CollectionUtils.isEmpty(mdServiceSlotList))
            return slotsMap;

        mdServiceSlotList.forEach(serviceSlot -> {
            if (slotsMap.containsKey(serviceSlot.getDay()))
                slotsMap.get(serviceSlot.getDay()).add(new MdServiceSlotResponse(serviceSlot));
        });
        slotsMap.forEach((key, value) -> value
                .sort(Comparator.comparing(MdServiceSlotResponse::getSlotTimeFrom)));
        return slotsMap;
    }

    @Transactional
    @Override
    public MdResponse findById(int id, int tenantId) {
        final MdServiceSlot mdServiceSlot = getMdServiceSlot(id, tenantId);
        return new MdServiceSlotResponse(mdServiceSlot);
    }

    @Transactional
    public MdServiceSlot getMdServiceSlot(int id, int tenantId) {
        return mdServiceSlotRepository.findByIdAndMdTenant_IdAndActiveTrue(id, tenantId);
    }
}
