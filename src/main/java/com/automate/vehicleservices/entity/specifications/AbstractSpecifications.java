package com.automate.vehicleservices.entity.specifications;

import org.springframework.data.jpa.domain.Specification;

import java.text.MessageFormat;

public abstract class AbstractSpecifications {

    public static <T, V> Specification<T> attributeEqualsTo(String attributeName, V value) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(attributeName), value);
        };
    }

    protected <T> Specification<T> containsSpecification(final String queryTerm, final String dataPoint) {
        return (root, query, builder) -> builder.like(root.get(dataPoint), contains(queryTerm));
    }

    protected String contains(String queryTerm) {
        return MessageFormat.format("%{0}%", queryTerm);
    }

    public <T> Specification<T> tenantEquals(final String tenant) {
        return (root, query, builder) -> builder.equal(root.get("mdTenant").get("tenantIdentifier"), tenant);
    }

}
