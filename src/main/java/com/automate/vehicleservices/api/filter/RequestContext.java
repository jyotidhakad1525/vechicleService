package com.automate.vehicleservices.api.filter;

import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RequestScope
@Component
public class RequestContext {
    private String tenant;
    private int tenantId;
    private String org;
    private String user;
    private int empId;

}
