package com.automate.vehicleservices.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class EmailAPIResponse extends NotificationResponse {
    @JsonProperty(value = "statusDescription")
    private String statusDescription;
    @JsonProperty(value = "count")
    private int count;
    @JsonProperty(value = "totalCount")
    private int totalCount;
    @JsonProperty(value = "statusCode")
    private int statusCode;

}
