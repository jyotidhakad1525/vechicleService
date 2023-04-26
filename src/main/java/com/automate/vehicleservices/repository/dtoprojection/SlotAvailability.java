package com.automate.vehicleservices.repository.dtoprojection;

/**
 * @author Chandrashekar V
 */
public interface SlotAvailability {

    int getSlotID();

    String getFromTime();

    String getToTime();

    int getAllocated();

    int getAvailable();

}
