package com.automate.vehicleservices.schedule;

import com.automate.vehicleservices.entity.enums.JobType;
import com.automate.vehicleservices.event.AbstractJob;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import com.automate.vehicleservices.schedule.v1.SchedulingCoreEngine;
import com.automate.vehicleservices.service.dto.PageableResponse;
import com.automate.vehicleservices.service.facade.VehicleFacade;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class VehicleServiceScheduleCronJobNew extends AbstractJob<ServiceVehicleDTO> {

    private final VehicleFacade vehicleFacade;
    private final SchedulingCoreEngine vehicleCoreScheduleEngine;
    AtomicBoolean schedulerRunning = new AtomicBoolean(false);

    public VehicleServiceScheduleCronJobNew(VehicleFacade vehicleFacade,
                                            SchedulingCoreEngine vehicleCoreScheduleEngine) {
        this.vehicleFacade = vehicleFacade;
        this.vehicleCoreScheduleEngine = vehicleCoreScheduleEngine;
    }

    @Override
//    @Scheduled(cron = "${dms.vehicle-services.schedule-cron-expression}")
    public void invokeJob() {
        init();
    }

    @Override
    public AtomicBoolean schedulerStatusTracker() {
        return schedulerRunning;
    }

    @Override
    protected String getVehicleRegNumber(ServiceVehicleDTO serviceVehicleDTO) {
        return serviceVehicleDTO.getRegNumber();
    }

    @Override
    @Transactional
    protected PageableResponse<ServiceVehicleDTO> getPageableResponse(int currentPage, int batchSize) {
        return vehicleFacade.fetchUnscheduledVehiclesPaginated(currentPage, BATCH_SIZE);
    }

    @Override
    protected String getJobName() {
        return JobType.VEHICLE_SERVICE_SCHEDULE.name();
    }


    @Override
    protected void process(ServiceVehicleDTO serviceVehicleDTO) {
        vehicleCoreScheduleEngine.schedule(serviceVehicleDTO);
    }
}
