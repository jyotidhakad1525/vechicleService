package com.automate.vehicleservices.service.facade;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.automate.vehicleservices.api.model.BulkUploadResponse;
import com.automate.vehicleservices.api.model.CustomerRequest;
import com.automate.vehicleservices.api.model.UpdateCustomerRequest;
import com.automate.vehicleservices.entity.Customer;
import com.automate.vehicleservices.entity.CustomerAddress;
import com.automate.vehicleservices.entity.CustomerTenant;
import com.automate.vehicleservices.entity.MdLeadSource;
import com.automate.vehicleservices.entity.MdServiceCategory;
import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.VehicleInsurance;
import com.automate.vehicleservices.entity.VehicleKmTracker;
import com.automate.vehicleservices.entity.VehicleServiceHistory;
import com.automate.vehicleservices.entity.VehicleWarranty;
import com.automate.vehicleservices.entity.builder.CustomerBuilder;
import com.automate.vehicleservices.entity.enums.FuelType;
import com.automate.vehicleservices.entity.enums.VehicleStatus;
import com.automate.vehicleservices.entity.enums.WarrantyType;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.framework.service.CrudService;
import com.automate.vehicleservices.repository.MdServiceCategoryRepository;
import com.automate.vehicleservices.repository.MdServiceTypeRepository;
import com.automate.vehicleservices.repository.MdTenantRepository;
import com.automate.vehicleservices.repository.ServiceVehicleRepository;
import com.automate.vehicleservices.repository.VehicleInsuranceRepository;
import com.automate.vehicleservices.repository.VehicleServiceHistoryRepository;
import com.automate.vehicleservices.service.CustomerAddressService;
import com.automate.vehicleservices.service.CustomerService;
import com.automate.vehicleservices.service.MdTenantService;
import com.automate.vehicleservices.service.dto.CustomerDTO;

/**
 * Chandrashekar V
 */
@Component
public class CustomerFacade {

    private final CustomerService customerService;

    private final MdTenantService tenantService;

    private final CrudService crudService;
    private final CustomerAddressService customerAddressService;
    private final MdTenantRepository mdTenantRepository;
	private final VehicleInsuranceRepository vehicleInsuranceRepository;
	private final VehicleServiceHistoryRepository vehicleServiceHistoryRepository;
	private final ServiceVehicleRepository serviceVehicleRepository;
	private final MdServiceCategoryRepository mdServiceCategoryRepository;
	private final MdServiceTypeRepository mdServiceTypeRepository;

    public CustomerFacade(CustomerService customerService, MdTenantService tenantService, CrudService crudService,
                          CustomerAddressService customerAddressService, MdTenantRepository mdTenantRepository,
              			ServiceVehicleRepository serviceVehicleRepository, VehicleInsuranceRepository vehicleInsuranceRepository,
            			VehicleServiceHistoryRepository vehicleServiceHistoryRepository,MdServiceCategoryRepository mdServiceCategoryRepository,
            			MdServiceTypeRepository mdServiceTypeRepository) {
        this.customerService = customerService;
        this.tenantService = tenantService;
        this.crudService = crudService;
        this.customerAddressService = customerAddressService;
        this.mdTenantRepository = mdTenantRepository;
		this.serviceVehicleRepository = serviceVehicleRepository;
		this.vehicleInsuranceRepository = vehicleInsuranceRepository;
		this.vehicleServiceHistoryRepository = vehicleServiceHistoryRepository;
		this.mdServiceCategoryRepository=mdServiceCategoryRepository;
		this.mdServiceTypeRepository=mdServiceTypeRepository;
	}

