package com.automate.vehicleservices.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NotificationResponse {
    @JsonProperty(value = "confirmationId")
    private String confirmationId;
    @JsonProperty(value = "status")
    private String status;
}
