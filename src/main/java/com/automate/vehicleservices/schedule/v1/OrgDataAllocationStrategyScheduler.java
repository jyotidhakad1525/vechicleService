//package com.automate.vehicleservices.schedule.v1;
//
//import com.automate.vehicleservices.entity.MdOrganization;
//import com.automate.vehicleservices.entity.OrgDataAllocationStrategyType;
//import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
//import com.automate.vehicleservices.entity.enums.AllocationType;
//import com.automate.vehicleservices.repository.MdOrganizationRepository;
//import com.automate.vehicleservices.repository.OrgDataAllocationStrategyTypeRepository;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//@Component
//@Slf4j
//@AllArgsConstructor
//public class OrgDataAllocationStrategyScheduler {
//
//    private final OrgDataAllocationStrategyTypeRepository allocationStrategyTypeRepository;
//    private final MdOrganizationRepository mdOrganizationRepository;
//
//
//    private List<OrgDataAllocationStrategyType> getDefaultData(MdOrganization organization) {
//        Date currentDate = Calendar.getInstance().getTime();
//        return Arrays.asList(
//                new OrgDataAllocationStrategyType(null, AllocationType.ROUND_ROBIN, ActiveInActiveStatus.ACTIVE, organization, currentDate, currentDate),
//                new OrgDataAllocationStrategyType(null, AllocationType.MANUAL, ActiveInActiveStatus.INACTIVE, organization, currentDate, currentDate)
//        );
//    }
//
//    @Scheduled(cron = "${round-robin-default-data-added.schedule-cron-expression}")
//    public void allocationStrategy() {
//        log.info(":::::::::::::scheduler started ::::::" + new Date());
//        // fetch all org
//        Iterable<MdOrganization> organizationList = mdOrganizationRepository.findAll();
//
//        for (MdOrganization organization : organizationList) {
//            List<OrgDataAllocationStrategyType> allocationStrategyTypeList = allocationStrategyTypeRepository.findAllByOrganizationId(organization.getId());
//            if (allocationStrategyTypeList.isEmpty()) {
//                log.info("Not found with given org id :: " + organization.getId());
//                allocationStrategyTypeRepository.saveAll(getDefaultData(organization));
//            }
//        }
//        log.info(":::::::::::::scheduler ended ::::::" + new Date());
//    }
//}
