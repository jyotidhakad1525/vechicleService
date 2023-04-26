package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.*;
import com.automate.vehicleservices.entity.builder.MdServiceCategoryBuilder;
import com.automate.vehicleservices.entity.builder.MdServiceRateCardBuilder;
import com.automate.vehicleservices.entity.builder.MdServiceTypeBuilder;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MdServiceTypeRepositoryTest extends BaseTest {

    Random random = new Random();
    @Autowired
    private MdServiceTypeRepository mdServiceTypeRepository;
    @Autowired
    private MdTenantRepository mdTenantRepository;
    @Autowired
    private MdServiceItemRepository mdServiceItemRepository;
    @Autowired
    private MdServiceRateCardRepository mdServiceRateCardRepository;
    @Autowired
    private MdMaintenanceTypeRepository mdMaintenanceTypeRepository;

    @Autowired
    private MdServiceCategoryRepository mdServiceCategoryRepository;

    public double randomDouble() {
        Random random = new Random();
        int max = 2;
        int min = 0;
        String val = Double.toString(min + random.nextDouble() * (max - min));
        BigDecimal bd = new BigDecimal(val);
        return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

    }

    /**
     * used to dump Auto data into DB. Dont enable with regular tests.
     */
    //@Test
    //@Transactional -- Rollbacks the changes
    void dumpAutoData() {
        final List<String> tenants = List.of("bhrth_Auto_Corp_Khammam"
                , "bhrth_Auto_Corp_Kothagudem"
                , "bhrth_Auto_Corp_Sathupalli"
                , "bhrth_Auto_Corp_Bhadrachalam"
                , "bhrth_Auto_Corp_Yellandu"
                , "bhrth_Auto_Corp_Thallada"
                , "bhrth_Auto_Corp_Manuguru"
                , "bhrth_Auto_Corp_Madhira");

        final var free_service = "Free Service";
        final var periodic_maintenance_service = "Periodic Maintenance Service";
        final var periodic_maintenance_check = "Periodic Maintenance Check";
        final var running_repair = "Running Repair";
        final var body_shop = "Body Shop";
        final var quick_service = "Quick Service";

        final List<String> categoryNames = List.of(quick_service,
                free_service,
                periodic_maintenance_service,
                periodic_maintenance_check,
                running_repair,
                body_shop);

        String firstFreeService = "1st Free Service";
        String secondFreeService = "2nd Free Service";
        String thirdFreeService = "3rd Free Service";
        String fourthFreeService = "4th Free Service";
        String fifthFreeService = "5th Free Service";

        List<String> freeServices = List.of(firstFreeService, secondFreeService, thirdFreeService, fourthFreeService,
                fifthFreeService);

        Map<String, String> qsmap = new HashMap<>();

        qsmap.put("SC", "Service Camp");
        qsmap.put("ACC", "Accessories");
        qsmap.put("ACC", "Accessories");
        qsmap.put("REFF", "Refurbishment");
        qsmap.put("TRN", "Transit Damage");
        qsmap.put("BDW", "Before Delivery Works");
        qsmap.put("TV", "True Value");
        qsmap.put("RJ", "Repeat Job");
        qsmap.put("Wash", "Washing");
        qsmap.put("OS", "On Road Service");
        qsmap.put("DSS", "Door Step Service");


        tenants.stream().map(str -> tenantRepository.findByTenantIdentifier(str)).forEach(tenant ->
                {
                    final var freeServiceCategory =
                            MdServiceCategoryBuilder.aMdServiceCategory().withMdTenant(tenant).withActive(true)
                                    .withCategoryName(free_service).withServiceGroup(ServiceGroup.REGULAR_MAINTENANCE).build();

                    addFreeServices(freeServices, tenant, freeServiceCategory);
                    mdServiceCategoryRepository.save(freeServiceCategory);

                    final var pmsCategory =
                            MdServiceCategoryBuilder.aMdServiceCategory().withMdTenant(tenant).withActive(true)
                                    .withCategoryName(periodic_maintenance_service)
                                    .withServiceGroup(ServiceGroup.REGULAR_MAINTENANCE).build();
                    pmsCategory.addMdServiceType(MdServiceTypeBuilder.aMdServiceType().withMdTenant(tenant).withActive(true)
                            .withServiceName(periodic_maintenance_service).withServiceCode("PMS").build());
                    mdServiceCategoryRepository.save(pmsCategory);

                    final var pmcCategory =
                            MdServiceCategoryBuilder.aMdServiceCategory().withMdTenant(tenant).withActive(true)
                                    .withCategoryName(periodic_maintenance_check)
                                    .withServiceGroup(ServiceGroup.QUICK_SERVICE).build();
                    pmcCategory.addMdServiceType(MdServiceTypeBuilder.aMdServiceType().withMdTenant(tenant).withActive(true)
                            .withServiceName(periodic_maintenance_check).withServiceCode("PMC").build());
                    mdServiceCategoryRepository.save(pmcCategory);

                    final var rr =
                            MdServiceCategoryBuilder.aMdServiceCategory().withMdTenant(tenant).withActive(true)
                                    .withCategoryName(running_repair)
                                    .withServiceGroup(ServiceGroup.QUICK_SERVICE).build();
                    rr.addMdServiceType(MdServiceTypeBuilder.aMdServiceType().withMdTenant(tenant).withActive(true)
                            .withServiceName(running_repair).withServiceCode("RR").build());
                    mdServiceCategoryRepository.save(rr);


                    final var bodyShop =
                            MdServiceCategoryBuilder.aMdServiceCategory().withMdTenant(tenant).withActive(true)
                                    .withCategoryName(body_shop)
                                    .withServiceGroup(ServiceGroup.BODY_REPAIR).build();
                    bodyShop.addMdServiceType(MdServiceTypeBuilder.aMdServiceType().withMdTenant(tenant).withActive(true)
                            .withServiceName(body_shop).withServiceCode("BANDP").build());
                    mdServiceCategoryRepository.save(bodyShop);

                    final var qs =
                            MdServiceCategoryBuilder.aMdServiceCategory().withMdTenant(tenant).withActive(true)
                                    .withCategoryName(quick_service)
                                    .withServiceGroup(ServiceGroup.QUICK_SERVICE).build();
                    qsmap.entrySet().forEach(entry -> {
                        qs.addMdServiceType(MdServiceTypeBuilder.aMdServiceType().withMdTenant(tenant).withActive(true)
                                .withServiceName(entry.getValue()).withServiceCode(entry.getKey()).build());
                    });
                    mdServiceCategoryRepository.save(qs);
                }
        );
    }

    private void addFreeServices(List<String> freeServices, MdTenant tenant, MdServiceCategory regularMaintenance) {
        String free_prefix = "Free_";
        AtomicInteger count = new AtomicInteger(1);
        freeServices.forEach(freeService -> {
            regularMaintenance
                    .addMdServiceType(MdServiceTypeBuilder.aMdServiceType().withMdTenant(tenant).withActive(true)
                            .withServiceName(freeService).withServiceCode(free_prefix + (count.getAndIncrement()))
                            .build());
        });
    }

    // @Test
    @Transactional
    @Rollback
    void testSave() {
        MdServiceType mdServiceType = mdServiceTypeRepository
                .save(MdServiceTypeBuilder.aMdServiceType().withMdServiceCategory(
                        MdServiceCategoryBuilder.aMdServiceCategory().withCategoryName("test-category")
                                .withServiceGroup(ServiceGroup.QUICK_SERVICE)
                                .withMdTenant(tenantRepository.findByTenantIdentifier("bhrthyund")).build())
                        .withServiceCode("test_st1")
                        .withServiceName("test Service type")
                        .withMdTenant(tenantRepository.findByTenantIdentifier("bhrthyund")).build());
        assertEquals(mdServiceType.getServiceName(), "test Service type");
        assertEquals(mdServiceType.getMdServiceCategory().getCategoryName(), "test-category");

    }

    //@Test
    @Transactional
    @Rollback
    void testSaveServiceItems() {
        Iterable<MdServiceType> allServiceTypes = mdServiceTypeRepository.findAll();
        Optional<MdServiceType> first = StreamSupport.stream(allServiceTypes.spliterator(), false)
                .filter(item -> item.getId() == 2).findFirst();

        List<MdServiceType> collect = StreamSupport.stream(allServiceTypes.spliterator(), false)
                .filter(item -> item.getId() > 3 && item.getId() < 14).map(mdServiceType -> {
                    mdServiceType.getServiceItems().addAll(first.get().getServiceItems());
                    return mdServiceType;
                }).collect(Collectors.toList());

        Iterable<MdServiceType> mdServiceTypes = mdServiceTypeRepository.saveAll(collect);


    }

    /**
     * Used to dump test data into database.
     */
    // @Test
    @Transactional
    void serviceTypeServiceItemJoinTableBulkData() {
        List.of("bhrth_Moto_Corp_Kothagudem",
                "bhrth_Moto_Corp_Sathupalli",
                "bhrth_Moto_Corp_Hyderabad").forEach(t -> {
            MdTenant tenant = mdTenantRepository.
                    findByTenantIdentifier(t);
            List<MdServiceType> serviceTypes =
                    mdServiceTypeRepository.findByMdTenantAndActiveTrueAndIdGreaterThan(tenant, 0);

            List<MdServiceItem> serviceItems = mdServiceItemRepository.findByMdTenantAndIsActive(tenant, true);

            Map<String, MdServiceType> serviceTypeMap =
                    serviceTypes.stream().collect(Collectors.toMap(MdServiceType::getServiceCode, Function.identity()));

            Map<String, MdServiceItem> serviceItemMap =
                    serviceItems.stream().collect(Collectors.toMap(MdServiceItem::getShortCode, Function.identity()));

            for (Map.Entry<String, MdServiceType> serviceTypeEntry : serviceTypeMap.entrySet()) {
                if (serviceTypeEntry.getKey().contains("FREE") || serviceTypeEntry.getKey().contains("PMS")) {
                    Set<MdServiceItem> gs_ =
                            serviceItemMap.entrySet().stream().filter(entry -> entry.getKey().contains("GS_"))
                                    .map(entry -> entry.getValue()).collect(Collectors.toSet());
                    serviceTypeEntry.getValue().addServiceItems(gs_);
                }

                if (serviceTypeEntry.getKey().contains("VAS")) {
                    Set<MdServiceItem> gs_ =
                            serviceItemMap.entrySet().stream().filter(entry -> entry.getKey().contains("VAS_"))
                                    .map(entry -> entry.getValue()).collect(Collectors.toSet());
                    serviceTypeEntry.getValue().addServiceItems(gs_);
                }

                if (serviceTypeEntry.getKey().contains("Quick_1")) {
                    Set<MdServiceItem> gs_ =
                            serviceItemMap.entrySet().stream().filter(entry -> entry.getKey().contains("QS_"))
                                    .map(entry -> entry.getValue()).collect(Collectors.toSet());
                    serviceTypeEntry.getValue().addServiceItems(gs_);
                }

                mdServiceTypeRepository.save(serviceTypeEntry.getValue());

            }

        });
    }

    /**
     * Used to dump test data into database.
     */
    // @Test
    @Transactional
    void bulkServiceRateCardData() {

        MdTenant referenceTenant = mdTenantRepository.findByTenantIdentifier("bhrthyund");
        List<String> vehicles = Arrays.asList("Santro",
                "Grand-i10",
                "Elite-i20",
                "Creta",
                "Venue",
                "Xcent",
                "Grand-i10-Nios");
        List<MdServiceRateCard> serviceRateCardReference =
                mdServiceRateCardRepository.findByMdTenantAndAndVehicleModelIn(referenceTenant, vehicles);
        List<MdServiceRateCard> mdServiceRateCards = new ArrayList<>();
        List.of("bhrth_Moto_Corp_Khammam",
                "bhrth_Moto_Corp_Kothagudem",
                "bhrth_Moto_Corp_Sathupalli",
                "bhrth_Moto_Corp_Hyderabad").forEach(t -> {
            MdTenant tenantToUpdate = mdTenantRepository.
                    findByTenantIdentifier(t);
            List<MdServiceType> serviceTypesToUpdate =
                    mdServiceTypeRepository.findByMdTenantAndActiveTrueAndIdGreaterThan(tenantToUpdate, 0);
            List<MdServiceItem> serviceItems = mdServiceItemRepository.findByMdTenantAndIsActive(tenantToUpdate, true);
            Map<String, MdServiceType> serviceTypesToUpdateMap =
                    serviceTypesToUpdate.stream().collect(Collectors.toMap(MdServiceType::getServiceCode,
                            Function.identity()));
            Map<String, MdServiceItem> serviceItemMap =
                    serviceItems.stream().collect(Collectors.toMap(MdServiceItem::getShortCode, Function.identity()));

            List<MdMaintenanceType> maintenanceTypes = mdMaintenanceTypeRepository.findByMdTenant(tenantToUpdate);
            Map<String, MdMaintenanceType> maintenanceTypeMap =
                    maintenanceTypes.stream().collect(Collectors.toMap(MdMaintenanceType::getShortCode,
                            Function.identity()));


            for (MdServiceRateCard mdServiceRateCard : serviceRateCardReference) {
                MdServiceType mdServiceType =
                        serviceTypesToUpdateMap.get(mdServiceRateCard.getMdServiceType().getServiceCode());
                double rate = mdServiceRateCard.getRate() * randomDouble();
                mdServiceRateCards.add(MdServiceRateCardBuilder.aMdServiceRateCard()
                        .withRate(rate)
                        .withMdServiceItem(serviceItemMap.get(mdServiceRateCard.getMdServiceItem().getShortCode()))
                        .withMdMaintenanceType(null != mdServiceRateCard.getMdMaintenanceType() ?
                                maintenanceTypeMap.get(mdServiceRateCard.getMdMaintenanceType().getShortCode()) : null)
                        .withApplicableForPetrol(mdServiceRateCard.getApplicableForPetrol())
                        .withMdServiceType(mdServiceType)
                        .withMdServiceTypeCriteria(null != mdServiceType.getMdServiceTypeCriteria() && mdServiceType
                                .getMdServiceTypeCriteria().size() > 0 ?
                                mdServiceType.getMdServiceTypeCriteria().get(0) : null)
                        .withMdTenant(tenantToUpdate)
                        .withVehicleModel(mdServiceRateCard.getVehicleModel())
                        .withApplicableForDiesel(mdServiceRateCard.getApplicableForDiesel())
                        .withApplicableForElectric(mdServiceRateCard.getApplicableForElectric())
                        .withApplicableForHybrid(mdServiceRateCard.getApplicableForHybrid())
                        .build());

            }
        });

        mdServiceRateCardRepository.saveAll(mdServiceRateCards);

    }
}