package com.automate.vehicleservices.service.rsa;

import com.automate.vehicleservices.api.model.rsa.RSAAddressRequest;
import com.automate.vehicleservices.api.model.rsa.RSARequest;
import com.automate.vehicleservices.entity.Employee;
import com.automate.vehicleservices.entity.RSA;
import com.automate.vehicleservices.entity.RSAAddress;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.builder.RSABuilder;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.service.CustomerService;
import com.automate.vehicleservices.service.dto.EmployeeDTO;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
import com.automate.vehicleservices.service.dto.rsa.RsaDto;
import com.automate.vehicleservices.service.dto.TechnicianDetailsDto;
import com.automate.vehicleservices.repository.RsaRepository;
import com.automate.vehicleservices.repository.EmployeeRepository;
import com.automate.vehicleservices.repository.ServiceVehicleRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

import static com.automate.vehicleservices.common.VehicleServiceConstants.EMPTY_STRING;
import static com.automate.vehicleservices.common.VehicleServiceConstants.SEAR_PARAM_LENGTH;
import static com.automate.vehicleservices.common.VehicleServiceConstants.TECHNICIAN_BY_ROLE;

@Service
public class RsaService {

    private final RsaRepository rsaRepository;

    private final ModelMapper modelMapper;

    private final RSAAddressService rsaAddressService;
    private final CustomerService customerService;
    private final EmployeeRepository employeeRepository;
    private final ServiceVehicleRepository serviceVehicleRepository;

    public RsaService(RsaRepository rsaRepository, ModelMapper modelMapper, CustomerService customerService,
                      RSAAddressService rsaAddressService, EmployeeRepository employeeRepository, ServiceVehicleRepository serviceVehicleRepository) {
        this.rsaRepository = rsaRepository;
        this.modelMapper = modelMapper;
        this.customerService = customerService;
        this.rsaAddressService = rsaAddressService;
        this.employeeRepository = employeeRepository;
        this.serviceVehicleRepository = serviceVehicleRepository;
    }

    @Transactional
    public RsaDto saveRSA(RSARequest rsaRequest) {
        Optional<Employee> optional = employeeRepository.findById(rsaRequest.getTechnician());
        Employee employee = null;
        if(optional.isPresent()){
            employee = optional.get();
        }

        RSA rsa = convertToEntity(rsaRequest, employee);
        rsa = rsaRepository.save(rsa);
        return convertFromEntityToDTO(rsa);
    }

    private RSA convertToEntity(RSARequest rsaRequest, Employee employee) {
        final ServiceVehicle serviceVehicle = serviceVehicleRepository.findByRegNumber(rsaRequest.getVehicleRegNo());
        final RSAAddress RSAAddressEntity = rsaAddressService.createRSAAddressEntity(rsaRequest
                .getRsaAddressRequest());
        final RSA rsa = RSABuilder.aRSA()
                .withAmount(rsaRequest.getAmount())
                .withCustomerId(rsaRequest.getCustomerId())
                .withDate(rsaRequest.getDate())
                .withReason(rsaRequest.getReason())
                .withRemarks(rsaRequest.getRemarks())
                .withStatus(rsaRequest.getStatus())
                .withRsaAddress(RSAAddressEntity)
                .withBranchName(rsaRequest.getBranchName())
               .withServiceVehicle(serviceVehicle)
                .build();
        if(employee != null)
            rsa.setTechnician(employee);
        RSAAddressEntity.setRSA(rsa);
        return rsa;
    }

