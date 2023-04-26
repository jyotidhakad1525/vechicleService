package com.automate.vehicleservices.entity.specifications;

import com.automate.vehicleservices.entity.CustomerTenant;
import com.automate.vehicleservices.service.PhoneNumberUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CustomerSpecifications extends AbstractSpecifications {

    private final PhoneNumberUtil phoneNumberUtil;

    public CustomerSpecifications(PhoneNumberUtil phoneNumberUtil) {
        this.phoneNumberUtil = phoneNumberUtil;
    }

    public Specification<CustomerTenant> firstNameContains(String queryTerm) {
        return (root, query, builder) -> builder.like(root.get("customer").get("firstName"), contains(queryTerm));
    }

    public Specification<CustomerTenant> lastNameContains(String queryTerm) {
        return (root, query, builder) -> builder.like(root.get("customer").get("lastName"), contains(queryTerm));
    }

    public Specification<CustomerTenant> idEquals(int queryTerm) {
        return (root, query, builder) -> builder.equal(root.get("customer").get("id"), queryTerm);
    }

    public Specification<CustomerTenant> contactNumberContains(String queryTerm) {
        queryTerm = phoneNumberUtil.cleansePhoneNumber(queryTerm);
        String finalQueryTerm = queryTerm;
        return (root, query, builder) -> builder
                .like(root.get("customer").get("contactNumber"), contains(finalQueryTerm));
    }

    public Specification<CustomerTenant> altContactNumberContains(String queryTerm) {
        queryTerm = phoneNumberUtil.cleansePhoneNumber(queryTerm);
        String finalQueryTerm = queryTerm;
        return (root, query, builder) -> builder
                .like(root.get("customer").get("altContactNumber"), contains(finalQueryTerm));
    }
}
