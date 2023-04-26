package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.framework.api.MdResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MdServiceTypeResponse implements MdResponse {
    private int id;
    private String description;
    private Boolean active;
    private String serviceCode;
    private String serviceName;
    private int categoryId;
    private String categoryName;
    private ServiceGroup categoryGroup;
    private int pmsSequence;
    private List<MdServiceItemResponse> serviceItems;

    public MdServiceTypeResponse(MdServiceType mdServiceType) {
        if (Objects.isNull(mdServiceType))
            return;

        this.id = mdServiceType.getId();
        this.description = mdServiceType.getDescription();
        this.active = mdServiceType.getActive();
        this.serviceCode = mdServiceType.getServiceCode();
        this.serviceName = mdServiceType.getServiceName();
        final var mdServiceCategory = mdServiceType.getMdServiceCategory();
        if (Objects.nonNull(mdServiceCategory)) {
            this.categoryId = mdServiceCategory.getId();
            this.categoryName = mdServiceCategory.getCategoryName();
            this.categoryGroup = mdServiceCategory.getServiceGroup();
        }
        this.pmsSequence = mdServiceType.getServiceSequence();

        final var serviceItems = mdServiceType.getServiceItems();
        if (CollectionUtils.isNotEmpty(serviceItems))
            this.serviceItems = serviceItems.stream().map(MdServiceItemResponse::new).collect(Collectors.toList());

    }
}
