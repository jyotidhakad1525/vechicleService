package com.automate.vehicleservices.framework;

import com.automate.vehicleservices.framework.api.MdRequest;
import com.automate.vehicleservices.framework.api.MdResponse;

import java.util.List;

public interface MasterDataService {
    MdResponse save(MdRequest mdRequest, int tenantId);

    //default MdResponse update(MdRequest mdRequest, int tenantId, int id);

    boolean delete(int id, int tenantId);

    List<? extends MdResponse> all(int tenantId);

    MdResponse findById(int id, int tenantId);
}
