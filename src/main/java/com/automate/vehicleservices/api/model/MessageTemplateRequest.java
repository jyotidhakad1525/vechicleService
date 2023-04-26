package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class MessageTemplateRequest {

    private Integer id;

    @NotBlank(message = "Template name is mandatory!")
    private String templateName;

    @NotNull(message = "Template is mandatory!!")
    private String template;

    private ActiveInActiveStatus status;

    @NotNull(message = "Type must be required!")
    private Integer templateType;
}
