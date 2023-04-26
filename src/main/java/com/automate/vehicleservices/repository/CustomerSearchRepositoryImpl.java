package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.api.model.CustomerSearchRequest;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
import com.automate.vehicleservices.service.dto.SearchResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional
@Slf4j
public class CustomerSearchRepositoryImpl implements CustomerSearchRepository {

    private final EntityManager entityManager;

    public CustomerSearchRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public PaginatedSearchResponse<SearchResponseDTO> search(CustomerSearchRequest customerSearchRequest, int page,
                                                             int size) {

        String countQueryString = "   SELECT count(*) FROM   " +
                "                  service_vehicle v  " +
                "                  LEFT JOIN customer c ON c.id = v.CUSTOMER_ID  " +
                "                  LEFT JOIN vehicle_insurance vi ON vi.VEHICLE_ID = v.ID ";


        String query = "SELECT " +
                " vi.INSURANCE_ID policyNumber, " +
                " v.MODEL model, " +
                " v.ENGINE_NUMBER engineNumber, " +
                " v.CHASSIS_NUMBER chassisNumber, " +
                " v.REG_NUMBER vehicleRegNumber, " +
                " v.VIN vin, " +
                " c.ID customerId, " +
                " c.FIRST_NAME firstName, " +
                " c.LAST_NAME lastName, " +
                " c.CONTACT_NUMBER contactNumber " +
                "FROM " +
                " service_vehicle v " +
                " LEFT JOIN customer c ON c.id = v.CUSTOMER_ID " +
                " LEFT JOIN vehicle_insurance vi ON vi.VEHICLE_ID = v.ID";

        QueryBuilder queryBuilder = new QueryBuilder()
                .add(customerSearchRequest.getContactNumber(), "c.CONTACT_NUMBER", Operator.LIKE)
                .add(customerSearchRequest.getPolicyNumber(), "vi.INSURANCE_ID", Operator.LIKE)
                .add(customerSearchRequest.getCustomerId(), "c.ID", Operator.EQUALS)
                .add(customerSearchRequest.getChassis(), "v.CHASSIS_NUMBER", Operator.LIKE)
                .add(customerSearchRequest.getVin(), "v.VIN", Operator.LIKE)
                .add(customerSearchRequest.getEngineNumber(), "v.ENGINE_NUMBER", Operator.LIKE)
                .add(customerSearchRequest.getVehicleRegNumber(), "v.REG_NUMBER", Operator.LIKE)
                .add(QueryBuilder.CompositeCondition.builder()
                        .condition1(QueryBuilder.Condition.builder().key("c" +
                                ".FIRST_NAME").operator(Operator.LIKE).value(customerSearchRequest.getCustomerName())
                                .build())
                        .condition2(QueryBuilder.Condition.builder().key("c" +
                                ".LAST_NAME").operator(Operator.LIKE).value(customerSearchRequest.getCustomerName())
                                .build())
                        .joiner(Joiner.OR).build());
        final var build = queryBuilder.build();
        final var resultsQuery =
                new StringBuilder(query).append(StringUtils.SPACE).append(build).toString();
        log.info(resultsQuery);

        final var finalCountQuery =
                new StringBuilder(countQueryString).append(StringUtils.SPACE).append(build).toString();
        Query nativeQuery = entityManager.createNativeQuery(resultsQuery, "CustomerSearchResult").setMaxResults(size)
                .setFirstResult(page * size);

        Query countQuery = entityManager.createNativeQuery(finalCountQuery);
        final var singleResult = countQuery.getSingleResult();
        int count = 0;
        if (Objects.nonNull(singleResult)) {
            count = ((BigInteger) singleResult).intValue();
        }
        final var resultList = nativeQuery.getResultList();
        final var paginatedSearchResponse = new PaginatedSearchResponse(resultList, page, size, count);
        return paginatedSearchResponse;
    }

    private enum Operator {
        LIKE(" like "),
        EQUALS("=");

        String value;

        Operator(String value) {
            this.value = value;
        }
    }

    private enum Joiner {
        AND(" and "),
        OR(" or ");

        String value;

        Joiner(String value) {
            this.value = value;
        }
    }

    private static class QueryBuilder {

        StringBuilder stringBuilder;
        List<Condition> conditions = new ArrayList<>();
        List<CompositeCondition> compositeConditions = new ArrayList<>();

        QueryBuilder() {
            stringBuilder = new StringBuilder();
        }

        QueryBuilder add(Object value, String key, Operator operator) {
            if (isValidValue(value))
                conditions.add(new Condition(value, key, operator));
            return this;
        }

        private boolean isValidValue(Object value) {
            if (Objects.isNull(value))
                return false;
            if (value instanceof String)
                return isValidValue((String) value);
            if (value instanceof Integer)
                return isValidValue((Integer) value);

            return Objects.nonNull(value);
        }

        private boolean isValidValue(String value) {
            return StringUtils.isNotBlank(value);
        }

        private boolean isValidValue(Integer value) {
            return value > 0;
        }

        QueryBuilder add(Condition condition) {
            if (isValidValue(condition.value))
                conditions.add(condition);
            return this;
        }

        QueryBuilder add(CompositeCondition condition) {
            if (isValidValue(condition.condition2.value) && isValidValue(condition.condition1.value))
                compositeConditions.add(condition);
            return this;
        }

        String build() {
            if (conditions.size() > 0 || compositeConditions.size() > 0) {
                stringBuilder.append(StringUtils.SPACE).append(" where ").append(StringUtils.SPACE);

                if (conditions.size() > 0) {
                    for (int i = 0; i < conditions.size(); i++) {
                        appendConditionToQuery(conditions.get(i));
                        if (i < conditions.size() - 1)
                            stringBuilder.append(StringUtils.SPACE).append(Joiner.AND).append(StringUtils.SPACE);
                    }

                }

                if (compositeConditions.size() > 0) {
                    if (conditions.size() == 0)
                        stringBuilder.append(StringUtils.SPACE).append(" where ").append(StringUtils.SPACE);
                    else
                        stringBuilder.append(StringUtils.SPACE).append(" AND ").append(StringUtils.SPACE);
                    for (int i = 0; i < compositeConditions.size(); i++) {
                        stringBuilder.append("(");
                        appendConditionToQuery(compositeConditions.get(i).condition1);
                        stringBuilder.append(compositeConditions.get(i).joiner.value);
                        appendConditionToQuery(compositeConditions.get(i).condition2);
                        stringBuilder.append(")");
                        if (i < compositeConditions.size() - 2)
                            stringBuilder.append(StringUtils.SPACE).append(Joiner.AND).append(StringUtils.SPACE);
                    }

                }

            }
            return stringBuilder.toString();
        }

        private void appendConditionToQuery(Condition condition) {
            if (condition.operator == Operator.LIKE)
                stringBuilder.append(StringUtils.SPACE).append(condition.key)
                        .append(StringUtils.SPACE)
                        .append(condition.operator.value)
                        .append(StringUtils.SPACE).append("'%").append(condition.value).append("%'");
            else
                stringBuilder.append(StringUtils.SPACE).append(condition.key)
                        .append(StringUtils.SPACE)
                        .append(condition.operator.value)
                        .append(StringUtils.SPACE).append(condition.value);
        }

        @AllArgsConstructor
        @Builder
        static class Condition {
            private final Object value;
            private final String key;
            private final Operator operator;

        }


        @AllArgsConstructor
        @Builder
        static class CompositeCondition {
            private final Condition condition1;
            private final Condition condition2;
            private final Joiner joiner;

        }

    }

}
