package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdVehicleDocumentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MdVehicleDocumentTypeRepositoryTest extends BaseTest {

    @Autowired
    private MdVehicleDocumentTypeRepository mdVehicleDocumentTypeRepository;

    @Test
    void findByDocumentNameAndLabelAndMdTenant_TenantIdentifier() {
        MdVehicleDocumentType mdVehicleDocumentType = mdVehicleDocumentTypeRepository
                .findByDocumentNameAndLabelAndMdTenant_TenantIdentifier("RC", "RC",
                        "bhrth_Moto_Corp_Khammam");

    }
}