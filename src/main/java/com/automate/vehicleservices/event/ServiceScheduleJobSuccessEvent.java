package com.automate.vehicleservices.event;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString
public class ServiceScheduleJobSuccessEvent extends VehicleServiceScheduleEvent {

    public ServiceScheduleJobSuccessEvent() {
        super();
    }


    public static final class ServiceScheduleJobSuccessEventBuilder {
        protected String vehicleRegNumber;
        protected int jobId;
        private String eventId;

        private ServiceScheduleJobSuccessEventBuilder() {
        }

        public static ServiceScheduleJobSuccessEventBuilder aServiceScheduleJobSuccessEvent() {
            return new ServiceScheduleJobSuccessEventBuilder();
        }

        public ServiceScheduleJobSuccessEventBuilder withVehicleRegNumber(String vehicleRegNumber) {
            this.vehicleRegNumber = vehicleRegNumber;
            return this;
        }

        public ServiceScheduleJobSuccessEventBuilder withJobId(int jobId) {
            this.jobId = jobId;
            return this;
        }

        public ServiceScheduleJobSuccessEventBuilder withEventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public ServiceScheduleJobSuccessEvent build() {
            ServiceScheduleJobSuccessEvent serviceScheduleJobSuccessEvent = new ServiceScheduleJobSuccessEvent();
            serviceScheduleJobSuccessEvent.setVehicleRegNumber(vehicleRegNumber);
            serviceScheduleJobSuccessEvent.setJobId(jobId);
            if (StringUtils.isNotBlank(this.eventId))
                serviceScheduleJobSuccessEvent.setEventId(eventId);
            return serviceScheduleJobSuccessEvent;
        }
    }
}
