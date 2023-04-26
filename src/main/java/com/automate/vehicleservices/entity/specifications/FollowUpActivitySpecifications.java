package com.automate.vehicleservices.entity.specifications;

import com.automate.vehicleservices.entity.ServiceReminderFollowUpActivity;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.enums.FollowUpActivityResult;
import com.automate.vehicleservices.entity.enums.FollowUpActivityStatus;
import com.automate.vehicleservices.entity.enums.FollowUpReason;
import com.automate.vehicleservices.entity.enums.FollowUpStepStatus;
import com.automate.vehicleservices.entity.enums.ScheduleStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class FollowUpActivitySpecifications extends AbstractSpecifications {

    public Specification<ServiceVehicle> vehicleRegNumberContains(String queryTerm) {
        return containsSpecification(queryTerm, "regNumber");
    }

    public Specification<ServiceReminderFollowUpActivity> reasonEquals(FollowUpReason followUpReason) {
        return (root, query, builder) -> builder.equal(root.get("followUpReason"), followUpReason);
    }

    public Specification<ServiceReminderFollowUpActivity> followUpStatusEquals(FollowUpStepStatus followUpStatus) {
        return (root, query, builder) -> builder.equal(root.get("followUpStatus"), followUpStatus);
    }
    public Specification<ServiceReminderFollowUpActivity> resultEquals(FollowUpActivityResult followUpResult) {
        return (root, query, builder) -> builder.equal(root.get("followUpActivityResult"), followUpResult);
    }

    public Specification<ServiceReminderFollowUpActivity> checkedVehicleScheduleStatus(ScheduleStatus status) {
        return (root, query, builder) -> builder.equal(root.get("serviceReminderFollowUp").get(
                "serviceReminderDetails").get("serviceReminder").get("vehicleServiceSchedule").get("status"), status);
    }

    public Specification<ServiceReminderFollowUpActivity> categoryId(Integer categoryId) {
        return (root, query, builder) -> builder.equal(root.get("serviceReminderFollowUp").get(
                "serviceReminderDetails").get("serviceReminder").get("mdServiceType").get("mdServiceCategory").get("id"), categoryId);
    }

    public Specification<ServiceReminderFollowUpActivity> locationEqual(final String location) {
        return (root, query, builder) -> builder.equal(root.get("creLocation"), location);
    }

    public Specification<ServiceReminderFollowUpActivity> serviceCenterCodeEqual(final String centerCode) {
        return (root, query, builder) -> builder.equal(root.get("serviceCenterCode"), centerCode);
    }

    public Specification<ServiceReminderFollowUpActivity> reasonNotEquals(FollowUpReason followUpReason) {
        return (root, query, builder) -> builder.notEqual(root.get("followUpReason"), followUpReason);
    }

    public Specification<ServiceReminderFollowUpActivity> openStatus() {
        return (root, query, builder) -> builder.equal(root.get("followUpActivityStatus"), FollowUpActivityStatus.OPEN);
    }

    public Specification<ServiceReminderFollowUpActivity> vehicleModel(final String model) {
        return (root, query, builder) -> builder.equal(root.get("serviceReminderFollowUp").get("serviceVehicle").get(
                "model"), model);

    }

    public Specification<ServiceReminderFollowUpActivity> serviceTypeIdEquals(final int serviceTypeId) {
        return (root, query, builder) -> builder.equal(root.get("serviceReminderFollowUp").get(
                "serviceReminderDetails").get("serviceReminder").get("mdServiceType").get("id"), serviceTypeId);
    }

    public Specification<ServiceReminderFollowUpActivity> closedStatus() {
        return (root, query, builder) -> builder.equal(root.get("followUpActivityStatus"),
                FollowUpActivityStatus.CLOSED);
    }

    public Specification<ServiceReminderFollowUpActivity> creIDsIn(List<Integer> inHouseEmployeeIds) {
        return (root, query, builder) -> builder.in(root.get("cre").get("id")).value(inHouseEmployeeIds);

    }

    public Specification<ServiceReminderFollowUpActivity> followUpStatusIsNull() {
        return (root, query, builder) -> builder.isNull(root.get("followUpStatus"));
    }

    public Specification<ServiceReminderFollowUpActivity> endDateGreaterThan(LocalDate date) {
        return (root, query, builder) -> builder.greaterThan(root.get("endDate"), date);
    }

    public Specification<ServiceReminderFollowUpActivity> startDateGreaterThan(LocalDate date) {
        return (root, query, builder) -> builder.greaterThan(root.get("startDate"), date);
    }

    public Specification<ServiceReminderFollowUpActivity> startDateGreaterThanEqual(LocalDate date) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("startDate"), date);
    }

    public Specification<ServiceReminderFollowUpActivity> endDateLessThanEqual(LocalDate date) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("endDate"), date);
    }

    public Specification<ServiceReminderFollowUpActivity> endDateEquals(LocalDate date) {
        return (root, query, builder) -> builder.equal(root.get("endDate"), date);
    }

    public Specification<ServiceReminderFollowUpActivity> endDateBetween(LocalDate from, LocalDate to) {
        return (root, query, builder) -> builder.between(root.get("endDate"), from, to);
    }

    public Specification<ServiceReminderFollowUpActivity> lastModifiedDateEquals(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return (root, query, builder) -> builder.between(root.get("lastModifiedDate"), startOfDay, endOfDay);
    }

    public Specification<ServiceReminderFollowUpActivity> lastModifiedDateGreaterThanOrEquals(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();

        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("lastModifiedDate"), startOfDay);
    }

    public Specification<ServiceReminderFollowUpActivity> lastModifiedDateLessThanOrEquals(LocalDate date) {
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("lastModifiedDate"), endOfDay);
    }
}
