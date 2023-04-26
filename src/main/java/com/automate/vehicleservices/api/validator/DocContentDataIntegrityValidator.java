package com.automate.vehicleservices.api.validator;

import com.automate.vehicleservices.api.model.DocContent;
import com.automate.vehicleservices.api.model.ServiceEstimateRequest;
import com.automate.vehicleservices.api.model.builder.ValidationResponseBuilder;
import com.automate.vehicleservices.framework.validation.DataIntegrityValidator;
import com.automate.vehicleservices.framework.validation.ValidationRequest;
import com.automate.vehicleservices.service.dto.ValidationError;
import com.automate.vehicleservices.service.dto.ValidationResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * Data integrity check for Document upload.
 */
public class DocContentDataIntegrityValidator implements DataIntegrityValidator {

    @Override
    public ValidationResponse validate(ValidationRequest validationRequest) {
        ValidationResponseBuilder validationResponseBuilder = ValidationResponseBuilder.aValidationResponse();
        ServiceEstimateRequest serviceEstimateRequest = (ServiceEstimateRequest) validationRequest;
        List<DocContent> docContents = serviceEstimateRequest.getDocuments();

        if (Objects.isNull(docContents))
            return validationResponseBuilder.build();

        docContents.forEach(docContent -> {
            boolean uuidIsBlank = StringUtils.isBlank(docContent.getUuid());
            boolean base64DataIsNull = isBase64DataIsNull(docContent);
            boolean docURLIsBlank = StringUtils.isBlank(docContent.getDocumentURL());

            if (!uuidIsBlank ? (!base64DataIsNull || !docURLIsBlank) : (!base64DataIsNull && !docURLIsBlank))
                validationResponseBuilder.withError(new ValidationError(String.format("Document Name : %s, Only one " +
                                "way of uploading document is allowed: use UUD or Base 64 Data, or DocumentURL",
                        docContent.getDocumentName()), "documents"));

            if (uuidIsBlank && docURLIsBlank && Objects.nonNull(docContent.getData())) {
                if (StringUtils.isBlank(docContent.getData().getBase64()))
                    validationResponseBuilder.withError(new ValidationError(String.format("Document Name : %s," +
                            "When uploading base64 data must not be null:", docContent.getDocumentName()), "documents"
                    ));
                if (StringUtils.isBlank(docContent.getData().getFilename()))
                    validationResponseBuilder.withError(new ValidationError(String.format("Document Name : %s, When " +
                            "uploading filename must be present:", docContent.getDocumentName()), "documents"));
            }
        });

        return validationResponseBuilder.build();

    }

    public boolean isBase64DataIsNull(DocContent docContent) {
        return Objects.isNull(docContent.getData()) ||
                (StringUtils.isBlank(docContent.getData().getFilename())
                        && StringUtils.isBlank(docContent.getData().getBase64()));
    }
}
