package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.repository.dtoprojection.ServiceItemByGroup;

import java.util.List;

/**
 * @author Chandrashekar V
 */
public interface VehicleServiceTypeRepository {

    List<ServiceItemByGroup> fetchServiceItemsByCategoryGroup(final int tenant, final ServiceGroup serviceGroup);

}
