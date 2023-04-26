package com.automate.vehicleservices.repository;

/**
 * @author Chandrashekar V
 */
public interface MdServiceCriteriaCustomRepository {

    int serviceTypeCriteria(long vehicleAge, double kmReading, int tenant);
}
