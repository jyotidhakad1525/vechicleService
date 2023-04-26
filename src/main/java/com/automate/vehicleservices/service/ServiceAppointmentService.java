package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.AddressLabel;
import com.automate.vehicleservices.api.model.AddressRequest;
import com.automate.vehicleservices.api.model.AppointmentRequest;
import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.entity.*;
import com.automate.vehicleservices.entity.builder.ServiceAppointmentBuilder;
import com.automate.vehicleservices.entity.builder.ServiceSlotAvailabilityBuilder;
import com.automate.vehicleservices.entity.enums.ServiceAppointmentStatus;
import com.automate.vehicleservices.event.AppointmentBookedEvent;
import com.automate.vehicleservices.framework.event.EventPublisher;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.ServiceAppointmentRepository;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
import com.automate.vehicleservices.service.dto.ServiceAppointmentResponse;
import com.automate.vehicleservices.service.facade.CustomerFacade;
import com.automate.vehicleservices.service.facade.VehicleFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ServiceAppointmentService extends AbstractService {
    private final ServiceAppointmentRepository serviceAppointmentRepository;

    private final ServiceSlotAvailabilityService slotAvailabilityService;

    private final CustomerFacade customerFacade;

    private final VehicleFacade vehicleFacade;

    private final CustomerAddressService customerAddressService;

    private final PhoneNumberUtil phoneNumberUtil;
    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    public ServiceAppointmentService(ServiceAppointmentRepository serviceAppointmentRepository,
                                     ServiceSlotAvailabilityService slotAvailabilityService,
                                     CustomerFacade customerFacade, VehicleFacade vehicleFacade,
                                     CustomerAddressService customerAddressService, PhoneNumberUtil phoneNumberUtil) {
        this.serviceAppointmentRepository = serviceAppointmentRepository;
        this.slotAvailabilityService = slotAvailabilityService;
        this.customerFacade = customerFacade;
        this.vehicleFacade = vehicleFacade;
        this.customerAddressService = customerAddressService;
        this.phoneNumberUtil = phoneNumberUtil;
    }

    public ServiceAppointmentResponse bookAppointment(AppointmentRequest appointmentRequest, String tenant) {
        boolean isEdit = false;
        ServiceAppointment existingAppointment = null;
        if (Objects.nonNull(appointmentRequest.getId())) {
            Optional<ServiceAppointment> appointmentEntityOptional = serviceAppointmentRepository.findByIdAndMdTenant_TenantIdentifier(appointmentRequest.getId(),tenant);
            if (!appointmentEntityOptional.isPresent()) {
                log.error("Appointment not found!!");
//                throw new VehicleServicesException(HttpStatus.NOT_FOUND,"Appointment not found!!");
            } else {
                isEdit = true;
                existingAppointment = appointmentEntityOptional.get();
            }
        }


        MdTenant mdTenant = tenantByIdentifier(tenant);
        MdServiceCategory serviceCategory = serviceCategory(appointmentRequest);
        MdServiceType mdServiceType = null;
        if(Objects.nonNull(appointmentRequest.getSubServiceId())){
            mdServiceType = serviceType(appointmentRequest.getSubServiceId());
        }
        MdServiceSlot slot = this.slotAvailabilityService.validateAndReturnSlot(appointmentRequest.getTimeSlotId(),
                appointmentRequest
                        .getServiceRequestDate(), mdTenant.getId(),isEdit);
        ServiceAppointment appointmentEntity = appointment(appointmentRequest, mdTenant, serviceCategory, slot, isEdit);
        if (isEdit){
            appointmentEntity.setId(appointmentRequest.getId());
            appointmentEntity.setBOOKING_ID(existingAppointment.getBOOKING_ID());
        }else{
            Integer bookingId = this.serviceAppointmentRepository.getLastBookingId();
            appointmentEntity.setBOOKING_ID(++bookingId);
        }
        appointmentEntity.setMdServiceType(mdServiceType);
        appointmentEntity.setModifiedReason(appointmentRequest.getReason());
        appointmentEntity.setRemarks(appointmentRequest.getRemark());
        appointmentEntity.setAdvisorName(appointmentRequest.getAdvisorName());
        appointmentEntity.setServiceCenterCode(appointmentRequest.getServiceCenterCode());
        ServiceAppointment appointment = this.serviceAppointmentRepository.save(appointmentEntity);

        final var serviceAppointmentResponse = new ServiceAppointmentResponse(appointment);
        eventPublisher.publish(AppointmentBookedEvent.builder().serviceAppointmentResponse(serviceAppointmentResponse).build());
        return serviceAppointmentResponse;
    }

    private void throwExceptionIfAppointmentAlreadyExists(AppointmentRequest appointmentRequest) {
        List<ServiceAppointment> existingAppointments =
                this.serviceAppointmentRepository.findByServiceDateAndServiceVehicleRegNumberAndStatusIn(appointmentRequest.getServiceRequestDate(), appointmentRequest
                                .getVehicleDetails().getVehicleRegNumber(),
                        ServiceAppointmentStatus.BOOKED,
                        ServiceAppointmentStatus.CONFIRMED);
        if (!existingAppointments.isEmpty())
            throw new VehicleServicesException(
                    String.format("Appointment already exists for the vehicle %s on the date: %s ",
                            appointmentRequest.getVehicleDetails().getVehicleRegNumber(),
                            appointmentRequest
                                    .getServiceRequestDate()));
    }

    private ServiceAppointment appointment(AppointmentRequest appointmentRequest, MdTenant tenant,
                                           MdServiceCategory serviceCategory, MdServiceSlot slot , Boolean isEdit) {
        if (!isEdit)
            throwExceptionIfAppointmentAlreadyExists(appointmentRequest);
        Customer customer = fetchOrConstructCustomerEntity(appointmentRequest, tenant);
        CustomerAddress pickupAddressEntity = getCustomerAddress(customer, appointmentRequest.getPickupAddress());
        CustomerAddress dropAddressEntity = getCustomerAddress(customer, appointmentRequest.getDropAddress());
        ServiceVehicle serviceVehicle =
                this.vehicleFacade.fetchOrCreateServiceVehicleEntity(appointmentRequest.getVehicleDetails(), customer
                        , tenant);
        AddressLabel pickupAddressLabel = (null != pickupAddressEntity) ? pickupAddressEntity.getAddressLabel() : null;
        AddressLabel dropAddressLabel = (null != dropAddressEntity) ? dropAddressEntity.getAddressLabel() : null;

        Driver driver = getDriver(appointmentRequest, null);
        ServiceAppointment serviceAppointment = buildAppointmentEntity(appointmentRequest, tenant, serviceCategory,
                slot, customer, pickupAddressEntity, dropAddressEntity, serviceVehicle, pickupAddressLabel,
                dropAddressLabel,driver);

        return serviceAppointment;
    }

    private Driver getDriver(AppointmentRequest appointmentRequest, Driver driver) {
        if (Objects.nonNull(appointmentRequest.getDriverId())) {
            Optional<Driver> driverOptional = this.crudService.findById(appointmentRequest.getDriverId(), Driver.class);
            if (!driverOptional.isPresent()) {
                throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Driver detail not found!!");
            }
            driver = driverOptional.get();
        }
        return driver;
    }

    private ServiceAppointment buildAppointmentEntity(AppointmentRequest appointmentRequest, MdTenant tenant,
                                                      MdServiceCategory serviceCategory, MdServiceSlot slot,
                                                      Customer customer, CustomerAddress pickupAddressEntity,
                                                      CustomerAddress dropAddressEntity,
                                                      ServiceVehicle serviceVehicle, AddressLabel pickupAddressLabel,
                                                      AddressLabel dropAddressLabel,Driver driver) {
        ServiceAppointmentBuilder serviceAppointmentBuilder =
                ServiceAppointmentBuilder.aServiceAppointment()
                        .withDropRequired(Boolean.valueOf(appointmentRequest.isDropRequired()))
                        .withPickupRequired(Boolean.valueOf(appointmentRequest.isPickupRequired()))
                        .withPickupTime(appointmentRequest.getPickupTime())
                        .withDropTime(appointmentRequest.getDropTime())
                        .withCustomer(customer).withServiceVehicle(serviceVehicle)
                        .withPickupAddress(pickupAddressEntity)
                        .withPickUpAddressLabel(pickupAddressLabel)
                        .withDropAddress(dropAddressEntity)
                        .withIsSelfDrive(appointmentRequest.isSelfDrive())
                        .withDoorStepService(appointmentRequest.isDoorStepService())
                        .withDropAddressLabel(dropAddressLabel)
                        .withMdTenant(tenant)
                        .withServiceDate(appointmentRequest.getServiceRequestDate())
                        .withSourceOfAppointment(appointmentRequest.getSourceOfRequest())
                        .withMdServiceCategory(serviceCategory).withCity(appointmentRequest.getCity())
                        .withServiceSlotAvailability(ServiceSlotAvailabilityBuilder.aServiceSlotAvailability()
                                .withMdServiceSlot(slot).withMdTenant(tenant).withIsActive(true)
                                .withServiceDate(appointmentRequest.getServiceRequestDate()).build())
                        .withStatus(ServiceAppointmentStatus.BOOKED)
                        .withDriver(driver);
        return serviceAppointmentBuilder.build();
    }

    private Customer fetchOrConstructCustomerEntity(AppointmentRequest appointmentRequest, MdTenant tenant) {
        return this.customerFacade.fetchOrConstructCustomerEntity(appointmentRequest.getCustomerRequest(), tenant);
    }

    private CustomerAddress getCustomerAddress(Customer customer, AddressRequest addressRequest) {
        CustomerAddress pickupAddressEntity = null;
        if (Objects.nonNull(addressRequest)) {
            pickupAddressEntity = this.customerAddressService.fetchOrCreateAddressEntity(customer, addressRequest);
            customer.addCustomerAddress(pickupAddressEntity);
        }
        return pickupAddressEntity;
    }

    private MdTenant tenantByIdentifier(String tenantIdentifier) {
        return this.tenantService.tenantByIdentifier(tenantIdentifier);
    }

    private MdServiceCategory serviceCategory(AppointmentRequest appointmentRequest) {
        Optional<MdServiceCategory> serviceCategory =
                this.crudService.findById(Integer.valueOf(appointmentRequest.getServiceCategoryId()),
                        MdServiceCategory.class);
        if (serviceCategory.isEmpty())
            throw new VehicleServicesException(String.format("Requested service Category : %d, doesn't exist.",
                    Integer.valueOf(appointmentRequest.getServiceCategoryId())));
        return serviceCategory.get();
    }

    private MdServiceType serviceType(Integer serviceTypeId) {
        Optional<MdServiceType> serviceType =
                this.crudService.findById(serviceTypeId,
                        MdServiceType.class);
        if (serviceType.isEmpty())
            throw new VehicleServicesException(String.format("Requested service type : %d, doesn't exist.", serviceTypeId));
        return serviceType.get();
    }

    @Transactional
    public PaginatedSearchResponse<ServiceAppointmentResponse> activeAppointments(String tenant,
                                                                                  Pagination pagination) {
        Specification<ServiceAppointment> appointmentByTenant = ServiceAppointment.byTenantIdentifier(tenant);
        Specification<ServiceAppointment> appointmentsByDateTodayOrLater =
                ServiceAppointment.appointmentsByDateTodayOrLater();
        Page<ServiceAppointment> serviceAppointmentPage =
                this.serviceAppointmentRepository.findAll(appointmentByTenant.and(appointmentsByDateTodayOrLater),
                        PageRequest.of(pagination.getPage(), pagination
                                .getSize()));
        return extractPaginatedResponse(serviceAppointmentPage);
    }

    @Transactional
    public List<ServiceAppointmentResponse> activeAppointments(String tenant, String vehicleRegNumber) {
        LocalDate today = LocalDate.now();
        List<ServiceAppointment> appointments =
                this.serviceAppointmentRepository.findByServiceDateEqualsOrServiceDateAfterAndMdTenant_TenantIdentifierAndServiceVehicle_RegNumber(today, today, tenant, vehicleRegNumber);
        return returnResponseData(appointments);
    }

    @Transactional
    public List<ServiceAppointmentResponse> allAppointments(String tenant, String vehicleRegNumber) {
        List<ServiceAppointment> appointments =
                this.serviceAppointmentRepository.findByMdTenant_TenantIdentifierAndServiceVehicle_RegNumber(tenant,
                        vehicleRegNumber);
        return returnResponseData(appointments);
    }

    private List<ServiceAppointmentResponse> returnResponseData(List<ServiceAppointment> appointments) {
        if (Objects.nonNull(appointments))
            return appointments.stream().map(ServiceAppointmentResponse::new).collect(Collectors.toList());
        return Collections.emptyList();
    }

    public PaginatedSearchResponse<ServiceAppointmentResponse> fetchAppointmentsByOrgAndCustomer(String org,
                                                                                                 String customerContactNumber, Pagination pagination, Integer slotId) {
        if (StringUtils.isBlank(customerContactNumber))
            throw new VehicleServicesException("Customer contact number is required to fetch appointments.");
        String cleansedPhoneNumber = this.phoneNumberUtil.cleansePhoneNumber(customerContactNumber);
        Specification<ServiceAppointment> appointmentByOrg =
                ServiceAppointment.appointmentByOrgIdentifierSpecification(org);
        Page<ServiceAppointment> serviceAppointmentPage;
        if (Objects.isNull(slotId)) {
            Specification<ServiceAppointment> appointmentByCustomerContactNumber =
                    ServiceAppointment.appointmentByCustomerContactNumber(cleansedPhoneNumber);
            serviceAppointmentPage =
                    this.serviceAppointmentRepository.findAll(appointmentByOrg.and(appointmentByCustomerContactNumber),
                            PageRequest.of(pagination.getPage(), pagination.getSize()));
        } else {
            Specification<ServiceAppointment> basedOnSlotId =
                    ServiceAppointment.compareMdServiceSlotId(slotId);
            Specification<ServiceAppointment> statusSpecification =
                    ServiceAppointment.checkStatusSpecification(List.of(ServiceAppointmentStatus.BOOKED,ServiceAppointmentStatus.CONFIRMED));

            serviceAppointmentPage =
                    this.serviceAppointmentRepository.findAll(appointmentByOrg.and(basedOnSlotId).and(statusSpecification),
                            PageRequest.of(pagination.getPage(), pagination.getSize()));
        }

        return extractPaginatedResponse(serviceAppointmentPage);
    }

    private PaginatedSearchResponse<ServiceAppointmentResponse> extractPaginatedResponse(Page<ServiceAppointment> serviceAppointmentPage) {
        List<ServiceAppointment> content = serviceAppointmentPage.getContent();
        if (CollectionUtils.isNotEmpty(content))
            return new PaginatedSearchResponse(serviceAppointmentPage, content
                    .stream().map(ServiceAppointmentResponse::new).collect(Collectors.toList()));
        return new PaginatedSearchResponse(serviceAppointmentPage, Collections.emptyList());
    }

    @Transactional
    public void cancelAppointment(String tenant, int id, String reason, String remark) {
        Optional<ServiceAppointment> appointmentEntityOptional = serviceAppointmentRepository.findByIdAndMdTenant_TenantIdentifier(id, tenant);
        if (!appointmentEntityOptional.isPresent()) {
            log.error("Appointment not found!!");
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Appointment not found!!");
        }
        ServiceAppointment  serviceAppointment = appointmentEntityOptional.get();
        serviceAppointment.setStatus(ServiceAppointmentStatus.CANCELLED);
        serviceAppointment.setRemarks(remark);
        serviceAppointment.setModifiedReason(reason);
        serviceAppointmentRepository.save(serviceAppointment);
    }

    @Transactional
    public boolean rescheduleAppointment(String tenant, int id) {
        return updateStatus(tenant, id, ServiceAppointmentStatus.RESCHEDULED);
    }

    private boolean updateStatus(String tenant, int id, ServiceAppointmentStatus statusToBeUpdated) {
        if (!this.serviceAppointmentRepository.existsByIdAndMdTenant_TenantIdentifier(id, tenant))
            throw new VehicleServicesException(HttpStatus.NOT_FOUND,
                    String.format("Appointment not found for the tenant %s", tenant));
        this.serviceAppointmentRepository.updateAppointmentStatus(statusToBeUpdated, id);
        return true;
    }

    @Transactional
    public ServiceAppointmentResponse rescheduleAppointment(String tenant, int id,
                                                            AppointmentRequest appointmentRequest) {
        rescheduleAppointment(tenant, id);
        return bookAppointment(appointmentRequest, tenant);
    }
}
