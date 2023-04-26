package com.automate.vehicleservices.service;

import com.automate.vehicleservices.outbound.OutboundCaller;
import com.automate.vehicleservices.service.dto.HRMSEmployee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HRMSIntegrationService {

    protected final OutboundCaller outboundCaller;
    @Value("${dms.vehicle-services.hrms.employee.hierarchy}")
    private String hrmsEmployeeHierarchyURL;

    public HRMSIntegrationService(OutboundCaller outboundCaller) {
        this.outboundCaller = outboundCaller;
    }

    public List<Integer> fetchAllEmployeesByManager(int loggedInEmpId) {
        final String url = hrmsEmployeeHierarchyURL + loggedInEmpId;
        HRMSEmployee[] hrmsEmployees = null;
        try {
            hrmsEmployees = (HRMSEmployee[]) outboundCaller
                    .get(url, null, HRMSEmployee[].class);

        } catch (Exception ex) {
            log.error("Exception while fetching employee details from HRMS", ex);
        }
        if (null == hrmsEmployees || hrmsEmployees.length == 0)
            return Collections.emptyList();

        return Arrays.asList(hrmsEmployees).stream().map(HRMSEmployee::getEmpId).collect(Collectors.toList());
    }


}
