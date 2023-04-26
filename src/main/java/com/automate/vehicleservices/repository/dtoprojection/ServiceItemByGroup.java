package com.automate.vehicleservices.repository.dtoprojection;

/**
 * @author Chandrashekar V
 */
public class ServiceItemByGroup {

    private String itemName;
    private int serviceTypeId;
    private int serviceItemId;

    public ServiceItemByGroup(String itemName, int serviceItemId, int serviceTypeId) {
        this.itemName = itemName;
        this.serviceTypeId = serviceTypeId;
        this.serviceItemId = serviceItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public int getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(int serviceItemId) {
        this.serviceItemId = serviceItemId;
    }
}
