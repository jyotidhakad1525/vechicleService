package com.automate.vehicleservices.service;

import com.automate.vehicleservices.entity.RoDataDetails;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.RoDataDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class RoDataUploadService {

    private final RoDataExcelReader roDataExcelReader;
    private final ServiceReminderFollowUpActivityService serviceReminderFollowUpActivityService;

    private final RoDataDetailsRepository roDataDetailsRepository;

    public RoDataUploadService(RoDataExcelReader roDataExcelReader, ServiceReminderFollowUpActivityService serviceReminderFollowUpActivityService, RoDataDetailsRepository roDataDetailsRepository) {
        this.roDataExcelReader = roDataExcelReader;
        this.serviceReminderFollowUpActivityService = serviceReminderFollowUpActivityService;
        this.roDataDetailsRepository = roDataDetailsRepository;
    }


    @Async
    public void uploadRoData(MultipartFile file, int tenantId, String tenant) {

        log.info(String.format("File type: %s", file.getContentType()));
        try {
            final var read = roDataExcelReader.read(file.getInputStream(), tenant);
            List<RoDataDetails> result = roDataDetailsRepository.saveAll(read);
            for (RoDataDetails roData : result) {
                serviceReminderFollowUpActivityService.updateBookedVehicleRoData(roData, tenantId);
            }
        } catch (IOException e) {
            throw new VehicleServicesException("Upload failed. Exception while reading file.");
        }
    }
}