    /**
     * Fetches customer if existing, else creates new one.
     *
     * @param customerRequest
     * @param tenant
     * @return
     */
    public Customer fetchOrConstructCustomerEntity(CustomerRequest customerRequest, MdTenant tenant) {
        List<Customer> customers = customerService.findByContactNumber(customerRequest.getContactNumber());

        if (CollectionUtils.isEmpty(customers)) {
            // create new customer entity
            return customerEntityFromCustomerRequest(customerRequest, tenant);
        }
        if (customers.size() > 1)
            throw new VehicleServicesException(String.format("More than one customer returned for the given contact " +
                    "number: %s", customerRequest.getContactNumber()));

        return customers.get(0);
    }

    /**
     * Verifies and returns if any customer exists with given contact number, if not creates a new entity and returns.
     *
     * @param customerRequest
     * @param mdTenant
     * @return
     */
    public Customer customerEntityFromCustomerRequest(@NotNull CustomerRequest customerRequest,
                                                      final MdTenant mdTenant) {

        if (StringUtils.isBlank(customerRequest.getContactNumber()))
            throw new VehicleServicesException("Contact number is required to proceed with estimation request.");

        Customer customer = customerEntity(customerRequest);

        customer.addCustomerTenant(CustomerTenant.builder().mdTenant(mdTenant).customer(customer).build());
        return customer;
    }

    public Customer customerEntity(@NotNull CustomerRequest customerRequest) {
        final CustomerBuilder customerBuilder = getCustomerBuilder(customerRequest);

        final var addresses = customerRequest.getAddresses();
        if (!CollectionUtils.isEmpty(addresses)) {
            final var customerAddresses =
                    addresses.stream().map(entry -> customerAddressService.createAddressEntity(entry)).collect(Collectors.toList());
            customerBuilder.withCustomerAddresses(customerAddresses);
        }
        return customerBuilder.build();

    }

    private CustomerBuilder getCustomerBuilder(@NotNull CustomerRequest customerRequest) {
        final var leadSource = crudService.findById(customerRequest.getLeadSource(), MdLeadSource.class);
        return CustomerBuilder.aCustomer()
                .withContactNumber(customerRequest
                        .getContactNumber()).withEmail(customerRequest.getEmail())
                .withFirstName(customerRequest.getFirstName())
                .withLastName(customerRequest.getLastName())
                .withAltContactNumber(customerRequest.getAlternateContactNumber())
                .withGender(customerRequest.getGender())
                .withActive(true)
                .withDateOfArrival(customerRequest.getDateOfArrival())
                .withDateOfBirth(customerRequest.getDateOfBirth())
                .withCustomerType(customerRequest.getCustomerType())
                .withOccupation(customerRequest.getOccupation())
                .withReferedBy(customerRequest.getRefered_by())
                .withLeadSource(leadSource.orElse(null));
    }


    /**
     * Verifies and returns if any customer exists with given contact number, if not creates a new entity and returns.
     *
     * @param customerRequest
     * @param mdTenant
     * @return
     */
    public CustomerDTO saveCustomer(@NotNull CustomerRequest customerRequest,
                                    final String mdTenant) {
        List<Customer> customers = customerService.findByContactNumber(customerRequest.getContactNumber());
        if (CollectionUtils.isNotEmpty(customers))
            throw new VehicleServicesException("Another customer exists with same contact number.");

        final var customer = customerEntityFromCustomerRequest(customerRequest,
                tenantService.tenantByIdentifier(mdTenant));
        final var save = crudService.save(customer);
        return new CustomerDTO(save);
    }

