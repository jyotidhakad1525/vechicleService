package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.VehicleServiceHistory;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class VehicleServiceHistoryRepositoryTest extends BaseTest {

    public static final String PERIODIC_MAINTENANCE_SERVICE = "Periodic Maintenance Service";
    public static final String PERIODIC_MAINTENANCE_CHECK = "Periodic Maintenance Check";
    @Autowired
    private VehicleServiceHistoryRepository vehicleServiceHistoryRepository;

    @Test
    void testFindByServiceVehicle_RegNumberAndMdServiceType_MdTenant_TenantIdentifierAndMdServiceType_MdServiceCategory_CategoryNameInOrderByServiceDateDesc() {
        List<VehicleServiceHistory> vehicleServiceHistoryList = vehicleServiceHistoryRepository
                .findByServiceVehicle_RegNumberAndMdServiceType_MdTenant_TenantIdentifierAndMdServiceType_MdServiceCategory_ServiceGroupOrderByServiceDateDesc(
                        "DL10RT6765", "bhrth_Moto_Corp_Kothagudem",
                        ServiceGroup.REGULAR_MAINTENANCE);

        vehicleServiceHistoryList.forEach(history -> System.out.println("History: " + history.toString()));
    }
}