package com.automate.vehicleservices.entity.specifications;

import com.automate.vehicleservices.entity.CustomerQuery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CustomerQuerySpecifications extends AbstractSpecifications {

    public Specification<CustomerQuery> hasCreatedBy(String value) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("createdBy"), value);
        };
    }

    public Specification<CustomerQuery> hasAssignedTo(String value) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("assignedTo"), value);
        };
    }

    public Specification<CustomerQuery> idEqualsTo(int id) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }
}