    @Transactional
    public CustomerDTO updateCustomer(int customerId, UpdateCustomerRequest customerRequest, String tenant) {

        final var customerOpt = customerService.getCustomer(customerId);
        if (customerOpt.isEmpty())
            throw new VehicleServicesException("Customer doesn't exist to update.");

        final var leadSource = crudService.findById(customerRequest.getLeadSource(), MdLeadSource.class);

        Customer customer = customerOpt.get();
        customer.setContactNumber(customerRequest
                .getContactNumber());
        customer.setEmail(customerRequest.getEmail());
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setAltContactNumber(customerRequest.getAlternateContactNumber());
        customer.setGender(customerRequest.getGender());
        customer.setDateOfArrival(customerRequest.getDateOfArrival());
        customer.setDateOfBirth(customerRequest.getDateOfBirth());
        customer.setCustomerType(customerRequest.getCustomerType());
        customer.setOccupation(customerRequest.getOccupation());
        customer.setRefered_by(customerRequest.getRefered_by());
        customer.setAge(customerRequest.getAge());
        customer.setRelationName(customerRequest.getRelationName());
        customer.setSalutation(customerRequest.getSalutation());
        customer.setLeadSource(leadSource.orElse(null));

        var customerAddresses = customer.getCustomerAddresses();
        Map<Integer, CustomerAddress> customerAddressMap = null;
        if (CollectionUtils.isNotEmpty(customerAddresses)) {
            customerAddressMap = customerAddresses.stream().collect(Collectors.toMap(CustomerAddress::getId,
                    Function.identity()));
        }

        final Map<Integer, CustomerAddress> existingMappedAddressesMap = customerAddressMap;
        final var addresses = customerRequest.getAddresses();
        if (MapUtils.isNotEmpty(addresses))
            addresses.entrySet()
                    .forEach(entry -> {
                        if (existingMappedAddressesMap.containsKey(entry.getKey())) {
                            customerAddressService.updateCustomerAddressEntity(existingMappedAddressesMap.get(entry.getKey()),
                                    entry.getValue());
                        } else {
                            final var addressEntity = customerAddressService.createAddressEntity(entry.getValue());
                            customer.addCustomerAddress(addressEntity);
                        }
                    });

        final var save = crudService.save(customer);
        return new CustomerDTO(save);
    }

    public BulkUploadResponse processBulkExcelForCustomerUpload(MultipartFile bulkExcel, String tenant)
			throws Exception {
		Resource file = null;
		if (bulkExcel.isEmpty()) {
			BulkUploadResponse res = new BulkUploadResponse();
			List<String> FailedRecords = new ArrayList<>();
			String resonForFailure = "File not found";
			FailedRecords.add(resonForFailure);
			res.setFailedCount(0);
			res.setFailedRecords(FailedRecords);
			res.setSuccessCount(0);
			res.setTotalCount(0);
			return res;
		}
		Path tmpDir = Files.createTempDirectory("temp");
		Path tempFilePath = tmpDir.resolve(bulkExcel.getOriginalFilename());
		Files.write(tempFilePath, bulkExcel.getBytes());
		String fileName = bulkExcel.getOriginalFilename();
		fileName = fileName.substring(0, fileName.indexOf("."));
		return bulkExcelForCustomerUploadList(tempFilePath.toString(), tenant);
	}
    
