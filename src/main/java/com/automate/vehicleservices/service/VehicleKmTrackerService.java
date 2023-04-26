package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.VehicleKMRequest;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.VehicleKmTracker;
import com.automate.vehicleservices.entity.builder.VehicleKmTrackerBuilder;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.VehicleKmTrackerRepository;
import com.automate.vehicleservices.repository.dtoprojection.VehicleKmTrackerDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class VehicleKmTrackerService {

    private final VehicleKmTrackerRepository vehicleKmTrackerRepository;

    private final ServiceVehicleService serviceVehicleService;

    public VehicleKmTrackerService(VehicleKmTrackerRepository vehicleKmTrackerRepository,
                                   ServiceVehicleService serviceVehicleService) {
        this.vehicleKmTrackerRepository = vehicleKmTrackerRepository;
        this.serviceVehicleService = serviceVehicleService;
    }

    public List<VehicleKmTracker> findByServiceVehicleOrderByRecordedDateDesc(final ServiceVehicle serviceVehicle) {
        return vehicleKmTrackerRepository.findByServiceVehicleOrderByRecordedDateDesc(serviceVehicle);
    }

    public List<VehicleKmTrackerDTO> findMaxKMReadingPerRecordedDate(final int vehicleId) {
        return vehicleKmTrackerRepository.findMaxKMReadingPerRecordedDate(vehicleId);
    }

    /**
     * Adds new vehicle KM tracker entry. Checks first whether the vehicle exists. If not, throws exception. Also checks
     * whether the recently recorded KM entry has KM reading greater than the incoming one. Which results in invalid
     * data entry.
     *
     * @param vehicleKMRequest
     * @param vehicleRegNumber
     * @return
     */
    public VehicleKmTracker addNewEntry(VehicleKMRequest vehicleKMRequest, String vehicleRegNumber) {
        // Check whether the vehicle exists. if not throw exception.
        final var byVehicleRegNumber =
                serviceVehicleService.findByVehicleRegNumber(vehicleRegNumber);
        if (Objects.isNull(byVehicleRegNumber))
            throw new VehicleServicesException(String.format("Vehicle %s doesn't exists. Can not update KM details",
                    vehicleRegNumber));
        final var vehicleKMTrackerList =
                vehicleKmTrackerRepository.findByServiceVehicle_RegNumberOrderByRecordedDateDesc(vehicleRegNumber);


        // Checks whether the most recent entry has KM reading greater than the requested one.
        if (null != vehicleKMTrackerList && !vehicleKMTrackerList.isEmpty()
                && vehicleKMTrackerList.get(0).getKmReading() > vehicleKMRequest.getKmReading())
            throw new VehicleServicesException(String.format("Invalid entry. Most recent KM details recorded on %s " +
                            "with KM reading %d.", vehicleKMTrackerList.get(0).getRecordedDate(),
                    vehicleKMTrackerList.get(0).getKmReading()));

        // Build entity
        final var vehicleKmTracker =
                VehicleKmTrackerBuilder.aVehicleKmTracker().withKmReading(vehicleKMRequest.getKmReading())
                        .withSource(vehicleKMRequest.getSource()).withRecordedDate(vehicleKMRequest.getRecordedDate())
                        .withServiceVehicle(byVehicleRegNumber)
                        .build();

        return vehicleKmTrackerRepository.save(vehicleKmTracker);

    }
}
