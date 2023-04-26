package com.automate.vehicleservices.service;

import com.automate.vehicleservices.entity.MdServiceSlot;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.ServiceSlotAvailabilityRepository;
import com.automate.vehicleservices.repository.dtoprojection.SlotAvailability;
import com.automate.vehicleservices.service.dto.SlotAvailabilityDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Chandrashekar V
 */
@Component
@Slf4j
public class ServiceSlotAvailabilityService {

    public static final String IST = "IST";
    private final MdServiceSlotService mdServiceSlotService;
    private final ServiceSlotAvailabilityRepository serviceSlotAvailabilityRepository;

    @Autowired
    public ServiceSlotAvailabilityService(ServiceSlotAvailabilityRepository serviceSlotAvailabilityRepository,
                                          MdServiceSlotService mdServiceSlotService) {
        this.serviceSlotAvailabilityRepository = serviceSlotAvailabilityRepository;
        this.mdServiceSlotService = mdServiceSlotService;
    }

    /**
     * Fetches available time slots for the given tenant and Date.
     *
     * @param tenant
     * @param serviceDate
     * @return
     */
    public List<SlotAvailabilityDTO> timeSlotAvailabilityByTenantAndDate(final int tenant,
                                                                         final LocalDate serviceDate) {
        Iterable<SlotAvailability> byTenantIdAndServiceDate = serviceSlotAvailabilityRepository
                .fetchSlotsAvailability(tenant, serviceDate, serviceDate.getDayOfWeek().name());

        return StreamSupport
                .stream(byTenantIdAndServiceDate.spliterator(), false)
                .filter(Objects::nonNull)
                .filter(slot -> isSlotNotExpired(slot, serviceDate))
                .map(slotAvailability -> new SlotAvailabilityDTO(slotAvailability,
                        serviceDate)).collect(Collectors.toList());

    }

    private boolean isSlotNotExpired(SlotAvailability slot, LocalDate serviceDate) {
        try {

            return isFutureDate(serviceDate) || (isToday(serviceDate) && isTimeInFuture(slot));
        } catch (Exception ex) {
            log.error("Time slot parsing from string to Localtime failed.");
        }
        return false;
    }

    private boolean isFutureDate(LocalDate serviceDate) {
        return serviceDate.isAfter(LocalDate.now());
    }

    private boolean isTimeInFuture(SlotAvailability slot) {
        if (Objects.isNull(slot.getToTime())) return false;
        final LocalTime localTime = LocalTime.parse(slot.getToTime(), DateTimeFormatter.ISO_LOCAL_TIME);
        return localTime.isAfter(LocalTime.now());
    }

    private boolean isToday(LocalDate serviceDate) {
        return serviceDate.isEqual(LocalDate.now());
    }

    /**
     * Validate and fetch requested timeslot.
     *
     * @param timeSlotId
     * @param slotDate
     * @param tenantId
     * @param isEdit
     * @return
     */
    public MdServiceSlot validateAndReturnSlot(final int timeSlotId, final LocalDate slotDate,
                                               final int tenantId, boolean isEdit) {
        Optional<MdServiceSlot> slot = mdServiceSlotService.findById(timeSlotId);
        if (!slot.isPresent())
            throw new VehicleServicesException("Slot has become unavailable.");

        throwExceptionIfSlotTimeHasPassed(slotDate, slot.get());
        List<SlotAvailabilityDTO> timeSlotAvailabilityByTenantAndDate =
                timeSlotAvailabilityByTenantAndDate(tenantId, slotDate);
        if (!isEdit)
            // Check slot availability
            throwExceptionIfSlotNotAvailable(timeSlotId, timeSlotAvailabilityByTenantAndDate);

        return slot.get();
    }


    /**
     * Checks whether time slot has been expired. Or time slot is in the past.
     *
     * @param slotDate
     * @param slot
     */
    private void throwExceptionIfSlotTimeHasPassed(final LocalDate slotDate, MdServiceSlot slot) {
        if (!slotDate.isAfter(LocalDate.now())) {
            if (LocalTime.now().isAfter(slot.getSlotTimeFrom()))
                throw new VehicleServicesException("Slot time has been expired.");
        }
    }

    /**
     * Checks if requested slot exists, if not throws run time exception.then n
     *
     * @param slotId
     * @param availabilityList
     */
    private void throwExceptionIfSlotNotAvailable(final int slotId, List<SlotAvailabilityDTO> availabilityList) {
        boolean isSlotAvailable = isSlotAvailable(availabilityList, slotId);
        if (!isSlotAvailable)
            throw new VehicleServicesException(String.format("Requested slot not available "));
    }

    /**
     * Checks whether the requested slot is available.
     *
     * @param availabilityBYServiceTypes
     * @param timeSlotId
     * @return
     */
    private boolean isSlotAvailable(List<SlotAvailabilityDTO> availabilityBYServiceTypes, final int timeSlotId) {
        Optional<SlotAvailabilityDTO> first = availabilityBYServiceTypes.stream()
                .filter(timeSlot -> timeSlot.getSlotId() == timeSlotId && timeSlot.getAvailable() > 0)
                .findFirst();
        return first.isPresent();
    }


}
