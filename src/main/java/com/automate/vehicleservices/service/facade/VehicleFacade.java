package com.automate.vehicleservices.service.facade;

import com.automate.vehicleservices.api.model.*;
import com.automate.vehicleservices.entity.Customer;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.builder.ServiceVehicleBuilder;
import com.automate.vehicleservices.entity.builder.VehicleInsuranceBuilder;
import com.automate.vehicleservices.entity.builder.VehicleKmTrackerBuilder;
import com.automate.vehicleservices.entity.builder.VehicleWarrantyBuilder;
import com.automate.vehicleservices.entity.enums.VehicleStatus;
import com.automate.vehicleservices.entity.enums.WarrantyStatus;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.framework.service.CrudService;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import com.automate.vehicleservices.service.MdTenantService;
import com.automate.vehicleservices.service.ServiceVehicleService;
import com.automate.vehicleservices.service.VehicleInsuranceService;
import com.automate.vehicleservices.service.dto.PageableResponse;
import com.automate.vehicleservices.service.dto.VehicleInsuranceDTO;
import com.automate.vehicleservices.service.dto.VehicleWarrantyDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Chandrashekar V
 */
@Component
public class VehicleFacade {

    private final ServiceVehicleService serviceVehicleService;
    private final CustomerFacade customerFacade;
    private final MdTenantService tenantService;
    private final CrudService crudService;
    private final VehicleInsuranceService vehicleInsuranceService;

    public VehicleFacade(ServiceVehicleService serviceVehicleService, CustomerFacade customerFacade,
                         MdTenantService tenantService, CrudService crudService,
                         VehicleInsuranceService vehicleInsuranceService) {
        this.serviceVehicleService = serviceVehicleService;
        this.customerFacade = customerFacade;
        this.tenantService = tenantService;
        this.crudService = crudService;
        this.vehicleInsuranceService = vehicleInsuranceService;
    }

    /**
     * Fetches Service vehicle if exists from database, based on vehicle registration number. If doesn't exists, creates
     * a new entity and respective vehicle km tracker and returns the entity to be persisted.
     *
     * @param vehicleDetails
     * @param customer
     * @return
     */
    public ServiceVehicle fetchOrCreateServiceVehicleEntity(VehicleDetails vehicleDetails, Customer customer,
                                                            MdTenant mdTenant) {
        ServiceVehicle vehicleWithRegNumber = serviceVehicleService
                .findByVehicleRegNumber(vehicleDetails.getVehicleRegNumber());

        if (Objects.nonNull(vehicleWithRegNumber) &&
                !StringUtils.equalsIgnoreCase(vehicleWithRegNumber.getCustomer().getContactNumber(),
                        customer.getContactNumber()))
            throw new VehicleServicesException(String.format("Vehicle %s is registered with different customer",
                    vehicleDetails.getVehicleRegNumber()));

        if (Objects.isNull(vehicleWithRegNumber)) {
            vehicleWithRegNumber = createServiceVehicleEntity(vehicleDetails, customer, mdTenant);
        }

        addVehicleTrackerEntity(vehicleWithRegNumber, vehicleDetails);

        return vehicleWithRegNumber;
    }

    public void addVehicleTrackerEntity(ServiceVehicle serviceVehicle, VehicleDetails vehicleDetails) {
        // Add child entity - VehicleKMTracker
        serviceVehicle.addVehicleKmTracker(
                VehicleKmTrackerBuilder.aVehicleKmTracker().withKmReading(vehicleDetails.getKmReading())
                        .withRecordedDate(LocalDateTime.now()).build());
    }

