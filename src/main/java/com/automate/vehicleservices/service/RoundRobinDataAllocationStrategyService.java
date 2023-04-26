package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.RoundRobinAllocationRequest;
import com.automate.vehicleservices.entity.Employee;
import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.RoundRobinDataAllocationStrategy;
import com.automate.vehicleservices.entity.ServiceTypeBasedAllocationRatio;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.RoundRobinDataAllocationStrategyRepository;
import com.automate.vehicleservices.repository.ServiceTypeBasedAllocationRatioRepository;
import com.automate.vehicleservices.service.dto.HRMSEmployee;
import com.automate.vehicleservices.service.dto.RoundRobinAllocationDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class RoundRobinDataAllocationStrategyService extends AbstractService {

    private final RoundRobinDataAllocationStrategyRepository repository;
    private final ServiceTypeBasedAllocationRatioRepository serviceTypeBasedAllocationRatioRepository;

    private final EmployeeService employeeService;

    @Transactional
    public Object allocationToCre(RoundRobinAllocationRequest request, MdOrganization organization) {
        Date date = Calendar.getInstance().getTime();
        RoundRobinDataAllocationStrategy roundRobinDataAllocationStrategy;
        if (Objects.nonNull(request.getId())) {
            log.info("update call");
            roundRobinDataAllocationStrategy = getRoundRobinDataAllocationStrategy(request.getId(), organization.getId());
            serviceTypeBasedAllocationRatioRepository.deleteAllByRoundRobinDataAllocationStrategyId(roundRobinDataAllocationStrategy.getId());

        } else {
            log.info("add call");
            Optional<Employee> employeeOptional = crudService.findById(request.getEmployeeId(), Employee.class);
            if (!employeeOptional.isPresent())
                throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Employee details not found!");

            Employee employee = employeeOptional.get();
            roundRobinDataAllocationStrategy = new RoundRobinDataAllocationStrategy();
            roundRobinDataAllocationStrategy.setCreatedDatetime(date);
            roundRobinDataAllocationStrategy.setOrganization(organization);
            roundRobinDataAllocationStrategy.setEmployee(employee);
            roundRobinDataAllocationStrategy.setIsNumber(request.isNumber());
        }
        roundRobinDataAllocationStrategy.setStatus(request.getStatus());
        roundRobinDataAllocationStrategy.setUpdatedDatetime(date);

        if (Objects.isNull(request.getAllocationRatioRequest()))
            throw new VehicleServicesException("One  service type data required!");

        final var serviceList = request.getAllocationRatioRequest().stream().map(data -> {
            Optional<MdServiceType> serviceTypeOptional = crudService.findById(data.getServiceTypeId(), MdServiceType.class);
            if (!serviceTypeOptional.isPresent())
                throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Service Type not found!!");
            return new ServiceTypeBasedAllocationRatio(null, serviceTypeOptional.get(), data.getRatio(), roundRobinDataAllocationStrategy);
        }).collect(Collectors.toList());
        roundRobinDataAllocationStrategy.setAllocationRatioList(serviceList);


        final var result = repository.save(roundRobinDataAllocationStrategy);
        return new RoundRobinAllocationDTO(result);
    }

    private RoundRobinDataAllocationStrategy getRoundRobinDataAllocationStrategy(Integer id, Integer orgId) {
        RoundRobinDataAllocationStrategy roundRobinDataAllocationStrategy;
        Optional<RoundRobinDataAllocationStrategy> optional = repository.findByIdAndOrganizationId(id, orgId);
        if (!optional.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Round robin detail not found!");

        roundRobinDataAllocationStrategy = optional.get();
        return roundRobinDataAllocationStrategy;
    }

    @Transactional
    public Object getAllAllocatedData(MdOrganization organization) {
        List<HRMSEmployee> creList = employeeService.getAllCreBasedOnOrg(organization);
        List<RoundRobinDataAllocationStrategy> list = repository.findAllByOrganizationId(organization.getId());
        Map<Integer, RoundRobinDataAllocationStrategy> map = list.stream().collect(Collectors.toMap(s -> s.getEmployee().getId(), s -> s));
        return creList.stream().map(data -> {
            var roundRobinData = map.get(data.getEmpId());
            if (Objects.nonNull(roundRobinData)) {
                return new RoundRobinAllocationDTO(roundRobinData);
            } else {
                return new RoundRobinAllocationDTO(data);
            }
        }).collect(Collectors.toList());
    }

    @Transactional
    public Object getAllocatedDataBasedOnId(Integer id, MdOrganization orgBasedOnTenantId) {
        return new RoundRobinAllocationDTO(getRoundRobinDataAllocationStrategy(id, orgBasedOnTenantId.getId()));
    }

    @Transactional
    public Object updateStatusBasedOnId(ActiveInActiveStatus changeStatus, Integer id, MdOrganization orgBasedOnTenantId) {
        RoundRobinDataAllocationStrategy roundRobinDataAllocationStrategy = getRoundRobinDataAllocationStrategy(id, orgBasedOnTenantId.getId());
        if (Objects.nonNull(changeStatus))
            roundRobinDataAllocationStrategy.setStatus(changeStatus);
        final var result = repository.save(roundRobinDataAllocationStrategy);
        return new RoundRobinAllocationDTO(result);
    }

    @Transactional
    public void updateIsNumberBasedOnOrgId(Boolean isNumber, MdOrganization orgBasedOnTenantId) {
       repository.updateIsNumberValueBasedOnOrgId(isNumber,orgBasedOnTenantId.getId());
    }

    @Transactional
    public List<RoundRobinDataAllocationStrategy> getCREDetailsBasedOnOrgIdAndStatusAndServiceType(Integer orgId,Integer serviceTypeId) {
       return repository.getCREDetailsBasedOnOrgIdAndStatusAndServiceType(orgId,serviceTypeId);
    }
}
