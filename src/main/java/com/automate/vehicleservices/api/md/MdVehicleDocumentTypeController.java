package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.service.MdVehicleDocumentTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = AbstractCRUDController.MD_BASE_URL + "/document-type")
@Tag(name = "Master data - Vehicle Document Type API", description = "Vehicle Document Type API")
@RestController
@Slf4j
public class MdVehicleDocumentTypeController extends AbstractCRUDController {

    private final MdVehicleDocumentTypeService mdVehicleDocumentTypeService;

    public MdVehicleDocumentTypeController(MdVehicleDocumentTypeService mdVehicleDocumentTypeService) {
        this.mdVehicleDocumentTypeService = mdVehicleDocumentTypeService;
    }

    @Override
    public MasterDataService getService() {
        return mdVehicleDocumentTypeService;
    }

}
