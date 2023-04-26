package com.automate.vehicleservices.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowUpStats {

    // Type of Call -Fresh Call, EndDate - today, Status - Open
    private long totalFreshCallsForToady;

    // Type of Call -Fresh Call, start - greater than today, Status - Open
    private long futureFreshCalls;

    // Type of Call -Follow Up Call, EndDate - Greater than today, Status - Open
    private long futureFollowUpCallsAsOfToday;

    // Type of Call -Follow Up Call, EndDate - today, Status - Open
    private long followUpsToBeClosedByToday;

    // Type of Call -Follow up, EndDate - Less than or equal today, Status - Open
    private long pendingFollowUpCallsAsOfToday;

    // Type of Call -Fresh Call, EndDate - today, Status - Closed, last modified - Today
    private long freshCallsMadeToday;

    // Type of Call -FollowUp Call, Status - Closed, last modified - Today
    private long followUpCallsMadeToday;

    // type of call - fresh call, status - closed, start date> last month start date endDate<last month end date
    private long freshCallsMadeLastMonth;

    // type of call - fresh call, status - open or closed, start date> last month start date endDate<last month end date
    private long totalFreshCallsLastMonth;

    // type of call - follow up call, status - closed, start date> last month start date endDate<last month end date
    private long followUpCallsMadeLastMonth;

    // type of call - follow up call, start date> last month start date endDate<last month end date
    private long totalFollowUpCallsLastMonth;

    // type of call - all calls, end date <= today
    private long allCallsPendingAsOfToday;
}
