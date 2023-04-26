package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.service.MdServiceRateCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = AbstractCRUDController.MD_BASE_URL + "/service-rate-card")
@Tag(name = "Master data - Service Rate Card API", description = "Service Rate Card Master Data APIs")
@RestController
@Slf4j
public class MdServiceRateCardController extends AbstractCRUDController {
    private final MdServiceRateCardService mdServiceRateCardService;

    public MdServiceRateCardController(MdServiceRateCardService mdServiceRateCardService) {
        this.mdServiceRateCardService = mdServiceRateCardService;
    }

    @Operation(tags = {"Master data - Service Category API"},
            parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> addNewServiceRateCard(
            @Validated @RequestBody MdServiceRateCardRequest mdRequest) {
        return responseEntity(getService().save(mdRequest, getTenantId()));

    }

    @Override
    public MasterDataService getService() {
        return mdServiceRateCardService;
    }
}
