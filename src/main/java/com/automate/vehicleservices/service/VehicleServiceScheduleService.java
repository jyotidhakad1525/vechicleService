package com.automate.vehicleservices.service;

import com.automate.vehicleservices.entity.VehicleServiceSchedule;
import com.automate.vehicleservices.entity.enums.ScheduleStatus;
import com.automate.vehicleservices.repository.VehicleServiceScheduleRepository;
import com.automate.vehicleservices.repository.dtoprojection.ServiceScheduleDTO;
import com.automate.vehicleservices.service.dto.PageableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chandrashekar V
 */
@Component
public class VehicleServiceScheduleService {

    private final VehicleServiceScheduleRepository vehicleServiceScheduleRepository;

    @Autowired
    public VehicleServiceScheduleService(VehicleServiceScheduleRepository vehicleServiceScheduleRepository) {
        this.vehicleServiceScheduleRepository = vehicleServiceScheduleRepository;
    }

    public List<ServiceScheduleDTO> findSchedulesByTenantAndStatus(final String tenant,
                                                                   final ScheduleStatus... scheduleStatuses) {
        final var serviceSchedules =
                vehicleServiceScheduleRepository.findByMdTenant_TenantIdentifierAndStatusIn(tenant,
                        scheduleStatuses);
        return toDTOList(serviceSchedules);

    }

    public List<ServiceScheduleDTO> findSchedulesByVehicleAndStatuses(final String vehicleRegNumber,
                                                                      ScheduleStatus[] scheduleStatuses) {
        final var serviceSchedules =
                vehicleServiceScheduleRepository.findByServiceVehicle_RegNumberAndStatusIn(vehicleRegNumber,
                        scheduleStatuses);
        return toDTOList(serviceSchedules);

    }


    /**
     * findByServiceVehicle_RegNumberAndStatusIn Checks whether there is any ongoing schedule exists.
     *
     * @param regNumber
     * @return
     */
    public boolean isActiveScheduleExists(final String regNumber) {
        long count = vehicleServiceScheduleRepository.countAllByServiceVehicle_RegNumberAndStatusIn(regNumber,
                ScheduleStatus.NEW, ScheduleStatus.IN_PROGRESS);
        return count > 0;
    }

    public List<ServiceScheduleDTO> findByVehicleWithActiveSchedule(String regNumber) {
        final var serviceSchedules = vehicleServiceScheduleRepository
                .findByServiceVehicle_RegNumberAndStatusIn(regNumber, ScheduleStatus.NEW, ScheduleStatus.IN_PROGRESS);
        return toDTOList(serviceSchedules);
    }

    public List<ServiceScheduleDTO> toDTOList(List<VehicleServiceSchedule> vehicleServiceSchedules) {
        return vehicleServiceSchedules.stream().map(ServiceScheduleDTO::new).collect(Collectors.toList());
    }

    public void cancelServiceScheduleForVehicle(final int vehicleId) {
        vehicleServiceScheduleRepository.changeVehicleScheduleStatus(vehicleId, ScheduleStatus.CANCELLED.name());
    }


    public void updateVehicleScheduleStatusNewToInProgress(final int vehicleId) {
        vehicleServiceScheduleRepository.changeVehicleScheduleStatus(vehicleId, ScheduleStatus.IN_PROGRESS.name(),
                ScheduleStatus.NEW.name());
    }

    @Transactional
    public PageableResponse<ServiceScheduleDTO> fetchVehicleSchedulesPaginated(final int pageNumber,
                                                                               final int numberOfElementsPerPage) {
        final var byScheduleStatus = vehicleServiceScheduleRepository.findByStatus(ScheduleStatus.NEW,
                PageRequest.of(pageNumber, numberOfElementsPerPage));

        final List<ServiceScheduleDTO> collect = toDTOList(byScheduleStatus.toList());

        if (byScheduleStatus.getTotalPages() == 0)
            return new PageableResponse<>();

        final var currentPageNumber = byScheduleStatus.getNumber() + 1;
        final boolean nextPageExists = currentPageNumber < byScheduleStatus.getTotalPages();
        return new PageableResponse(collect,
                nextPageExists, currentPageNumber, byScheduleStatus.getTotalPages(),
                byScheduleStatus.getTotalElements());
    }


}
