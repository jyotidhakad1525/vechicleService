package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdServiceItem;
import com.automate.vehicleservices.entity.ServiceAppointment;
import com.automate.vehicleservices.entity.ServiceAppointmentAdditionalService;

public final class ServiceAppointmentAdditionalServiceBuilder {
    //bi-directional many-to-one association to ServiceAppointment
    private ServiceAppointment serviceAppointment;
    //uni-directional many-to-one association to MdServiceItem
    private MdServiceItem mdServiceItem;

    private ServiceAppointmentAdditionalServiceBuilder() {
    }

    public static ServiceAppointmentAdditionalServiceBuilder aServiceAppointmentAdditionalService() {
        return new ServiceAppointmentAdditionalServiceBuilder();
    }

    public ServiceAppointmentAdditionalServiceBuilder withServiceAppointment(ServiceAppointment serviceAppointment) {
        this.serviceAppointment = serviceAppointment;
        return this;
    }

    public ServiceAppointmentAdditionalServiceBuilder withMdServiceItem(MdServiceItem mdServiceItem) {
        this.mdServiceItem = mdServiceItem;
        return this;
    }

    public ServiceAppointmentAdditionalServiceBuilder but() {
        return aServiceAppointmentAdditionalService().withServiceAppointment(serviceAppointment)
                .withMdServiceItem(mdServiceItem);
    }

    public ServiceAppointmentAdditionalService build() {
        ServiceAppointmentAdditionalService serviceAppointmentAdditionalService =
                new ServiceAppointmentAdditionalService();
        serviceAppointmentAdditionalService.setServiceAppointment(serviceAppointment);
        serviceAppointmentAdditionalService.setMdServiceItem(mdServiceItem);
        return serviceAppointmentAdditionalService;
    }
}