    /**
     * Creates ServiceVehicle entity from vehicle details request.
     *
     * @param vehicleDetails
     * @return
     */
    private ServiceVehicle createServiceVehicleEntity(VehicleDetails vehicleDetails, Customer customer,
                                                      MdTenant mdTenant) {

        ServiceVehicle serviceVehicle = ServiceVehicleBuilder.aServiceVehicle()
                .withChassisNumber(vehicleDetails.getChassisNumber())
                .withModel(vehicleDetails.getVehicleModel())
                .withPurchaseDate(vehicleDetails.getPurchaseDate())
                .withRegNumber(vehicleDetails.getVehicleRegNumber())
                .withFuelType(vehicleDetails.getFuelType())
                .withVin(vehicleDetails.getVin())
                .withMdTenant(mdTenant)
                .withStatus(VehicleStatus.ACTIVE)
                .withEngineNumber(vehicleDetails.getEngineNumber())
                .withColor(vehicleDetails.getColor())
                .withVariant(vehicleDetails.getVariant())
                .withSellingDealer(vehicleDetails.getSellingDealer())
                .withSellingLocation(vehicleDetails.getSellingLocation())
                .withTransmissionType(vehicleDetails.getTransmisionType())
                .withVehicleMake(vehicleDetails.getVehicleMake())
                .withVehicleMakeYear(vehicleDetails.getVehicleMakeYear())
                .build();
        customer.addCustomerVehicle(serviceVehicle);
        return serviceVehicle;
    }

    public List<ServiceVehicleDTO> fetchUnscheduledVehicles() {
        return serviceVehicleService.fetchUnscheduledVehicles();
    }

    @Transactional
    public PageableResponse<ServiceVehicleDTO> fetchUnscheduledVehiclesPaginated(final int pageNumber,
                                                                                 final int numberOfElementsPerPage) {
        return serviceVehicleService.fetchUnscheduledVehiclesPaginated(pageNumber, numberOfElementsPerPage);
    }

    /**
     * Adds new vehicle into the database. Initially does few validations.
     *
     * @param vehicleRequest
     * @param tenant
     * @return
     */
    @Transactional
    public ServiceVehicleDTO addNewVehicle(VehicleRequest vehicleRequest, String tenant) {
        final var byVehicleRegNumber =
                serviceVehicleService.findByVehicleRegNumber(vehicleRequest.getVehicleDetails().getVehicleRegNumber());
        if (Objects.nonNull(byVehicleRegNumber))
            throw new VehicleServicesException(String.format("Vehicle with reg number %s, already exists",
                    vehicleRequest.getVehicleDetails().getVehicleRegNumber()));

        final var mdTenant = tenantService.tenantByIdentifier(tenant);

        if (Objects.isNull(mdTenant))
            throw new VehicleServicesException(String.format("tenant %s doesn't exists. Vehicle can not be created",
                    tenant));

        final var customer = customerFacade.fetchOrConstructCustomerEntity(vehicleRequest.getCustomer(), mdTenant);

        final var serviceVehicle = fetchOrCreateServiceVehicleEntity(vehicleRequest.getVehicleDetails(), customer,
                mdTenant);

        final ServiceVehicle save = crudService.save(serviceVehicle);
        return new ServiceVehicleDTO(save);
    }

    @Transactional
    public VehicleInsuranceDTO addInsuranceDetails(String tenant, VehicleInsuranceRequest insuranceRequest,
                                                   String vehicleRegistrationNumber) {
        final ServiceVehicle byVehicleRegNumber = getServiceVehicle(vehicleRegistrationNumber);

        final var mdTenant = tenantService.tenantByIdentifier(tenant);
        final var vehicleInsurance = VehicleInsuranceBuilder.aVehicleInsurance()
                .withEndDate(insuranceRequest.getEndDate())
                .withStartDate(insuranceRequest.getStartDate())
                .withInsuranceAmount(insuranceRequest.getInsuranceAmount())
                .withProvider(insuranceRequest.getVendor())
                .withInsuranceIdentifier(insuranceRequest.getInsuranceIdentifier())
                .withCustomer(customerFacade.fetchOrConstructCustomerEntity(insuranceRequest.getCustomerRequest(),
                        mdTenant))
                .withServiceVehicle(byVehicleRegNumber).build();

        final var save = crudService.save(vehicleInsurance);

        return new VehicleInsuranceDTO(save);
    }

