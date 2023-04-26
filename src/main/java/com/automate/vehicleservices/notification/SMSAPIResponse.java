package com.automate.vehicleservices.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * SMS integration response object.
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SMSAPIResponse extends NotificationResponse {
    @JsonProperty(value = "count")
    private int count;
    @JsonProperty(value = "totalCount")
    private int totalCount;
    @JsonProperty(value = "statusCode")
    private int statusCode;


}