    private RsaDto convertFromEntityToDTO(RSA rsa) {
        return new RsaDto(rsa);
    }

   
    @Transactional
    public PaginatedSearchResponse<RsaDto> getAllRsa(Integer page, Integer size, Integer customerid, String status) {
        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        System.out.println("<<<<<<<<<<<<<<<<<<<<<In GetAll Rsa>>>>>>>>>>>>>>>>>>>>>>>>>>>"+customerid);
    	Page<RSA> rsaList = null;
    	
        if (Objects.nonNull(customerid) && Objects.nonNull(status)) {
        	System.out.println("<<<<<<<<<<<<<<<<<<<<<FirstIF>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            rsaList = rsaRepository.findAllByStatusAndCustomerId(status, customerid, paging);
        } else if (Objects.isNull(customerid) && Objects.nonNull(status)) {
        	System.out.println("<<<<<<<<<<<<<<<<<<<<<ELSEIF 1>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            rsaList = rsaRepository.findAllByStatus(status, paging);
        } else if (Objects.isNull(customerid) && Objects.isNull(status)) {
        	System.out.println("<<<<<<<<<<<<<<<<<<<<<ELSEIF 2>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            rsaList = rsaRepository.findAll(paging);
        } else {
        	System.out.println("<<<<<<<<<<<<<<<<<<<<<ELSE>>>>>>>>>>>>>>>>>>>>>>>>>>>"+paging);
        	
        	
            rsaList = rsaRepository.findAllByCustomerId(customerid, paging);
        }
        
        final List<RsaDto> rsaDtoList = rsaList.stream().map(this::convertFromEntityToDTO).collect(Collectors.toList());
        System.out.println("<<<<<<<<<<<<<<<<<<<<<rsaDtoList>>>>>>>>>>>>>>>>>>>>>>>>>>>"+rsaDtoList);
        return new PaginatedSearchResponse<>(rsaList, rsaDtoList);
    }
    
    
    

    @Transactional
    public List<RsaDto> fetchRSAByCustomerId(int customerId) {
        final List<RSA> rsaList = rsaRepository.findAllByCustomerId(customerId);
        if (CollectionUtils.isEmpty(rsaList))
            return Collections.emptyList();

        return rsaList.stream().map(RsaDto::new).collect(Collectors.toList());
    }

    @Transactional
    public RsaDto fetchRSAById(int rsaID) {
        final var rsa = getRsa(rsaID);
        return new RsaDto(rsa);
    }

    private RSA getRsa(int rsaID) {
        final Optional<RSA> optionalRSA = rsaRepository.findById(rsaID);
        return optionalRSA.orElseThrow(() -> new VehicleServicesException(String.format("RSA does not exists" +
                " with id %d", rsaID)));
    }

    @Transactional
    public void deleteRsaById(Integer id) {
        final boolean exists = rsaRepository.existsById(id);
        if (!exists)
            throw new VehicleServicesException(String.format("RSA does not exists with id %d", id));

        rsaRepository.deleteById(id);
    }

    @Transactional
    public RsaDto updateRSA(final int rsaId, RSARequest rsaRequest) {
        RSA rsa = getRsa(rsaId);
        rsa.setAmount(rsaRequest.getAmount());
        rsa.setCustomerId(rsaRequest.getCustomerId());
        rsa.setDate(rsaRequest.getDate());
        rsa.setReason(rsaRequest.getReason());
        rsa.setRemarks(rsaRequest.getRemarks());
        rsa.setStatus(rsaRequest.getStatus());
        RSAAddress rsaAddress = rsa.getRsaAddress();

        final RSAAddressRequest rsaAddressRequest = rsaRequest.getRsaAddressRequest();
        if (Objects.isNull(rsaAddress)) {
            final RSAAddress rsaAddressEntity = rsaAddressService.createRSAAddressEntity(rsaAddressRequest);
            rsa.setRsaAddress(rsaAddressEntity);
            rsaAddressEntity.setRSA(rsa);
        } else {
            rsaAddress.setAddress(rsaAddressRequest.getAddress());
            rsaAddress.setArea(rsaAddressRequest.getArea());
            rsaAddress.setLandmark(rsaAddressRequest.getLandmark());
            rsaAddress.setLatitude(rsaAddressRequest.getLatitude());
            rsaAddress.setLongitude(rsaAddressRequest.getLongitude());
            rsaAddress.setPin(rsaAddressRequest.getPin());
        }
        final RSA save = rsaRepository.save(rsa);
        return new RsaDto(save);
    }

    public PaginatedSearchResponse<RsaDto> getRSAList(Integer page, Integer size, String sortBy, String searchBy){
        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        String searchParam = StringUtils.isNotEmpty(searchBy) && searchBy.length() >= SEAR_PARAM_LENGTH? searchBy : EMPTY_STRING;
        Page<RSA> rsaList = rsaRepository.getRSAList(searchParam,paging);
        final List<RsaDto> rsaDtoList = rsaList.stream().map(this::convertFromEntityToDTO).collect(Collectors.toList());
        System.out.println("rsaDtoList for all customers:"+rsaDtoList);
        return new PaginatedSearchResponse<>(rsaList, rsaDtoList);
    }

    public TechnicianDetailsDto getTechnicianDetailsDto(){
        List<Employee> employees = employeeRepository.findByRoleIgnoreCase(TECHNICIAN_BY_ROLE);
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(employees)){
            for(Employee e : employees){
                employeeDTOS.add(buildEmployee(e));
            }
        }
        return TechnicianDetailsDto.builder()
                .employeeDTOS(employeeDTOS)
                .build();

    }

    private EmployeeDTO buildEmployee(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setActive(employee.getActive());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setEmpId(employee.getId());

        employeeDTO.setId(employee.getId());
        employeeDTO.setMasterIdentifier(employee.getMasterIdentifier());
        employeeDTO.setMobileNo(employee.getMobileNo());
        employeeDTO.setTenantId(employee.getTenant().getId());
        employeeDTO.setRole(employee.getRole());
        return employeeDTO;
    }
}