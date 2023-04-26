package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.v1.VehicleDetailsV1;
import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.builder.VehicleKmTrackerBuilder;
import com.automate.vehicleservices.entity.builder.MdServiceTypeBuilder;
import com.automate.vehicleservices.entity.enums.ScheduleStatus;
import com.automate.vehicleservices.entity.VehicleServiceSchedule;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.ServiceVehicleRepository;
import com.automate.vehicleservices.repository.VehicleServiceScheduleRepository;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import com.automate.vehicleservices.repository.dtoprojection.VehicleServiceScheduleDto;
import com.automate.vehicleservices.service.dto.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceVehicleService {

    private final ServiceVehicleRepository serviceVehicleRepository;

    private final VehicleServiceScheduleRepository vehicleServiceScheduleRepository;

    @Autowired
    public ServiceVehicleService(ServiceVehicleRepository serviceVehicleRepository, VehicleServiceScheduleRepository vehicleServiceScheduleRepository) {
        this.serviceVehicleRepository = serviceVehicleRepository;
        this.vehicleServiceScheduleRepository = vehicleServiceScheduleRepository;
    }

    @Transactional
    public ServiceVehicle findByVehicleRegNumber(final String registrationNumber) {
        return serviceVehicleRepository.findByRegNumber(registrationNumber);
    }

    @Transactional
    public ServiceVehicle findByVehicleRegNumberOrThrowException(final String registrationNumber) {
        final ServiceVehicle serviceVehicle = serviceVehicleRepository.findByRegNumber(registrationNumber);
        if (Objects.isNull(serviceVehicle))
            throw new VehicleServicesException(String.format("Vehicle not found with reg number %s",
                    registrationNumber));
        return serviceVehicle;
    }

    @Transactional
    public ServiceVehicleDTO findByVehicleRegNumberAndReturnDTO(final String registrationNumber) {
        final ServiceVehicle serviceVehicle = findByVehicleRegNumber(registrationNumber);
        if (Objects.isNull(serviceVehicle))
            throw new VehicleServicesException(String.format("Vehicle not found with reg number %s",
                    registrationNumber));
        return new ServiceVehicleDTO(serviceVehicle);
    }

    @Transactional
    public List<ServiceVehicleDTO> fetchUnscheduledVehicles(final MdTenant tenant) {
        final var serviceVehicles =
                serviceVehicleRepository.fetchUnscheduledVehicles(Arrays.asList(ScheduleStatus.NEW.name(),
                        ScheduleStatus.IN_PROGRESS.name()));
        return populateServiceVehicleDTOS(serviceVehicles);
    }

    @Transactional
    public List<ServiceVehicleDTO> fetchUnscheduledVehicles() {
        final var serviceVehicles =
                serviceVehicleRepository.fetchUnscheduledVehicles(Arrays.asList(ScheduleStatus.NEW.name(),
                        ScheduleStatus.IN_PROGRESS.name()));
        return populateServiceVehicleDTOS(serviceVehicles);
    }

    private List<ServiceVehicleDTO> populateServiceVehicleDTOS(List<ServiceVehicle> serviceVehicles) {
        if (Objects.nonNull(serviceVehicles))
            return serviceVehicles.stream()
                    .map(ServiceVehicleDTO::new)
                    .collect(Collectors.toList());

        return null;
    }

    @Transactional
    public PageableResponse<ServiceVehicleDTO> fetchUnscheduledVehiclesPaginated(final int pageNumber,
                                                                                 final int numberOfElementsPerPage) {
        final Page<ServiceVehicle> iServiceVehicleDTOS =
                serviceVehicleRepository.fetchUnscheduledVehiclesPaginated(Arrays.asList(ScheduleStatus.NEW.name(),
                        ScheduleStatus.IN_PROGRESS.name()), PageRequest.of(pageNumber, numberOfElementsPerPage));

        final List<ServiceVehicleDTO> collect =
                iServiceVehicleDTOS.stream()
                        .map(ServiceVehicleDTO::new)
                        .collect(Collectors.toList());

        if (iServiceVehicleDTOS.getTotalPages() == 0)
            return new PageableResponse<>();

        final var currentPageNumber = iServiceVehicleDTOS.getNumber() + 1;
        final boolean nextPageExists = currentPageNumber < iServiceVehicleDTOS.getTotalPages();
        return new PageableResponse(collect,
                nextPageExists, currentPageNumber, iServiceVehicleDTOS.getTotalPages(),
                iServiceVehicleDTOS.getTotalElements());

    }

    /**
     * Update given vehicle with latest details.
     *
     * @param vehicleDetails
     * @param vehicleRegNumber
     * @return
     */
    @Transactional
    public ServiceVehicleDTO update(VehicleDetailsV1 vehicleDetails, String vehicleRegNumber) {
        final var vehicleEntity = getServiceVehicleOrThrow(vehicleRegNumber);

        vehicleEntity.setChassisNumber(vehicleDetails.getChassisNumber());
        vehicleEntity.setVin(vehicleDetails.getVin());
        vehicleEntity.setModel(vehicleDetails.getVehicleModel());
        vehicleEntity.setPurchaseDate(vehicleDetails.getPurchaseDate());
        vehicleEntity.setFuelType(vehicleDetails.getFuelType());
        vehicleEntity.setColor(vehicleDetails.getColor());
        vehicleEntity.setVariant(vehicleDetails.getVariant());
        vehicleEntity.setSellingDealer(vehicleDetails.getSellingDealer());
        vehicleEntity.setSellingLocation(vehicleDetails.getSellingLocation());
        if (vehicleDetails.getCurrentKmReading() > 0) {
            final var vehicleKmTracker =
                    VehicleKmTrackerBuilder.aVehicleKmTracker().withKmReading(vehicleDetails.getCurrentKmReading())
                            .withRecordedDate(LocalDateTime.now()).build();
            vehicleEntity.addVehicleKmTracker(vehicleKmTracker);
        }
        final var save = serviceVehicleRepository.save(vehicleEntity);

        return new ServiceVehicleDTO(save);
    }

    @Transactional
    public ConsolidateCustomerVehicleResponse fetchCompleteVehicleInfo(String tenant, String vehicleRegNumber) {

        final ServiceVehicle vehicle = getServiceVehicleOrThrow(vehicleRegNumber);
        final var customer = vehicle.getCustomer();

        final var vehicleWarranties = vehicle.getVehicleWarranties();

        List<VehicleWarrantyDTO> vehicleWarrantyDTOs = null;
        if (CollectionUtils.isNotEmpty(vehicleWarranties))
            // Only one supposed to be active Hence taking the first one in case of multiple results..
            vehicleWarrantyDTOs = vehicleWarranties.stream().map(VehicleWarrantyDTO::new).collect(Collectors.toList());

        VehicleInsuranceMinimalDTO vehicleInsuranceDTO = getVehicleInsuranceMinimalDTO(vehicle);

        var serviceVehicleDTO = new ServiceVehicleDTO(vehicle);

        VehicleServiceScheduleDto vehicleServiceScheduleDto = VehicleServiceScheduleDto.builder().build();
        Optional<VehicleServiceSchedule> vehicleServiceSchedule = vehicleServiceScheduleRepository.findFirstByServiceVehicle_idOrderByLastServiceDateDesc(vehicle.getId());
        if(vehicleServiceSchedule.isPresent()){
            buildVehicleServiceScheduleDto(vehicleServiceScheduleDto, vehicleServiceSchedule.get());
        }

        return ConsolidateCustomerVehicleResponse.builder()
                .customer(new CustomerDTO(customer))
                .vehicle(serviceVehicleDTO)
                .vehicleInsurance(vehicleInsuranceDTO)
                .vehicleWarranty(vehicleWarrantyDTOs)
                .vehicleServiceScheduleDto(vehicleServiceScheduleDto)
                .build();
    }

    private ServiceVehicle getServiceVehicleOrThrow(String vehicleRegNumber) {
        final var vehicle = findByVehicleRegNumber(vehicleRegNumber);
        if (Objects.isNull(vehicle))
            throw new VehicleServicesException(String.format("Vehicle not found with registration number %s",
                    vehicleRegNumber));
        return vehicle;
    }

    @Transactional
    public VehicleInsuranceMinimalDTO fetchVehicleInsurance(String vehicleRegNumber) {
        final ServiceVehicle vehicle = getServiceVehicleOrThrow(vehicleRegNumber);
        return getVehicleInsuranceMinimalDTO(vehicle);
    }

    private VehicleInsuranceMinimalDTO getVehicleInsuranceMinimalDTO(ServiceVehicle vehicle) {
        final var vehicleInsurances = vehicle.getVehicleInsurances();
        VehicleInsuranceMinimalDTO vehicleInsuranceDTO = null;
        if (CollectionUtils.isNotEmpty(vehicleInsurances))
            // Only one supposed to be active Hence taking the first one in case of multiple results..
            vehicleInsuranceDTO = new VehicleInsuranceMinimalDTO(vehicleInsurances.get(0));
        return vehicleInsuranceDTO;
    }

    @Transactional
    public void deleteVehicle(String vehicleRegNumber) {
        final var vehicleEntity = getServiceVehicleOrThrow(vehicleRegNumber);
        serviceVehicleRepository.delete(vehicleEntity);
    }

    private void buildVehicleServiceScheduleDto(VehicleServiceScheduleDto vehicleServiceScheduleDto, VehicleServiceSchedule vehicleServiceSchedule){
        MdServiceTypeBuilder mdServiceTypeBuilder = MdServiceTypeBuilder.aMdServiceType();
        mdServiceTypeBuilder.withServiceName(vehicleServiceSchedule.getMdServiceType().getServiceName());
        vehicleServiceScheduleDto.setAverageKM(vehicleServiceSchedule.getAverageKM());
        vehicleServiceScheduleDto.setDifferenceInDaysBetweenLastTwoServices(vehicleServiceSchedule.getDifferenceInDaysBetweenLastTwoServices());
        vehicleServiceScheduleDto.setId(vehicleServiceSchedule.getId());
        vehicleServiceScheduleDto.setKmBetweenLastTwoServices(vehicleServiceSchedule.getKmBetweenLastTwoServices());
        vehicleServiceScheduleDto.setAverageKM(vehicleServiceSchedule.getAverageKM());
        vehicleServiceScheduleDto.setLastModifiedReason(vehicleServiceSchedule.getLastModifiedReason());
        vehicleServiceScheduleDto.setLastServiceDate(vehicleServiceSchedule.getLastServiceDate());
        vehicleServiceScheduleDto.setMdServiceType(mdServiceTypeBuilder);
        vehicleServiceScheduleDto.setServiceCategory(vehicleServiceSchedule.getServiceCategory());
        vehicleServiceScheduleDto.setServiceDueDatePerSchedule(vehicleServiceSchedule.getServiceDueDatePerSchedule());
        vehicleServiceScheduleDto.setServiceDueDatePerSchedule(vehicleServiceSchedule.getServiceDueDatePerSchedule());
        vehicleServiceScheduleDto.setServiceDueDatePreferred(vehicleServiceSchedule.getServiceDueDatePreferred());
        vehicleServiceScheduleDto.setServiceDueDateRecommended(vehicleServiceSchedule.getServiceDueDateRecommended());
        vehicleServiceScheduleDto.setStatus(vehicleServiceSchedule.getStatus());
    }

    public void updateOnGoingDueDateAndServiceTypeBasedOnVehicleId(int vehicleId, MdServiceType mdServiceType, LocalDate recommendedDueDate) {
        serviceVehicleRepository.updateOnGoingDueDateAndServiceTypeBasedOnVehicleId(vehicleId, mdServiceType.getId(), recommendedDueDate);
    }
}
