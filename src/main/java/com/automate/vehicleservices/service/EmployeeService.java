package com.automate.vehicleservices.service;

import com.automate.vehicleservices.entity.Employee;
import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.repository.EmployeeRepository;
import com.automate.vehicleservices.service.dto.EmployeeDTO;
import com.automate.vehicleservices.service.dto.HRMSEmployee;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Map<String, List<EmployeeDTO>> listAllEmployeesByTenant(final int tenant) {
        final var employees = employeeRepository.findByActiveTrueAndTenant_Id(tenant);
        if (CollectionUtils.isEmpty(employees))
            return Collections.emptyMap();

        return employees.stream()
                .filter(employee -> StringUtils.isNotBlank(employee.getRole())).map(EmployeeDTO::new)
                .collect(Collectors.groupingBy(EmployeeDTO::getRole, Collectors.toList()));

    }

    private List<EmployeeDTO> getEmployeeDTOS(List<Employee> employees) {
        if (CollectionUtils.isEmpty(employees))
            return Collections.emptyList();

        return employees.stream().map(EmployeeDTO::new).collect(Collectors.toList());
    }

    public List<EmployeeDTO> findEmployeesByMasterIdentifier(List<Integer> masterIdentifiers) {
        final var employees = employeeRepository.findByActiveTrueAndMasterIdentifierIn(masterIdentifiers);
        return getEmployeeDTOS(employees);
    }

    public EmployeeDTO findEmployeesByMasterIdentifier(int masterIdentifier) {
        final var employee = employeeRepository.findByActiveTrueAndMasterIdentifier(masterIdentifier);
        return new EmployeeDTO(employee);
    }
    @Transactional
    public List<HRMSEmployee> getAllCreBasedOnOrg(MdOrganization organization) {
        List<Employee> employeeList = employeeRepository.getCreBasedOnOrgId(organization.getId());
        return employeeList.stream().map(data -> new HRMSEmployee(data)).collect(Collectors.toList());
    }
}
