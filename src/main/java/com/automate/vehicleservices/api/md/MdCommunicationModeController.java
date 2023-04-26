package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.service.MdCommunicationModeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = AbstractCRUDController.MD_BASE_URL + "/communication-mode")
@Tag(name = "Master data - Communication Mode API", description = "Communication Mode API")
@RestController
@Slf4j
public class MdCommunicationModeController extends AbstractCRUDController {

    private final MdCommunicationModeService mdCommunicationModeService;

    public MdCommunicationModeController(MdCommunicationModeService mdCommunicationModeService) {
        this.mdCommunicationModeService = mdCommunicationModeService;
    }

    @Override
    public MasterDataService getService() {
        return mdCommunicationModeService;
    }

}