    public BulkUploadResponse bulkExcelForCustomerUploadList(String inputFilePath, String tenant) throws Exception {
		Workbook workbook = null;
		Sheet sheet = null;
		workbook = getWorkBook(new File(inputFilePath));
		sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		List<String> FailedRecords = new ArrayList<>();
		int TotalCount = -1;
		int SuccessCount = 0;
		int FailedCount = 0;
		int emptyCheck = 0;
		int j = 0;
		MdTenant mdTenant = mdTenantRepository.findByTenantIdentifier(tenant);
		BulkUploadResponse res = new BulkUploadResponse();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		while (rowIterator.hasNext()) {
			TotalCount++;
			Row row = rowIterator.next();
			j++;
			try {
				if (row.getRowNum() != 0) {
					emptyCheck++;
					Customer customer = new Customer();
					ServiceVehicle serviceVehicle = new ServiceVehicle();
					VehicleServiceHistory vecHistory = new VehicleServiceHistory();
					VehicleInsurance vehicleInsurance = new VehicleInsurance();
					VehicleWarranty vehicleWarranty = new VehicleWarranty();
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 0))) {
						try {
							customer.setFirstName(getCellValueBasedOnCellType(row, 0));
						} catch (Exception ex) {
							FailedRecords.add("FirstName field cannot be blank");
							throw new Exception("FirstName field cannot be blank");
						}
					} else {
						throw new Exception("FirstName field cannot be blank");
					}

					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 1))) {
						try {
							customer.setLastName(getCellValueBasedOnCellType(row, 1));
						} catch (Exception ex) {
							FailedRecords.add("LastName field cannot be blank");
							throw new Exception("LastName field cannot be blank");
						}
					} else {
						throw new Exception("LastName field cannot be blank");
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 2))) {
						try {
							customer.setDateOfBirth(LocalDate.parse(getCellValueBasedOnCellType(row, 2),formatter));
						} catch (IllegalArgumentException ex) {
							FailedRecords.add("FirstName field cannot be blank");
							throw new Exception("FirstName field cannot be blank");
						}
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 3))) {
						try {
							System.out.println(getCellValueBasedOnCellType(row, 3));
							String s=getCellValueBasedOnCellType(row, 3);
							customer.setContactNumber(getCellValueBasedOnCellType(row, 3));
						} catch (Exception ex) {
							FailedRecords.add("ContactNumber field cannot be blank");
							throw new Exception("ContactNumber field cannot be blank");
						}
					} else {
						throw new Exception("ContactNumber field cannot be blank");
					}

					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 4))) {
						customer.setEmail(getCellValueBasedOnCellType(row, 4));
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 5))) {
						customer.setOccupation(getCellValueBasedOnCellType(row, 5));
					}
					//customer.setCommunicationPreference("PHONE");
					customer.setActive(true);
					customer.setCustomerType("Individual");
					customer.setDateOfArrival(LocalDate.now());
					customer.setCreatedDate(LocalDateTime.now());
					customer.setLastModifiedBy("Admin");
					customer.setLastModifiedDate(LocalDateTime.now());
					customer.setCreatedBy("Admin");
					Customer saveCustomer=null;
					try {
						saveCustomer = crudService.save(customer);
					}catch(Exception e) {
					}
					serviceVehicle.setCustomer(saveCustomer);
					serviceVehicle.setStatus(VehicleStatus.ACTIVE);
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 8))) {
						serviceVehicle.setRegNumber(getCellValueBasedOnCellType(row, 8));
					} else {
						String resonForFailure = "RegNumber field can not be blank" + j;
						FailedRecords.add(resonForFailure);
						throw new Exception("RegNumber field cannot be blank");
					}
