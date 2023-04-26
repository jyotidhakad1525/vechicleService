package com.automate.vehicleservices.event;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ServiceScheduleJobFailureEvent extends VehicleServiceScheduleEvent {
    private String reasonForFailure;

    public ServiceScheduleJobFailureEvent(String reasonForFailure) {
        super();
        this.reasonForFailure = reasonForFailure;
    }


    public static final class ServiceScheduleJobFailureEventBuilder {
        protected String vehicleRegNumber;
        protected int jobId;
        private String reasonForFailure;
        private String eventId;

        private ServiceScheduleJobFailureEventBuilder() {
        }

        public static ServiceScheduleJobFailureEventBuilder aServiceScheduleJobFailureEvent() {
            return new ServiceScheduleJobFailureEventBuilder();
        }

        public ServiceScheduleJobFailureEventBuilder withReasonForFailure(String reasonForFailure) {
            this.reasonForFailure = reasonForFailure;
            return this;
        }

        public ServiceScheduleJobFailureEventBuilder withVehicleRegNumber(String vehicleRegNumber) {
            this.vehicleRegNumber = vehicleRegNumber;
            return this;
        }

        public ServiceScheduleJobFailureEventBuilder withJobId(int jobId) {
            this.jobId = jobId;
            return this;
        }

        public ServiceScheduleJobFailureEventBuilder withEventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public ServiceScheduleJobFailureEvent build() {
            ServiceScheduleJobFailureEvent serviceScheduleJobFailureEvent = new ServiceScheduleJobFailureEvent();
            serviceScheduleJobFailureEvent.setReasonForFailure(reasonForFailure);
            serviceScheduleJobFailureEvent.setVehicleRegNumber(vehicleRegNumber);
            serviceScheduleJobFailureEvent.setJobId(jobId);
            if (StringUtils.isNotBlank(this.eventId))
                serviceScheduleJobFailureEvent.setEventId(eventId);
            return serviceScheduleJobFailureEvent;
        }
    }
}
