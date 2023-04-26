package com.automate.vehicleservices.reminder;

import com.automate.vehicleservices.entity.enums.JobType;
import com.automate.vehicleservices.event.AbstractJob;
import com.automate.vehicleservices.repository.dtoprojection.ServiceScheduleDTO;
import com.automate.vehicleservices.service.VehicleServiceScheduleService;
import com.automate.vehicleservices.service.dto.PageableResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class VehicleServiceReminderCronJobNew extends AbstractJob<ServiceScheduleDTO> {

    private final VehicleServiceScheduleService vehicleServiceScheduleService;
    private final VehicleCoreReminderEngine vehicleCoreReminderEngine;
    AtomicBoolean schedulerRunning = new AtomicBoolean(false);

    public VehicleServiceReminderCronJobNew(VehicleServiceScheduleService vehicleServiceScheduleService,
                                            VehicleCoreReminderEngine vehicleCoreReminderEngine) {
        this.vehicleServiceScheduleService = vehicleServiceScheduleService;
        this.vehicleCoreReminderEngine = vehicleCoreReminderEngine;
    }

    @Override
//    @Scheduled(cron = "${dms.vehicle-services.reminder-cron-expression}")
    public void invokeJob() {
        init();
    }

    @Override
    public AtomicBoolean schedulerStatusTracker() {
        return schedulerRunning;
    }

    @Override
    protected String getVehicleRegNumber(ServiceScheduleDTO serviceScheduleDTO) {
        return serviceScheduleDTO.getServiceVehicle().getRegNumber();
    }

    @Override
    protected PageableResponse<ServiceScheduleDTO> getPageableResponse(int currentPage, int batchSize) {
        return vehicleServiceScheduleService.fetchVehicleSchedulesPaginated(currentPage, BATCH_SIZE);
    }

    @Override
    protected String getJobName() {
        return JobType.VEHICLE_SERVICE_REMINDER.name();
    }


    @Override
    protected void process(ServiceScheduleDTO serviceScheduleDTO) {
        vehicleCoreReminderEngine.initiateReminderFlowV1(serviceScheduleDTO);
    }
}