//
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 9))) {
						serviceVehicle.setVehicleMake(getCellValueBasedOnCellType(row, 9));
					} else {
						String resonForFailure = "VehicleMake field can not be blank" + j;
						FailedRecords.add(resonForFailure);
						throw new Exception("VehicleMake field cannot be blank");
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 10))) {
						serviceVehicle.setModel(getCellValueBasedOnCellType(row, 10));
					} else {
						String resonForFailure = "Model field can not be blank" + j;
						FailedRecords.add(resonForFailure);
						throw new Exception("Model field cannot be blank");
					}

					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 11))) {
						serviceVehicle.setVariant(getCellValueBasedOnCellType(row, 11));
					} else {
						String resonForFailure = "Variant field can not be blank" + j;
						FailedRecords.add(resonForFailure);
						throw new Exception("Variant field cannot be blank");
					}

					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 12))) {
						serviceVehicle.setTransmisionType(getCellValueBasedOnCellType(row, 12));
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 13))) {
						if (getCellValueBasedOnCellType(row, 13).equalsIgnoreCase("PETROL")) {
							serviceVehicle.setFuelType(FuelType.PETROL);
						}
						if (getCellValueBasedOnCellType(row, 13).equalsIgnoreCase("DIESEL")) {
							serviceVehicle.setFuelType(FuelType.DIESEL);
						}
						if (getCellValueBasedOnCellType(row, 13).equalsIgnoreCase("ELECTRIC")) {
							serviceVehicle.setFuelType(FuelType.ELECTRIC);
						}
						if (getCellValueBasedOnCellType(row, 13).equalsIgnoreCase("HYBRID")) {
							serviceVehicle.setFuelType(FuelType.HYBRID);
						}
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 14))) {
						serviceVehicle.setColor(getCellValueBasedOnCellType(row, 14));
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 15))) {
						serviceVehicle.setVin(getCellValueBasedOnCellType(row, 15));
					} else {
						String resonForFailure = "Vin field can not be blank" + j;
						System.out.println(resonForFailure);
						FailedRecords.add(resonForFailure);
						throw new Exception("Vin field cannot be blank");
					}

					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 16))) {
						serviceVehicle.setEngineNumber(getCellValueBasedOnCellType(row, 16));
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 17))) {
						serviceVehicle.setCurrentKmReading(Integer.valueOf(getCellValueBasedOnCellType(row, 17)));
					}else {
						serviceVehicle.setCurrentKmReading(1);
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 18))) {
						try {
							serviceVehicle
									.setPurchaseDate(LocalDate.parse(getCellValueBasedOnCellType(row, 18), formatter));
						} catch (Exception ex) {
							FailedRecords.add("ContactNumber field cannot be blank");
							throw new Exception("Provide Purchase Date in dd-MM-yyyy Format");
						}
					} else {
						String resonForFailure = "Selling date field can not be blank" + j;
						System.out.println(resonForFailure);
						FailedRecords.add(resonForFailure);
						throw new Exception("Selling date field cannot be blank");
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 19))) {
						serviceVehicle.setMakingMonth(getCellValueBasedOnCellType(row, 19));
					}
