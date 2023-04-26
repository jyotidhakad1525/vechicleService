package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.PreDefineMessageTemplate;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageTemplateDTO {
    private Integer id;

    private String templateName;

    private String template;

    private ActiveInActiveStatus status;

    private int templateType;

    public MessageTemplateDTO(PreDefineMessageTemplate preDefineMessageTemplate) {
        if (Objects.isNull(preDefineMessageTemplate))
            return;

        this.id = preDefineMessageTemplate.getId();
        this.templateName = preDefineMessageTemplate.getTemplateName();
        this.template = preDefineMessageTemplate.getTemplate();
        this.status = preDefineMessageTemplate.getStatus();
        this.templateType = preDefineMessageTemplate.getTemplateType();
    }
}