    @Transactional
    public ServiceVehicle addInsuranceDetailsToExistingVehicle(VehicleInsuranceDetails insuranceRequest,
                                                               ServiceVehicle serviceVehicle) {
        // Check if existing insurance  is matching with incoming insurance.
        if (Objects.isNull(insuranceRequest) || isInsuranceExistsAlready(insuranceRequest)) return serviceVehicle;

        // Add as a new insurance
        final var vehicleInsurance = VehicleInsuranceBuilder.aVehicleInsurance()
                .withEndDate(insuranceRequest.getEndDate())
                .withStartDate(insuranceRequest.getStartDate())
                .withInsuranceAmount(insuranceRequest.getInsuranceAmount())
                .withProvider(insuranceRequest.getVendor())
                .withInsuranceIdentifier(insuranceRequest.getPolicyNumber())
                .withCustomer(serviceVehicle.getCustomer())
                .withServiceVehicle(serviceVehicle)
                .build();
        serviceVehicle.addVehicleInsurance(vehicleInsurance);

        return crudService.save(serviceVehicle);
    }

    private boolean isInsuranceExistsAlready(VehicleInsuranceDetails insuranceRequest) {
        final var policyNumber = insuranceRequest.getPolicyNumber();
        final var byVehiclePolicyNumber = vehicleInsuranceService.findByVehiclePolicyNumber(policyNumber);
        if (Objects.nonNull(byVehiclePolicyNumber)
                && insuranceRequest.getPolicyNumber().equals(byVehiclePolicyNumber.getInsuranceIdentifier())) {
            if (!Objects.equals(byVehiclePolicyNumber.getEndDate(), insuranceRequest.getEndDate()))
                throw new VehicleServicesException("Existing vehicle insurance details holds different end date");

            if (!Objects.equals(byVehiclePolicyNumber.getProvider(), insuranceRequest.getVendor()))
                throw new VehicleServicesException("Existing vehicle insurance details holds different Vendor");

            return true;
        }
        return false;
    }

    private ServiceVehicle getServiceVehicle(String vehicleRegistrationNumber) {
        final var byVehicleRegNumber = serviceVehicleService.findByVehicleRegNumber(vehicleRegistrationNumber);
        if (Objects.isNull(byVehicleRegNumber))
            throw new VehicleServicesException(String.format("Vehicle %s, doesn't exists. Can not add insurance " +
                    "details.", vehicleRegistrationNumber));
        return byVehicleRegNumber;
    }

    public VehicleWarrantyDTO addWarrantyDetails(String tenant, VehicleWarrantyRequest warrantyRequest,
                                                 String vehicleRegistrationNumber) {
        final ServiceVehicle byVehicleRegNumber = getServiceVehicle(vehicleRegistrationNumber);

        final var mdTenant = tenantService.tenantByIdentifier(tenant);
        final var vehicleWarranty = VehicleWarrantyBuilder.aVehicleWarranty()
                .withExpiryDate(warrantyRequest.getExpiryDate())
                .withStartDate(warrantyRequest.getStartDate())
                .withAmountPaid(warrantyRequest.getAmountPaid())
                .withWarrantyTpe(warrantyRequest.getWarrantyType())
                .withAmcName(warrantyRequest.getAmc_name())
                .withEwName(warrantyRequest.getEwName())
                .withFastagStatus(warrantyRequest.getFastagStatus())
                .withOemPeriod(warrantyRequest.getOemPeriod())
                .withStatus(WarrantyStatus.ACTIVE)
                .withMdTenant(mdTenant)
                .withServiceVehicle(byVehicleRegNumber).build();

        final var save = crudService.save(vehicleWarranty);

        return new VehicleWarrantyDTO(save);

    }

    @Transactional
    public ServiceVehicleDTO mapVehicleToCustomer(final int customer, final String vehicleRegNumber) {
        final ServiceVehicle serviceVehicle = getServiceVehicle(vehicleRegNumber);
        if (Objects.nonNull(serviceVehicle)) {
            final var byId = crudService.findById(customer, Customer.class);
            if (byId.isPresent())
                serviceVehicle.setCustomer(byId.get());
            final var save = crudService.save(serviceVehicle);
            return new ServiceVehicleDTO(save);
        }
        throw new VehicleServicesException(String.format("Vehicle with reg number %s, mapping failed to customer %d",
                vehicleRegNumber, customer));
    }

}