//					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 20))) {
//						try {
//							String makeYearDate="01-01-"+getCellValueBasedOnCellType(row, 20);
//							DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//							serviceVehicle.setVehicleMakeYear(
//									LocalDate.parse(makeYearDate, formatter1));
//						} catch (Exception ex) {
//							FailedRecords.add("ContactNumber field cannot be blank");
//							throw new Exception("Provide Some Date in dd-MM-yyyy Format");
//						}
//					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 21))) {
						serviceVehicle.setSellingDealer(getCellValueBasedOnCellType(row, 21));
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 22))) {
						serviceVehicle.setSellingLocation(getCellValueBasedOnCellType(row, 22));
					}
					serviceVehicle.setCreatedBy("Admin");
					serviceVehicle.setCreatedDate(LocalDateTime.now());
					serviceVehicle.setLastModifiedBy("Admin");
					serviceVehicle.setLastModifiedDate(LocalDateTime.now());
					serviceVehicle.setMdTenant(mdTenant);
					MdServiceCategory subServiceCategory = mdServiceCategoryRepository.findFirstByMdTenant_TenantIdentifierAndCategoryName(tenant, getCellValueBasedOnCellType(row, 27));
					MdServiceType serviceType = mdServiceTypeRepository.getServiceTypeByTenantCategoryAndServiceType(subServiceCategory.getId(),getCellValueBasedOnCellType(row, 26),tenant);
					serviceVehicle.setServiceType(serviceType);
					serviceVehicle.setDueDate(LocalDate.now());
					ServiceVehicle saveServiceVehicle=null;
					try {
						try {
							 saveServiceVehicle = serviceVehicleRepository.save(serviceVehicle);
						}catch(Exception e){
							saveServiceVehicle=serviceVehicleRepository.findByRegNumber(getCellValueBasedOnCellType(row, 8));
							System.out.println(e);
						}
						if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 24))) {
							vecHistory.setServiceDate(LocalDate.parse(getCellValueBasedOnCellType(row, 24), formatter));
							if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 17))) {
								List<VehicleKmTracker> vehicleKmTrackers = new ArrayList<>();
								VehicleKmTracker obj = new VehicleKmTracker();
								obj.setServiceVehicle(saveServiceVehicle);
								obj.setKmReading(Integer.valueOf(getCellValueBasedOnCellType(row, 17)));
								obj.setSource("Bulk Upload");
								obj.setRecordedDate(LocalDate.parse(getCellValueBasedOnCellType(row, 24), formatter)
										.atStartOfDay());
								vehicleKmTrackers.add(obj);
								saveServiceVehicle.setVehicleKmTrackers(vehicleKmTrackers);
								try {
								saveServiceVehicle = serviceVehicleRepository.save(saveServiceVehicle);
								}catch(Exception e){
									saveServiceVehicle=serviceVehicleRepository.findByRegNumber(getCellValueBasedOnCellType(row, 8));
									System.out.println(e);
								}
							}
						}
						vecHistory.setServiceVehicle(serviceVehicle);

					} catch (DataAccessException e) {
						String resonForFailure = "DUPLICATE ENTRY IN " + j + " ROW FOUND";
						System.out.println(resonForFailure);
						FailedRecords.add(resonForFailure);
						continue;
					} catch (Exception e) {
						String resonForFailure = "ERROR IN SAVEING DATA FOR " + j + " ROW " + e.getMessage();
						System.out.println(resonForFailure);
						FailedRecords.add(resonForFailure);
						continue;
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 27))) {

						vecHistory.setServiceAmount(new Float(getCellValueBasedOnCellType(row, 27)));
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 28))) {

						vecHistory.setServiceCenter(getCellValueBasedOnCellType(row, 28));
					}

					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 29))) {

						vecHistory.setKmReading(new Integer(getCellValueBasedOnCellType(row, 29)));
					}

					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 30))) {

						vecHistory.setServiceManager(getCellValueBasedOnCellType(row, 30));
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 31))) {

						vecHistory.setDealerName(getCellValueBasedOnCellType(row, 31));
					}

					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 32))) {

						vecHistory.setDealerLocation(getCellValueBasedOnCellType(row, 32));
					}
					vecHistory.setMdTenant(mdTenant);

					try {
						vehicleServiceHistoryRepository.save(vecHistory);
					} catch (DataAccessException e) {
						String resonForFailure = "DUPLICATE ENTRY IN " + j + " ROW FOUND";
						System.out.println(resonForFailure);
						FailedRecords.add(resonForFailure);
					} catch (Exception e) {
						String resonForFailure = "ERROR IN SAVEING DATA FOR " + j + " ROW " + e.getMessage();
						System.out.println(resonForFailure);
						FailedRecords.add(resonForFailure);
					}

					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 33))) {
						vehicleInsurance.setCustomer(customer);
						vehicleInsurance.setServiceVehicle(serviceVehicle);
						vehicleInsurance.setProvider(getCellValueBasedOnCellType(row, 33));
					}

					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 34))) {
						vehicleInsurance.setStartDate(LocalDate.parse(getCellValueBasedOnCellType(row, 34), formatter));
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 35))) {
						vehicleInsurance.setEndDate(LocalDate.parse(getCellValueBasedOnCellType(row, 35), formatter));
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 36))) {
						vehicleInsurance.setInsuranceAmount(new Double(getCellValueBasedOnCellType(row, 36)));
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 37))) {
						vehicleInsurance.setInsuranceIdentifier(inputFilePath);
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 38))) {
						vehicleWarranty.setServiceVehicle(serviceVehicle);
						vehicleWarranty.setOemPeriod(getCellValueBasedOnCellType(row, 38));
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 39))) {
						vehicleWarranty.setWarrantyTpe(WarrantyType.OEM);
						vehicleWarranty.setStartDate(LocalDate.parse(getCellValueBasedOnCellType(row, 39), formatter));
					}
					if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 40))) {
						// vehicleWarranty.setWarrantyTpe(WarrantyType.OEM);
						vehicleWarranty.setExpiryDate(LocalDate.parse(getCellValueBasedOnCellType(row, 40), formatter));
					} else if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 41))) {
						vehicleWarranty.setWarrantyTpe(WarrantyType.EW);
						vehicleWarranty.setEwName(getCellValueBasedOnCellType(row, 41));
						if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 42))) {
							vehicleWarranty
									.setStartDate(LocalDate.parse(getCellValueBasedOnCellType(row, 42), formatter));
						}
						if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 43))) {
							vehicleWarranty
									.setExpiryDate(LocalDate.parse(getCellValueBasedOnCellType(row, 43), formatter));
						}
					} else if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 44))) {
						vehicleWarranty.setWarrantyTpe(WarrantyType.MCP);
						vehicleWarranty.setStartDate(LocalDate.parse(getCellValueBasedOnCellType(row, 43), formatter));
						if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 45))) {
							vehicleWarranty
									.setExpiryDate(LocalDate.parse(getCellValueBasedOnCellType(row, 43), formatter));
						}
						if (StringUtils.isNotBlank(getCellValueBasedOnCellType(row, 44))) {
							vehicleWarranty.setAmc_name(getCellValueBasedOnCellType(row, 44));
						}
					}
					try {
						vehicleInsuranceRepository.save(vehicleInsurance);
					} catch (DataAccessException e) {
						String resonForFailure = "DUPLICATE ENTRY IN " + j + " ROW FOUND";
						System.out.println(resonForFailure);
						FailedRecords.add(resonForFailure);
					} catch (Exception e) {
						String resonForFailure = "ERROR IN SAVEING DATA FOR " + j + " ROW " + e.getMessage();
						System.out.println(resonForFailure);
						FailedRecords.add(resonForFailure);
					}
					SuccessCount++;
				}
			} catch (Exception e) {
				String resonForFailure = e.getMessage();
				System.out.println(resonForFailure);
				FailedRecords.add(resonForFailure);
				continue;
			}
		}
		if (emptyCheck == 0) {
			String resonForFailure = "DATA NOT FOUND";
			System.out.println(resonForFailure);
			FailedRecords.add(resonForFailure);
		}
		FailedCount = TotalCount - SuccessCount;
		res.setFailedCount(FailedCount);
		res.setFailedRecords(FailedRecords);
		res.setSuccessCount(SuccessCount);
		res.setTotalCount(TotalCount);
		return res;
	}

	public static Workbook getWorkBook(File fileName) {
		Workbook workbook = null;
		try {
			String myFileName = fileName.getName();
			String extension = myFileName.substring(myFileName.lastIndexOf("."));
			if (extension.equalsIgnoreCase(".xls")) {
				workbook = new HSSFWorkbook(new FileInputStream(fileName));
			} else if (extension.equalsIgnoreCase(".xlsx")) {
				workbook = new XSSFWorkbook(new FileInputStream(fileName));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return workbook;
	}

	private String getCellValueBasedOnCellType(Row rowData, int columnPosition) {
		String cellValue = null;
		Cell cell = rowData.getCell(columnPosition);
		if (cell != null) {
			if (cell.getCellType() == CellType.STRING) {
				String inputCellValue = cell.getStringCellValue();
				if (inputCellValue.endsWith(".0")) {
					inputCellValue = inputCellValue.substring(0, inputCellValue.length() - 2);
				}
				cellValue = inputCellValue;
			} else if (cell.getCellType() == CellType.NUMERIC) {
				if (DateUtil.isCellDateFormatted(cell)) {
					DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
					Date today = cell.getDateCellValue();
					cellValue = df.format(today);
				} else {
					Integer doubleVal = (int) cell.getNumericCellValue();
					cellValue = Integer.toString(doubleVal);
				}
			}
		}
		return cellValue;
	}
}
